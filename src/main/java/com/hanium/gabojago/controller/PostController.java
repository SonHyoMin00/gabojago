package com.hanium.gabojago.controller;

import com.hanium.gabojago.dto.post.PostCreateRequest;
import com.hanium.gabojago.dto.post.PostPageResponse;
import com.hanium.gabojago.dto.post.PostResponse;
import com.hanium.gabojago.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
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
    @GetMapping("id/{id}")
    public PostResponse getPost(@PathVariable Long id) {
        return postService.getPost(id);
    }

    // 게시글 작성
    @PostMapping
    public Long createPost(@RequestBody PostCreateRequest postCreateRequest) {
        log.info(String.valueOf(postCreateRequest));
        log.info("게시글에 등록 요청된 태그: " + postCreateRequest.getTags().toString());
        return postService.createPost(postCreateRequest);
    }

    // 게시글 수정
    @PutMapping("{id}")
    public Long updatePost(@RequestBody PostCreateRequest postCreateRequest, String email) {
        return postService.updatePost(postCreateRequest, email);
    }

    // 게시글 삭제
    @DeleteMapping("{id}")
    public Long deletePost(@PathVariable Long id, @RequestParam String email) {
        return postService.deletePost(id, email);
    }
}
