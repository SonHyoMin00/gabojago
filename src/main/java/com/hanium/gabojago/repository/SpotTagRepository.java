package com.hanium.gabojago.repository;

import com.hanium.gabojago.domain.SpotTag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SpotTagRepository extends JpaRepository<SpotTag, Long> {
    @Query(value = "select s from SpotTag s join fetch s.spot where s.tag.tagId=:tagId",
    countQuery = "select count(s) from SpotTag s")
    Page<SpotTag> findHotplaceByTag(@Param(value = "tagId") int tagId, Pageable pageable);
}
