package com.hanium.gabojago.dto.comment;

import com.hanium.gabojago.domain.Comment;
import com.hanium.gabojago.util.properties.ApplicationProperties;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CommentResponse {
    private final Long id;
    private final String userName;
    private final String profilePhoto;
    private final String context;
    private final LocalDateTime createdAt;

    @Builder
    public CommentResponse(Comment comment) {
        this.id = comment.getCommentId();
        this.userName = comment.getUser().getName();
        this.profilePhoto = ApplicationProperties.HOST_IMAGE_URL
                            + comment.getUser().getProfilePhoto();
        this.context = comment.getContext();
        this.createdAt = comment.getCreatedAt();
    }
}
