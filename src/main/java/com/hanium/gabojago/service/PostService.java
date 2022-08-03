package com.hanium.gabojago.service;

import com.hanium.gabojago.domain.Post;
import com.hanium.gabojago.dto.post.PostCreateRequest;
import com.hanium.gabojago.dto.post.PostPageResponse;
import com.hanium.gabojago.dto.post.PostResponse;
import com.hanium.gabojago.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class PostService {
    private final PostRepository postRepository;

    public PostPageResponse getPosts(int page, int size) {
        return null;
    }

    @Transactional(readOnly = true)
    public PostResponse getPost(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("id " + id + "에 해당하는 게시글이 존재하지 않습니다."));
        return null;
    }

    public Long createPost(PostCreateRequest postCreateRequest) {
        return 0L;
    }

    public Long updatePost(PostCreateRequest postCreateRequest) {
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
