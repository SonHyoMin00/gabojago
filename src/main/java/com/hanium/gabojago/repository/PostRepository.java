package com.hanium.gabojago.repository;

import com.hanium.gabojago.domain.Post;
import com.hanium.gabojago.domain.User;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {
    @Query(
            value = "select p from Post p join fetch p.user",
            countQuery = "select count(p) from Post p")
    Page<Post> findAllSpotsByPage(Pageable pageable);

    //가독성이 매우 나쁘다.....
    List<Post> findTop3ByCreatedAtBetweenOrderByGreatCntDescViewCntDescCreatedAtAsc(LocalDateTime start, LocalDateTime end);

    @Query(value = "select p from Post p join fetch p.user where p.postId=:postId")
    Optional<Post> findByIdWithUser(Long postId);

    @Query(
            value = "select p from Post p join fetch p.user where p.user=:user",
        countQuery = "select count(p) from Post p where p.user=:user")
    Page<Post> findAllByUser(User user, Pageable pageable);
}
