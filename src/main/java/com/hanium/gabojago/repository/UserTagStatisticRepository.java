package com.hanium.gabojago.repository;

import com.hanium.gabojago.domain.Tag;
import com.hanium.gabojago.domain.User;
import com.hanium.gabojago.domain.UserTagStatistic;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserTagStatisticRepository extends JpaRepository<UserTagStatistic, Long> {
    Optional<UserTagStatistic> findByUserAndTag(User user, Tag tag);

    List<UserTagStatistic> findAllByUser(User user);
}
