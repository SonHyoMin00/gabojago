package com.hanium.gabojago.dto.post;

import com.hanium.gabojago.domain.Post;
import com.hanium.gabojago.domain.PostTag;
import com.hanium.gabojago.domain.Tag;
import com.hanium.gabojago.domain.User;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@ToString
@Getter @Setter
public class PostSaveRequest {
    private String title;
    private String context;
    private List<Integer> tags;
    private List<MultipartFile> files;

    public Post toPost(User user) {
        return Post.builder()
                .user(user)
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
