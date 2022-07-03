package com.hanium.gabojago.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class User {
    @Id
    private Long userId;

    @Column
    private String email;

    @Column
    private String name;
}
