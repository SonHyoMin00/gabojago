package com.hanium.gabojago.repository;

import com.hanium.gabojago.domain.SpotTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SpotTagRepository extends JpaRepository<SpotTag, Long> {
    @Query("select s from SpotTag s join fetch s.spot where s.tag.tagId=:tagId")
    List<SpotTag> findHotplaceByTag(@Param(value = "tagId") int tagId);
}
