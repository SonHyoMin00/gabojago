package com.hanium.gabojago.repository;

import com.hanium.gabojago.domain.Comment;
import com.hanium.gabojago.domain.Post;
import com.hanium.gabojago.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    @Query("select c from Comment c join fetch c.user where c.commentId=:id")
    Optional<Comment> findByIdWithUser(Long id);

    @Query(
            value = "select c from Comment c join fetch c.user where c.post=:post",
        countQuery = "select count(c) from Comment c where c.post=:post")
    Page<Comment> findAllByPostWithUser(Post post, Pageable pageable);

    Long countByUser(User user);
}
