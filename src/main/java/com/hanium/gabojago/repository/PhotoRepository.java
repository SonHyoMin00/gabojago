package com.hanium.gabojago.repository;

import com.hanium.gabojago.domain.Photo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PhotoRepository extends JpaRepository<Photo, Long> {
}
