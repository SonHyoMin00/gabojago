package com.hanium.gabojago.dto.post;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
public class PostPageResponse {
    private int totalPages;
    private List<PostResponse> postResponses;

    @Builder
    public PostPageResponse(int totalPages, List<PostResponse> postResponses) {
        this.totalPages = totalPages;
        this.postResponses = postResponses;
    }
}
