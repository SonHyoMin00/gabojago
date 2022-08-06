package com.hanium.gabojago.domain;

import com.hanium.gabojago.dto.post.PostCreateRequest;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Post extends BaseTimeEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column
    private String title;

    @Column
    private String context;

    @Column
    private int viewCnt;

    @Column
    private int greatCnt;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<PostTag> postTags = new ArrayList<>();

    @Builder
    public Post(User user, String title, String context) {
        this.user = user;
        this.title = title;
        this.context = context;
    }

    public String updatePost(PostCreateRequest postCreateRequest, List<PostTag> newPostTags) {
        this.title = postCreateRequest.getTitle();
        this.context = postCreateRequest.getContext();
        this.postTags = newPostTags;
        return "Success";
    }
}
