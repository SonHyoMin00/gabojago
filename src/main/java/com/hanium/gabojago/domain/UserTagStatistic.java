package com.hanium.gabojago.domain;

import lombok.Getter;

import javax.persistence.*;

@Getter
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
    private Integer tagCnt;
}
