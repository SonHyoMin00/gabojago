package com.hanium.gabojago.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class UserTagStatistic {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userTagId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "tag_id")
    private Tag tag;

    @Column
    private Integer tagCnt = 0;

    @Builder
    public UserTagStatistic(User user, Tag tag) {
        this.user = user;
        this.tag = tag;
    }

    public void increaseTagViewCnt() {
        this.tagCnt++;
    }
}
