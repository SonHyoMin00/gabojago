package com.hanium.gabojago.dto.post;

import com.hanium.gabojago.domain.Post;
import com.hanium.gabojago.dto.user.UserPostResponse;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
public class PostResponse {
    private Long postId;
    private UserPostResponse user;
    private String title;
    private String context;
    private Integer viewCnt;
    private Integer greatCnt;
    private LocalDateTime createdAt;

    @Builder
    public PostResponse(Post entity) {
        this.postId = entity.getPostId();
        this.user = UserPostResponse.builder()
                .user(entity.getUser())
                .build();
        this.title = entity.getTitle();
        this.context = entity.getContext();
        this.viewCnt = entity.getViewCnt();
        this.greatCnt = entity.getGreatCnt();
        this.createdAt = entity.getCreatedAt();
    }
}
