package com.hanium.gabojago.controller;

import com.hanium.gabojago.dto.comment.CommentRequest;
import com.hanium.gabojago.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("comment")
public class CommentController {
    private final CommentService commentService;

    // 특정 게시글의 전체 댓글 조회

    // 댓글 등록
    @PostMapping
    public Long makeComment(@RequestParam String email,
                            @RequestBody CommentRequest commentRequest) {
        return commentService.makeComment(email);
    }

    // 댓글 수정
    @PutMapping("{id}")
    public Long updateComment(@PathVariable Long id,
                              @RequestParam String email,
                              @RequestBody CommentRequest commentRequest) {
        return commentService.updateComment(id, email);
    }

    //댓글 삭제
    @DeleteMapping("{id}")
    public Long deleteComment(@PathVariable Long id, @RequestParam String email) {
        return commentService.deleteComment(id, email);
    }
}
