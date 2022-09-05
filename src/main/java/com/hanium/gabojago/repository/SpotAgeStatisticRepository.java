package com.hanium.gabojago.repository;

import com.hanium.gabojago.domain.SpotAgeStatistic;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SpotAgeStatisticRepository extends JpaRepository<SpotAgeStatistic, Long> {
    List<SpotAgeStatistic> findTop10ByOrderByAge10Desc();
    List<SpotAgeStatistic> findTop10ByOrderByAge20Desc();
    List<SpotAgeStatistic> findTop10ByOrderByAge30Desc();
    List<SpotAgeStatistic> findTop10ByOrderByAge40Desc();
    List<SpotAgeStatistic> findTop10ByOrderByAge50Desc();
}
