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

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<Photo> photos = new ArrayList<>();

    @Builder
    public Post(User user, String title, String context) {
        this.user = user;
        this.title = title;
        this.context = context;
    }

    public void updatePost(PostCreateRequest postCreateRequest, List<PostTag> newPostTags) {
        this.title = postCreateRequest.getTitle();
        this.context = postCreateRequest.getContext();
        this.postTags.addAll(newPostTags);
    }

    public void increaseViewCnt() {
        this.viewCnt++;
    }

    public void increaseGreatCnt() {
        this.greatCnt++;
    }

    public void decreaseGreatCnt() {
        if(this.greatCnt == 0) throw new IllegalStateException("Error: 좋아요 수는 음수가 될 수 없습니다.");
        this.greatCnt--;
    }

}
