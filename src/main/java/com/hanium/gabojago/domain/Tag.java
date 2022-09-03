package com.hanium.gabojago.domain;

import lombok.Getter;

import javax.persistence.*;

@Getter
@Entity
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer tagId;

    private String value;
}
