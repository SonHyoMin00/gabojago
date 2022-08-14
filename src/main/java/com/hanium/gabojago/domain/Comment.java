package com.hanium.gabojago.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Comment {
    @Id
    private Long commentId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    @Column
    private String context;

    @Builder
    public Comment(User user, Post post, String context) {
        this.user = user;
        this.post = post;
        this.context = context;
    }

    public void updateContext(String context) {
        this.context = context;
    }
}
