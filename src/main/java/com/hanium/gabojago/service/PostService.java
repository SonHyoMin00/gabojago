package com.hanium.gabojago.service;

import com.hanium.gabojago.domain.*;
import com.hanium.gabojago.dto.post.*;
import com.hanium.gabojago.util.handler.FileHandler;
import com.hanium.gabojago.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class PostService {
    private final PostRepository postRepository;
    private final TagRepository tagRepository;
    private final PostTagRepository postTagRepository;
    private final GreatRepository greatRepository;
    private final PhotoRepository photoRepository;
    private final FileHandler fileHandler;

    @Value("${images.path.post}")
    private String postPath;

    // Page<Post>를 PostPageResponse(dto)로 바꾸는 함수
    // 계속 중복됨: 나중에 인터페이스로 나타내기
    private PostPageResponse convertSPostsToPostPageResponse(Page<Post> posts) {
        //총 페이지 수
        int totalPages = posts.getTotalPages();
        log.info("총 페이지 수: " + posts.getTotalPages());

        //spotResponses DTO로 변환
        List<PostResponse> postResponses = posts.getContent()
                .stream().map(PostResponse::new).collect(Collectors.toList());

        // 총 페이지 수 추가하여 반환
        return PostPageResponse.builder()
                .postResponses(postResponses)
                .totalPages(totalPages)
                .build();
    }

    // 전체 게시글 조회
    @Transactional(readOnly = true)
    public PostPageResponse getPosts(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Post> posts = postRepository.findAllSpotsByPage(pageable);
        return convertSPostsToPostPageResponse(posts);
    }

    //매달 top3 게시글 조회
    @Transactional(readOnly = true)
    public List<PostResponse> getTop3Posts() {
        int year = LocalDate.now().getYear();
        int preMonth = LocalDate.now().getMonthValue() - 1;
        if(preMonth == 0) {
            preMonth = 12;
            year--;
        }
        LocalDate startDate = LocalDate.of(year, preMonth, 1);
        LocalDateTime start = LocalDateTime.of(startDate, LocalTime.MIN);
        LocalDate endDate = startDate.plusDays(startDate.lengthOfMonth() - 1);
        LocalDateTime end = LocalDateTime.of(endDate, LocalTime.MAX);

        List<Post> posts = postRepository
                .findTop3ByCreatedAtBetweenOrderByGreatCntDescViewCntDescCreatedAtAsc(start, end);
        return posts.stream().map(PostResponse::new).collect(Collectors.toList());
    }

    // 특정 게시글 조회
    @Transactional
    public PostResponse getPost(Long id, User user) {
        Post post = postRepository.findByIdWithUser(id)
                .orElseThrow(() -> new IllegalArgumentException("id " + id + "에 해당하는 게시글이 존재하지 않습니다."));
        post.increaseViewCnt();

        // 로그인 한 상태인지 확인 -> 로그인 했다면 좋아요 여부 조회, 로그인 하지 않았다면 조회 없이 무조건 false
        boolean greatState = false;
        log.info("사용자: " + user);
        if(user != null) {
            Optional<Great> great = greatRepository.findByUserAndPost(post.getUser(), post);
            greatState = great.isPresent();
        }

        return new PostDetailResponse(post, greatState);
    }

    // 게시글 작성
    @Transactional
    public Long createPost(PostCreateRequest postCreateRequest, User user) {
        Post post = Post.builder()
                .user(user)
                .title(postCreateRequest.getTitle())
                .context(postCreateRequest.getContext())
                .build();

        List<Integer> tags = postCreateRequest.getTags();
        List<Tag> tagList = tagRepository.findAllById(tags);
        log.info("tagList: " + tagList);

        for (Tag tag: tagList) {
            PostTag postTag = PostTag.builder()
                    .post(post)
                    .tag(tag)
                    .build();

            post.getPostTags().add(postTag);
            log.info("포스트태그: " + postTag);
        }

        // 첨부파일(이미지) 처리
        List<Photo> photos = fileHandler.parseFileInfo(postCreateRequest.getFiles(), post);
        if(!photos.isEmpty()) post.getPhotos().addAll(photos);
        log.info("파일 리스트: " + photos);

        // 태그 수만큼 쿼리 추가 발생..! 최적화 필요?
        postRepository.save(post);
        return post.getPostId();
    }

    // 게시글 수정
    @Transactional
    public Long updatePost(Long id, PostUpdateRequest postUpdateRequest, User user) {
        // 1. 게시글 존재 여부 확인
         Post post = postRepository.findByIdWithUser(id)
                .orElseThrow(() -> new IllegalArgumentException("id " + id + "에 해당하는 게시글이 존재하지 않습니다."));

        // 2. 요청을 보낸 사용자가 post 작성자와 같은지 검사
        if (!user.equals(post.getUser())) {
            throw new IllegalArgumentException("잘못된 접근입니다.");
        }

        // 수정 로직
        // 3. 기존에 등록된 태그들 삭제
        List<PostTag> originalPostTags = post.getPostTags();
        if (!originalPostTags.isEmpty()) {
            postTagRepository.deleteAllInBatch(originalPostTags);
        }

        // 4. 새로 등록할 태그 조회
        List<Integer> tags = postUpdateRequest.getTags();
        List<Tag> tagList = tagRepository.findAllById(tags);
        log.info("tagList: " + tagList);

        // 5. 새로 등록할 postTag 객체 생성
        List<PostTag> newPostTags = new ArrayList<>();
        for (Tag tag: tagList) {
            PostTag postTag = PostTag.builder()
                    .post(post)
                    .tag(tag)
                    .build();
            newPostTags.add(postTag);
        }
        log.info("수정된 태그: " + newPostTags);

        // 6. 삭제를 요청받은 사진 삭제
        if(!postUpdateRequest.getDeleteFiles().isEmpty()) {
            List<Photo> photos = photoRepository.findAllById(postUpdateRequest.getDeleteFiles());
            fileHandler.deletePhotosInServer(photos);
            photoRepository.deleteAllInBatch(photos);
            log.info("사진 삭제: " + postUpdateRequest.getDeleteFiles().toString());
        }

        // 7. 추가 요청받은 사진 저장
        List<Photo> newPhotos = fileHandler.parseFileInfo(postUpdateRequest.getInsertFiles(), post);
        log.info("새로 추가된 사진: " + newPhotos.toString());

        // 8. post 수정
        post.updatePost(postUpdateRequest, newPostTags, newPhotos);
        return post.getPostId();
    }

    // 게시글 삭제
    @Transactional
    public Long deletePost(Long id, User user) {
        // 1. 게시글 조회
        Post post = postRepository.findByIdWithUser(id)
                .orElseThrow(() -> new IllegalArgumentException("id " + id + "에 해당하는 게시글이 존재하지 않습니다."));

        // 2. 요청을 보낸 사용자(로그인된 사용자)의 이메일과 post의 이메일이 같은지 검사
        if (!user.equals(post.getUser())) {
            throw new IllegalArgumentException("잘못된 접근입니다.");
        }

        // 3. 첨부파일 서버에서 삭제
        List<Photo> photos = photoRepository.findAllByPost(post);
        fileHandler.deletePhotosInServer(photos);

        // 4. 첨부파일 DB에서 삭제
        photoRepository.deleteAllInBatch(photos);
        log.info("사진 삭제 완료.");

        // 5. 게시글 삭제
        // on delete cascade 제약조건이라 post만 지워도 post_tag는 알아서 삭제되는데 post_tag 삭제 쿼리가 n개만큼 먼저 나가는 문제
        postRepository.delete(post);
        return id;
    }

    // 사용자 게시글 조회(마이페이지)
    public PostPageResponse getUserPosts(User user, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Post> posts = postRepository.findAllByUser(user, pageable);
        return convertSPostsToPostPageResponse(posts);
    }
}