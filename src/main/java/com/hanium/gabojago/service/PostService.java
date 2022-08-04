package com.hanium.gabojago.service;

import com.hanium.gabojago.domain.Post;
import com.hanium.gabojago.domain.User;
import com.hanium.gabojago.dto.post.PostCreateRequest;
import com.hanium.gabojago.dto.post.PostPageResponse;
import com.hanium.gabojago.dto.post.PostResponse;
import com.hanium.gabojago.repository.PostRepository;
import com.hanium.gabojago.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;

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

    public PostPageResponse getPosts(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Post> posts = postRepository.findAllSpotsByPage(pageable);
        return convertSPostsToPostPageResponse(posts);
    }

    @Transactional(readOnly = true)
    public PostResponse getPost(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("id " + id + "에 해당하는 게시글이 존재하지 않습니다."));
        return PostResponse.builder()
                .entity(post)
                .build();
    }

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

        postRepository.save(post);

        return post.getPostId();
    }

    public Long updatePost(PostCreateRequest postCreateRequest, String email) {
        return 0L;
    }

    public Long deletePost(Long id, String email) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("id " + id + "에 해당하는 게시글이 존재하지 않습니다."));

        //요청을 보낸 사용자의 이메일과 post의 이메일이 같은지 검사
        if (!email.equals(post.getUser().getEmail())) {
            throw new IllegalArgumentException("잘못된 접근입니다.");
        }

        postRepository.delete(post);
        return id;
    }
}
