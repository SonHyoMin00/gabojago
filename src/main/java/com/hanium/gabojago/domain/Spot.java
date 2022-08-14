package com.hanium.gabojago.domain;

import lombok.Getter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
public class Spot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long spotId;

    @Column
    private String spotName;

    @Column
    private String address;

    @Column
    private String region;

    @Column
    private String detail;

    @Column
    private String tel;

    @Column
    private String spotImage;

    @Column
    private Integer viewCnt;

    @Column(name = "spot_x")
    private BigDecimal spotX;

    @Column(name = "spot_y")
    private BigDecimal spotY;

    @OneToMany(mappedBy = "spot", fetch = FetchType.LAZY)
    private List<SpotTag> spotTags = new ArrayList<>();

    @OneToMany(mappedBy = "spot", fetch = FetchType.LAZY)
    private List<Bookmark> bookmarks = new ArrayList<>();

    public void addViewCnt() {
        this.viewCnt++;
    }

}
