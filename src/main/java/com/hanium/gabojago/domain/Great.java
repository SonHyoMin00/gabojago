package com.hanium.gabojago.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Great {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long greatId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    @Builder
    public Great(User user, Post post) {
        this.user = user;
        this.post = post;
    }
}
