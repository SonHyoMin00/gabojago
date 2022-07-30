package com.hanium.gabojago.repository;

import com.hanium.gabojago.domain.Spot;
import com.hanium.gabojago.domain.SpotTag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SpotRepository extends JpaRepository<Spot, Long> {
    List<Spot> findTop10ByOrderByViewCntDesc();

    @Query(value = "select distinct s.* from (select * from spot order by view_cnt) s", nativeQuery = true)
    List<Spot> findHotplacesByViewCnt(Pageable pageable);

//    @Query(value = "select distinct s from Spot s, SpotTag st, Tag t where s.region=:region and s.spotId=st.spot.spotId and st.tag.tagId=t.tagId")
//    @Query(value = "select s.* from (select * from spot order by view_cnt) s, spot_tag st, tag t where s.spot_id=st.spot_id and st.tag_id=t.tag_id", nativeQuery = true)
    @Query(value = "select distinct s.* from (select * from spot order by view_cnt) s where s.region=:region", nativeQuery = true)
    List<Spot> findHotplacesByRegion(String region, Pageable pageable);

    @Query(value = "select s from Spot s inner join s.spotTags st on st.tag.tagId=:tagId",
            countQuery = "select count(st) from SpotTag st where st.tag.tagId=:tagId")
    Page<Spot> findHotplaceByTag(@Param(value = "tagId") int tagId, Pageable pageable);

    List<Spot> findAllBySpotId(Long spotId);
}
