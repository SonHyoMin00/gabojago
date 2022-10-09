package com.hanium.gabojago.dto.spot;

import com.hanium.gabojago.domain.Post;
import com.hanium.gabojago.domain.Spot;
import com.hanium.gabojago.dto.post.PostResponse;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Getter @Setter
public class SpotDetailResponse {
    private Long spotId;
    private String spotName;
    private String address;
    private String region;
    private String detail;
    private String tel;
    private String spotImage;
    private Integer viewCnt;
    private BigDecimal spotX;
    private BigDecimal spotY;
    private List<PostResponse> posts;
    private List<SpotTagResponse> spotTags;
    private Long bookmarkCnt;

    @Builder
    public SpotDetailResponse(Spot entity, List<Post> posts, Long bookmarkCnt) {
        this.spotId = entity.getSpotId();
        this.spotName = entity.getSpotName();
        this.address = entity.getAddress();
        this.region = entity.getRegion();
        this.detail = entity.getDetail();
        this.tel = entity.getTel();
        this.spotImage = entity.getSpotImage();
        this.viewCnt = entity.getViewCnt();
        this.spotX = entity.getSpotX();
        this.spotY = entity.getSpotY();
        this.posts = entity.getRecent3Posts(posts);
        this.spotTags = entity.getSpotTags().stream()
                .map(SpotTagResponse::new).collect(Collectors.toList());
        this.bookmarkCnt = bookmarkCnt;

    }
}
