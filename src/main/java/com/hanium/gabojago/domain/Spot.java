package com.hanium.gabojago.domain;

import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Getter
@Entity
public class Spot {
    @Id
    Long spotId;

    @Column
    String spotName;

    @Column
    String address;

    @Column
    String region;

    @Column
    String detail;

    @Column
    String tel;

    @Column
    String spotImage;

    @Column
    Integer viewCnt;
}
