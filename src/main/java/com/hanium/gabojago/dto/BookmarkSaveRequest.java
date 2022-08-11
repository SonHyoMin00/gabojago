package com.hanium.gabojago.dto;

import lombok.*;

import java.util.List;

@ToString
@Getter @Setter
@NoArgsConstructor
public class BookmarkSaveRequest {
    private Long spotId;
    private String email;

    @Builder
    public BookmarkSaveRequest(Long SpotId, String email){
        this.spotId = getSpotId();
        this.email = getEmail();
    }
}
