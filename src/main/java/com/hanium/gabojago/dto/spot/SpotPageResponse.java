package com.hanium.gabojago.dto.spot;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public class SpotPageResponse {
    private int totalPages;
    private List<SpotResponse> spotResponses;

    @Builder
    public SpotPageResponse(int totalPages, List<SpotResponse> spotResponses) {
        this.totalPages = totalPages;
        this.spotResponses = spotResponses;
    }
}
