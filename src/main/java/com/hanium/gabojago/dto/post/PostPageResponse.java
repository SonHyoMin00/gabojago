package com.hanium.gabojago.dto.post;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public class PostPageResponse {
    private int totalPages;
    private List<PostResponse> spotResponses;
}
