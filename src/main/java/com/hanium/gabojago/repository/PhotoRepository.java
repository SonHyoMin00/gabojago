package com.hanium.gabojago.repository;

import com.hanium.gabojago.domain.Photo;
import com.hanium.gabojago.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PhotoRepository extends JpaRepository<Photo, Long> {
    List<Photo> findAllByPost(Post post);
}
