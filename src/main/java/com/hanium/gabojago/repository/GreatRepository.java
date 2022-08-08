package com.hanium.gabojago.repository;

import com.hanium.gabojago.domain.Great;
import com.hanium.gabojago.domain.Post;
import com.hanium.gabojago.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GreatRepository extends JpaRepository<Great, Long> {
    Optional<Great> findByUserAndPost(User user, Post post);
}
