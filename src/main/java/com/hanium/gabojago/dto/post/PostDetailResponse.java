package com.hanium.gabojago.dto.post;

import com.hanium.gabojago.domain.Post;
import lombok.Getter;

@Getter
public class PostDetailResponse extends PostResponse{
    private Long spotId;
    private boolean greatState;

    public PostDetailResponse(Post entity, Long spotId, boolean greatState) {
        super(entity);
        this.spotId = spotId;
        this.greatState = greatState;
    }
}
