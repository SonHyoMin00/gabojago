package com.hanium.gabojago.dto;

import com.hanium.gabojago.domain.Spot;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class SpotBookmarkResponse {
    private Long spotId;
    private String spotName;
    private String address;
    private String region;
    private String detail;
    private String tel;
    private String spotImage;
    private Integer viewCnt;
    private List<BookmarkResponse> bookmarks;

    public SpotBookmarkResponse(Spot entity) {
        this.spotId = entity.getSpotId();
        this.spotName = entity.getSpotName();
        this.address = entity.getAddress();
        this.region = entity.getRegion();
        this.detail = entity.getDetail();
        this.tel = entity.getTel();
        this.spotImage = entity.getSpotImage();
        this.viewCnt = entity.getViewCnt();
        this.bookmarks = entity.getBookmarks().stream()
                .map(BookmarkResponse::new).collect(Collectors.toList());
    }
}
