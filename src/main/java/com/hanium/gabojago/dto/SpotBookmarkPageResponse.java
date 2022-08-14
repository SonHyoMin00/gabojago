package com.hanium.gabojago.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public class SpotBookmarkPageResponse {
    private int totalPages;
    private List<SpotBookmarkResponse> spotBookmarkResponses;

    @Builder
    public SpotBookmarkPageResponse(int totalPages, List<SpotBookmarkResponse> spotBookmarkResponses) {
        this.totalPages = totalPages;
        this.spotBookmarkResponses = spotBookmarkResponses;
    }
}
