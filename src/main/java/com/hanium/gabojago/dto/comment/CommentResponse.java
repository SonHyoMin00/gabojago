package com.hanium.gabojago.dto.comment;

import com.hanium.gabojago.domain.Comment;
import com.hanium.gabojago.dto.user.UserInfoResponse;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CommentResponse {
    private final Long commentId;
    private final UserInfoResponse user;
    private final String context;
    private final LocalDateTime createdAt;

    @Builder
    public CommentResponse(Comment comment) {
        this.commentId = comment.getCommentId();
        this.user = UserInfoResponse.builder()
                .user(comment.getUser())
                .build();
        this.context = comment.getContext();
        this.createdAt = comment.getCreatedAt();
    }
}
