package com.hanium.gabojago.domain;

import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Getter
@Entity
public class Tag {
    @Id
    Integer tagId;

    @Column
    String value;
}
