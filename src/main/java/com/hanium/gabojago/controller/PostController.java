package com.hanium.gabojago.controller;

import com.hanium.gabojago.dto.post.PostCreateRequest;
import com.hanium.gabojago.dto.post.PostPageResponse;
import com.hanium.gabojago.dto.post.PostResponse;
import com.hanium.gabojago.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("posts")
@RestController
public class PostController {
    private final PostService postService;

    // 전체 게시글 조회
    @GetMapping
    public PostPageResponse getPosts(
            @RequestParam(required = false, defaultValue = "1", value = "page")int page,
            @RequestParam(required = false, defaultValue = "10", value = "size")int size) {
        return postService.getPosts(page - 1, size);
    }

    // 특정 게시글 조회
    @PostMapping("id/{id}")
    public PostResponse getPost(@PathVariable Long id) {
        return postService.getPost(id);
    }

    // 게시글 작성
    @PostMapping
    public Long createPost(@RequestBody PostCreateRequest postCreateRequest) {
        return postService.createPost(postCreateRequest);
    }

    // 게시글 수정
    @PostMapping
    public Long updatePost(@RequestBody PostCreateRequest postCreateRequest) {
        return postService.updatePost(postCreateRequest);
    }

    // 게시글 삭제
    @PostMapping
    public Long deletePost(Long id, String email) {
        return postService.deletePost(id, email);
    }
}
