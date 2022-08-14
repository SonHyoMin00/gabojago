package com.hanium.gabojago.dto.post;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@ToString
@Getter @Setter
public class PostCreateRequest {
    private String title;
    private String context;
    private List<Integer> tags;
    private List<MultipartFile> files;
}
