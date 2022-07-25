package com.hanium.gabojago.repository;

import com.hanium.gabojago.domain.Spot;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SpotRepository extends JpaRepository<Spot, Long> {
    List<Spot> findTop10ByOrderByViewCntDesc();

    List<Spot> findTop20ByRegion(String region);

    List<Spot> findAllBySpotId(Long spotId);
}
