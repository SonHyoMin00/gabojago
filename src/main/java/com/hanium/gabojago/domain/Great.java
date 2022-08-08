package com.hanium.gabojago.domain;

import javax.persistence.*;

@Entity
public class Great {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long greatId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Post post;
}
