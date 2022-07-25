package com.hanium.gabojago.repository;

import com.hanium.gabojago.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
