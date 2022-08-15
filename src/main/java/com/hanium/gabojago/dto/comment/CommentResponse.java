package com.hanium.gabojago.dto.comment;

import com.hanium.gabojago.domain.Comment;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CommentResponse {
    private Long id;
    private String userName;
    private String context;
    private LocalDateTime createdAt;

    @Builder
    public CommentResponse(Comment comment) {
        this.id = comment.getCommentId();
        this.userName = comment.getUser().getName();
        this.context = comment.getContext();
        this.createdAt = comment.getCreatedAt();
    }
}
