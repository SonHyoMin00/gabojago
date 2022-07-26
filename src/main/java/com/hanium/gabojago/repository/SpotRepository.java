package com.hanium.gabojago.repository;

import com.hanium.gabojago.domain.Spot;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

import com.hanium.gabojago.domain.SpotTag;
import com.hanium.gabojago.domain.Tag;
import org.springframework.data.repository.query.Param;

public interface SpotRepository extends JpaRepository<Spot, Long> {
    List<Spot> findTop10ByOrderByViewCntDesc();

    List<Spot> findTop20ByRegion(String region);

    List<Spot> findAllBySpotId(Long spotId);

}
