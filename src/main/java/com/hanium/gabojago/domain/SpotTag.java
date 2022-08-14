package com.hanium.gabojago.domain;

import lombok.Getter;

import javax.persistence.*;

@Getter
@Entity
public class SpotTag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long spotTagId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "spot_id")
    private Spot spot;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tag_id")
    private Tag tag;

}
