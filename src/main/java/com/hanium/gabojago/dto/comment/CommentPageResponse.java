package com.hanium.gabojago.dto.comment;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class CommentPageResponse {
    private int totalPages;
    private List<CommentResponse> comments;

    @Builder
    public CommentPageResponse(int totalPages, List<CommentResponse> comments) {
        this.totalPages = totalPages;
        this.comments = comments;
    }
}
