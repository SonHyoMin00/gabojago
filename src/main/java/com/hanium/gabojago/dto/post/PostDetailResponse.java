package com.hanium.gabojago.dto.post;

import com.hanium.gabojago.domain.Post;
import lombok.Getter;

@Getter
public class PostDetailResponse extends PostResponse{
    private boolean greatState;

    public PostDetailResponse(Post entity, boolean greatState) {
        super(entity);
        this.greatState = greatState;
    }
}
