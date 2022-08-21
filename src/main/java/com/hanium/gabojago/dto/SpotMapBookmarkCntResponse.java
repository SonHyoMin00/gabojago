package com.hanium.gabojago.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SpotMapBookmarkCntResponse {
    private Long bookmarkCnt;
    private SpotMapResponse spotMapResponses;

    @Builder
    public SpotMapBookmarkCntResponse(Long bookmarkCnt, SpotMapResponse spotMapResponses) {
        this.bookmarkCnt = bookmarkCnt;
        this.spotMapResponses = spotMapResponses;
    }
}
