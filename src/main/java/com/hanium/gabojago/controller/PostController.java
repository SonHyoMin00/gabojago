package com.hanium.gabojago.controller;

import com.hanium.gabojago.dto.post.PostCreateRequest;
import com.hanium.gabojago.dto.post.PostPageResponse;
import com.hanium.gabojago.dto.post.PostResponse;
import com.hanium.gabojago.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

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

    //매달 top3 게시글 조회
    @GetMapping("top3")
    public List<PostResponse> getTop3Posts() {
        return postService.getTop3Posts();
    }

    // 특정 게시글 조회(회원이면 - 좋아요 표시 여부를 함께 리턴, 비회원이면 - 좋아요 표시를 false로 리턴)
    @GetMapping("{id}")
    public PostResponse getPost(@PathVariable Long id, @RequestParam(required = false) String email) {
        return postService.getPost(id, email);
    }

    // 게시글 작성
    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public Long createPost(@RequestPart(name = "request") PostCreateRequest postCreateRequest,
                           @RequestPart(name = "files", required = false) List<MultipartFile> files) {
        postCreateRequest.setFiles(files);
        log.info(String.valueOf(postCreateRequest));
        log.info("게시글에 첨부된 파일: " + files.size());
        return postService.createPost(postCreateRequest);
    }

    // 게시글 수정
    @PutMapping("{id}")
    public Long updatePost(@PathVariable Long id,
                           @RequestPart PostCreateRequest postCreateRequest,
                           @RequestPart(required = false) List<MultipartFile> files) {
        postCreateRequest.setFiles(files);
        return postService.updatePost(id, postCreateRequest);
    }

    // 게시글 삭제
    @DeleteMapping("{id}")
    public Long deletePost(@PathVariable Long id, @RequestParam String email) {
        return postService.deletePost(id, email);
    }

    //특정 유저의 게시글 조회(마이페이지)
    @GetMapping("user")
    public PostPageResponse userPosts(@RequestParam String email,
                                      @RequestParam(required = false, defaultValue = "1", value = "page")int page,
                                      @RequestParam(required = false, defaultValue = "10", value = "size")int size) {
        return postService.getUserPosts(email, page - 1, size);
    }
}
