package com.hanium.gabojago.domain;

import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Getter
@Entity
public class Post {
    @Id
    private Long postId;

    @Column
    private String title;
}
