package com.hanium.gabojago.dto.post;

import com.hanium.gabojago.domain.PostTag;
import lombok.Builder;
import lombok.Getter;

@Getter
public class PostTagResponse {
    private String value;

    @Builder
    public PostTagResponse(PostTag postTag) {
        this.value = postTag.getTag().getValue();
    }
}
