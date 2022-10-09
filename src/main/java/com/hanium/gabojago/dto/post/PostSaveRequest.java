package com.hanium.gabojago.dto.post;

import com.hanium.gabojago.domain.*;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@ToString
@Getter @Setter
public class PostSaveRequest {
    private String title;
    private String context;
    private Long spotId;
    private List<Integer> tags;
    private List<MultipartFile> files;

    public Post toPost(User user, Spot spot) {
        return Post.builder()
                .user(user)
                .spot(spot)
                .title(title)
                .context(context)
                .build();
    }

    public PostTag toPostTag(Post post, Tag tag) {
        return PostTag.builder()
                .post(post)
                .tag(tag)
                .build();
    }
}
