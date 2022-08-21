package com.hanium.gabojago.dto.postTag;

import com.hanium.gabojago.domain.PostTag;
import lombok.Builder;
import lombok.Getter;

@Getter
public class PostTagResponse {
    private Integer tagId;
    private String value;

    @Builder
    public PostTagResponse(PostTag postTag) {
        this.tagId = postTag.getTag().getTagId();
        this.value = postTag.getTag().getValue();
    }
}
