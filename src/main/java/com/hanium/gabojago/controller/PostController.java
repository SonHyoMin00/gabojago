package com.hanium.gabojago.controller;

import com.hanium.gabojago.domain.User;
import com.hanium.gabojago.dto.post.PostCreateRequest;
import com.hanium.gabojago.dto.post.PostPageResponse;
import com.hanium.gabojago.dto.post.PostResponse;
import com.hanium.gabojago.dto.post.PostUpdateRequest;
import com.hanium.gabojago.service.PostService;
import com.hanium.gabojago.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("posts")
@RestController
public class PostController {
    private final PostService postService;
    private final UserService userService;

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
    public PostResponse getPost(@PathVariable Long id, HttpServletRequest httpServletRequest) {

        String token = httpServletRequest.getHeader("Authorization");
        User user = null;
        if (token != null)
            user = userService.findUserByJwtToken(token);

        return postService.getPost(id, user);
    }

    // 게시글 작성
    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public Long createPost(@RequestPart(name = "request") PostCreateRequest postCreateRequest,
                           @RequestPart(name = "files", required = false) List<MultipartFile> files,
                           HttpServletRequest httpServletRequest) {

        postCreateRequest.setFiles(files);
        log.info(String.valueOf(postCreateRequest));

        String token = httpServletRequest.getHeader("Authorization");
        User user = userService.findUserByJwtToken(token);

        return postService.createPost(postCreateRequest, user);
    }

    // 게시글 수정
    @PutMapping("{id}")
    public Long updatePost(@PathVariable Long id,
                           @RequestPart(name = "request") PostUpdateRequest postUpdateRequest,
                           @RequestPart(required = false) List<MultipartFile> files,
                           HttpServletRequest httpServletRequest) {

        postUpdateRequest.setInsertFiles(files);
        log.info(String.valueOf(postUpdateRequest));

        String token = httpServletRequest.getHeader("Authorization");
        User user = userService.findUserByJwtToken(token);

        return postService.updatePost(id, postUpdateRequest, user);
    }

    // 게시글 삭제
    @DeleteMapping("{id}")
    public Long deletePost(@PathVariable Long id, HttpServletRequest httpServletRequest) {

        String token = httpServletRequest.getHeader("Authorization");
        User user = userService.findUserByJwtToken(token);

        return postService.deletePost(id, user);
    }
}