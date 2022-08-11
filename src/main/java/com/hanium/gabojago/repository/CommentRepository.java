package com.hanium.gabojago.repository;

import com.hanium.gabojago.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    @Query("select c from Comment c join fetch c.user")
    Optional<Comment> findByIdWithUser(Long id);
}
