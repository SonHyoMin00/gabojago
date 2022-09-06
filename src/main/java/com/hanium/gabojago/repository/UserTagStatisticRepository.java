package com.hanium.gabojago.repository;

import com.hanium.gabojago.domain.User;
import com.hanium.gabojago.domain.UserTagStatistic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserTagStatisticRepository extends JpaRepository<UserTagStatistic, Long> {
    List<UserTagStatistic> findAllByUser(User user);

    List<UserTagStatistic> findTop2ByUserOrderByTagCntDesc(User user);
}
