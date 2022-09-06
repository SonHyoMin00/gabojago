package com.hanium.gabojago.repository;

import com.hanium.gabojago.domain.Bookmark;
import com.hanium.gabojago.domain.Spot;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface SpotRepository extends JpaRepository<Spot, Long> {
    List<Spot> findTop10ByOrderByViewCntDesc();

    Page<Spot> findAllByOrderByViewCntDescSpotIdAsc(Pageable pageable);

    Page<Spot> findAllByRegionOrderByViewCntDescSpotIdAsc(String region, Pageable pageable);

    @Query(value = "select s from Spot s inner join s.spotTags st on st.tag.tagId=:tagId",
            countQuery = "select count(st) from SpotTag st where st.tag.tagId=:tagId")
    Page<Spot> findHotplacesByTag(@Param(value = "tagId") int tagId, Pageable pageable);

    List<Spot> findAllBySpotXBetweenAndSpotYBetween(BigDecimal xStart, BigDecimal xEnd,
                                                    BigDecimal yStart, BigDecimal yEnd);

    @Query(value = "select s from Spot s order by size(s.bookmarks) desc")
    Page<Spot> findHotplacesByBookmarkCnt(Pageable pageable);

    Page<Spot> findAllByBookmarksIn(List<Bookmark> bookmark, Pageable pageable);

    @Query(value = "select distinct s.* from spot s inner join spot_tag st " +
            "on s.spot_id=st.spot_id and st.tag_id=:tagId " +
            "order by RAND() limit 10",nativeQuery = true)
    List<Spot> findAllByTagOrderByRandom(int tagId);

}
