package com.hanium.gabojago.controller;

import com.hanium.gabojago.domain.User;
import com.hanium.gabojago.dto.comment.CommentPageResponse;
import com.hanium.gabojago.dto.comment.CommentRequest;
import com.hanium.gabojago.service.CommentService;
import com.hanium.gabojago.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RequiredArgsConstructor
@RestController
@RequestMapping("comments")
public class CommentController {
    private final CommentService commentService;
    private final UserService userService;

    // 특정 게시글의 전체 댓글 조회
    @GetMapping("post/{id}")
    public CommentPageResponse getComments(@PathVariable Long id,
                                           @RequestParam(required = false, defaultValue = "1", value = "page")int page,
                                           @RequestParam(required = false, defaultValue = "10", value = "size")int size) {
        return commentService.getComments(id, page - 1, size);
    }

    // 댓글 등록
    @PostMapping("post/{id}")
    public Long makeComment(@PathVariable Long id,
                            @RequestBody CommentRequest commentRequest,
                            HttpServletRequest httpServletRequest) {

        String token = httpServletRequest.getHeader("Authorization");
        User user = userService.findUserByJwtToken(token);

        return commentService.makeComment(id, commentRequest, user);
    }

    // 댓글 수정
    @PutMapping("{id}")
    public Long updateComment(@PathVariable Long id,
                              @RequestBody CommentRequest commentRequest,
                              HttpServletRequest httpServletRequest) {

        String token = httpServletRequest.getHeader("Authorization");
        User user = userService.findUserByJwtToken(token);

        return commentService.updateComment(id, commentRequest, user);
    }

    //댓글 삭제
    @DeleteMapping("{id}")
    public Long deleteComment(@PathVariable Long id,
                              HttpServletRequest httpServletRequest) {

        String token = httpServletRequest.getHeader("Authorization");
        User user = userService.findUserByJwtToken(token);

        return commentService.deleteComment(id, user);
    }
}
