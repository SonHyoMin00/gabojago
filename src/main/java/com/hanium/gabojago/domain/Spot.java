package com.hanium.gabojago.domain;

import com.hanium.gabojago.dto.post.PostResponse;
import lombok.Getter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Entity
public class Spot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long spotId;

    private String spotName;

    private String address;

    private String region;

    private String detail;

    private String tel;

    private String spotImage;

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

    public List<PostResponse> getRecent3Posts(List<Post> posts) {
        return posts.stream().map(PostResponse::new).collect(Collectors.toList());
    }

}
