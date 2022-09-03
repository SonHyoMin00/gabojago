package com.hanium.gabojago.domain;

import lombok.Getter;

import javax.persistence.*;

@Getter
@Entity
public class SpotAgeStatistic {
    @Id
    private Long spotId;

    @MapsId
    @OneToOne
    @JoinColumn(name = "spot_id")
    private Spot spot;

    @Column(name = "age_10")
    private Integer age10;

    @Column(name = "age_20")
    private Integer age20;

    @Column(name = "age_30")
    private Integer age30;

    @Column(name = "age_40")
    private Integer age40;

    @Column(name = "age_50")
    private Integer age50;
}
