package com.hanium.gabojago.dto.bookmark;

import com.hanium.gabojago.domain.Spot;
import lombok.Getter;
import lombok.Setter;

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
    private Long bookmarkCnt;

    public SpotBookmarkResponse(Spot entity, Long bookmarkCnt) {
        this.spotId = entity.getSpotId();
        this.spotName = entity.getSpotName();
        this.address = entity.getAddress();
        this.region = entity.getRegion();
        this.detail = entity.getDetail();
        this.tel = entity.getTel();
        this.spotImage = entity.getSpotImage();
        this.viewCnt = entity.getViewCnt();
        this.bookmarkCnt = bookmarkCnt;
    }
}
