package com.hanium.gabojago.repository;

import com.hanium.gabojago.domain.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {
    @Query(
            value = "select p from Post p join fetch p.user",
            countQuery = "select count(p) from Post p")
    Page<Post> findAllSpotsByPage(Pageable pageable);

    @Query(value = "select p from Post p join fetch p.user where p.postId=:postId")
    Optional<Post> findByIdWithPostTag(Long postId);
}
