package com.hanium.gabojago.repository;

import com.hanium.gabojago.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
}
