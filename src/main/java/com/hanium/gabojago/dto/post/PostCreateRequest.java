package com.hanium.gabojago.dto.post;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@ToString
@Getter @Setter
public class PostCreateRequest {
    private String email;
    private String title;
    private String context;
    private List<Integer> tags;
}
