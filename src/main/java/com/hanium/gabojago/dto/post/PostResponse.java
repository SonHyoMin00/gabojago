package com.hanium.gabojago.dto.post;

import com.hanium.gabojago.domain.Post;
import com.hanium.gabojago.dto.photo.FileResponse;
import com.hanium.gabojago.dto.user.UserPostResponse;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
@Getter
public class PostResponse {
    private Long postId;
    private UserPostResponse user;
    private String title;
    private String context;
    private List<PostTagResponse> postTags;
    private List<FileResponse> files;
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
        this.postTags = entity.getPostTags()
                .stream().map(PostTagResponse::new).collect(Collectors.toList());
        this.files = entity.getPhotos()
                .stream().map(FileResponse::new).collect(Collectors.toList());
        this.viewCnt = entity.getViewCnt();
        this.greatCnt = entity.getGreatCnt();
        this.createdAt = entity.getCreatedAt();
    }
}
