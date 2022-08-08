package com.hanium.gabojago.service;

import com.hanium.gabojago.domain.Post;
import com.hanium.gabojago.domain.PostTag;
import com.hanium.gabojago.domain.Tag;
import com.hanium.gabojago.domain.User;
import com.hanium.gabojago.dto.post.PostCreateRequest;
import com.hanium.gabojago.dto.post.PostPageResponse;
import com.hanium.gabojago.dto.post.PostResponse;
import com.hanium.gabojago.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final TagRepository tagRepository;
    private final PostTagRepository postTagRepository;

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

    // 특정 게시글 조회
    @Transactional(readOnly = true)
    public PostResponse getPost(Long id) {
        Post post = postRepository.findByIdWithUser(id)
                .orElseThrow(() -> new IllegalArgumentException("id " + id + "에 해당하는 게시글이 존재하지 않습니다."));
        return PostResponse.builder()
                .entity(post)
                .build();
    }

    // 게시글 작성
    @Transactional
    public Long createPost(PostCreateRequest postCreateRequest) {
        String email = postCreateRequest.getEmail();
        User user = userRepository.findByEmail(email)
                .orElseThrow(()-> new IllegalArgumentException("이메일 \"" + email + "\"에 해당하는 사용자가 존재하지 않습니다."));

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

        // 태그 수만큼 쿼리 추가 발생..! 최적화 필요?
        postRepository.save(post);
        return post.getPostId();
    }

    // 게시글 수정
    @Transactional
    public Long updatePost(Long id, PostCreateRequest postCreateRequest) {
        // 1. 게시글 존재 여부 확인
         Post post = postRepository.findByIdWithUser(id)
                .orElseThrow(() -> new IllegalArgumentException("id " + id + "에 해당하는 게시글이 존재하지 않습니다."));

        // 2. 요청을 보낸 사용자(로그인된 사용자)의 이메일과 post의 이메일이 같은지 검사
        String email = postCreateRequest.getEmail();
        if (!email.equals(post.getUser().getEmail())) {
            throw new IllegalArgumentException("잘못된 접근입니다.");
        }

        // 수정 로직
        // 3. 기존에 등록된 태그들 삭제
        List<PostTag> originalPostTags = post.getPostTags();
        if (!originalPostTags.isEmpty()) {
            postTagRepository.deleteAllInBatch(originalPostTags);
        }

        // 4. 새로 등록할 태그 조회
        List<Integer> tags = postCreateRequest.getTags();
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

        // 6. post 수정
        post.updatePost(postCreateRequest, newPostTags);
        return post.getPostId();
    }

    // 게시글 삭제
    public Long deletePost(Long id, String email) {
        // 1. 게시글 조회
        Post post = postRepository.findByIdWithUser(id)
                .orElseThrow(() -> new IllegalArgumentException("id " + id + "에 해당하는 게시글이 존재하지 않습니다."));

        // 2. 요청을 보낸 사용자(로그인된 사용자)의 이메일과 post의 이메일이 같은지 검사
        if (!email.equals(post.getUser().getEmail())) {
            throw new IllegalArgumentException("잘못된 접근입니다.");
        }

        // 3. 게시글 삭제
        // on delete cascade 제약조건이라 post만 지워도 post_tag는 알아서 삭제되는데 post_tag 삭제 쿼리가 n개만큼 먼저 나가는 문제
        postRepository.delete(post);
        return id;
    }

//    //이미지 저장 함수
//    public String saveProfileImage(Long postId, MultipartFile imageFile) {
//       return null;
//    }
}