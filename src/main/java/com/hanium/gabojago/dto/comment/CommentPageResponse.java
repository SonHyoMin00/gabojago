package com.hanium.gabojago.dto.comment;

import lombok.Getter;

import java.util.List;

@Getter
public class CommentPageResponse {
    private int totalPages;
    private List<CommentResponse> commentResponses;
}
