package com.hanium.gabojago.repository;

import com.hanium.gabojago.domain.Spot;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface SpotRepository extends JpaRepository<Spot, Long> {
    List<Spot> findTop10ByOrderByViewCntDesc();

    Page<Spot> findAllByOrderByViewCntDescSpotIdAsc(Pageable pageable);

    @Query(value = "select s from Spot s where s.region=:region order by s.viewCnt desc, s.spotId")
    Page<Spot> findHotplacesByRegion(String region, Pageable pageable);

    @Query(value = "select s from Spot s inner join s.spotTags st on st.tag.tagId=:tagId",
            countQuery = "select count(st) from SpotTag st where st.tag.tagId=:tagId")
    Page<Spot> findHotplaceByTag(@Param(value = "tagId") int tagId, Pageable pageable);

    List<Spot> findAllBySpotId(Long spotId);

    List<Spot> findAllBySpotXBetweenAndSpotYBetween(BigDecimal xStart, BigDecimal xEnd, BigDecimal yStart, BigDecimal yEnd);

    @Query(value = "select s from Spot s order by size(s.bookmarks) desc")
    Page<Spot> findAllByBookmarkGroupBySpotId(Pageable pageable);

    Optional<Spot> findBySpotId(Long spotId);
}
