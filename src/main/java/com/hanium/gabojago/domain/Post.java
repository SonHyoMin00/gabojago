package com.hanium.gabojago.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Post {
    @Id
    private Long postId;

    @Column
    private String title;
}
