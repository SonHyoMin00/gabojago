package com.hanium.gabojago.dto.spot;

import com.hanium.gabojago.domain.SpotTag;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class SpotTagResponse {
    private String value;

    @Builder
    public SpotTagResponse(SpotTag spotTag) {
        this.value = spotTag.getTag().getValue();
    }
}
