package com.hanium.gabojago.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;

@Slf4j
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class SpotAgeStatistic {
    @Id
    private Long spotId;

    @MapsId
    @OneToOne
    @JoinColumn(name = "spot_id")
    private Spot spot;

    @Column(name = "age_10")
    private Integer age10 = 0;

    @Column(name = "age_20")
    private Integer age20 = 0;

    @Column(name = "age_30")
    private Integer age30 = 0;

    @Column(name = "age_40")
    private Integer age40 = 0;

    @Column(name = "age_50")
    private Integer age50 = 0;

    @Builder
    public SpotAgeStatistic(Spot spot) {
        this.spot = spot;
    }

    public void increaseAgeViewCnt(byte age) {
        if(age == 10) this.age10++;
        else if(age == 20) this.age20++;
        else if(age == 30) this.age30++;
        else if(age == 40) this.age40++;
        else if(age == 50) this.age50++;
        else log.error("유효하지 않은 사용자 연령대입니다: " + age);
    }
}
