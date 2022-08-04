package com.hanium.gabojago.dto.post;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter @Setter
public class PostCreateRequest {
    private String email;
    private String title;
    private String context;
}
