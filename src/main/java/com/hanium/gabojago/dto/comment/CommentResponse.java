package com.hanium.gabojago.dto.comment;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CommentResponse {
    private Long id;
    private String userName;
    private String context;
    private LocalDateTime createdAt;
}
