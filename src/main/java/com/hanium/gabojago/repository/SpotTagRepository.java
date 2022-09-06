package com.hanium.gabojago.repository;

import com.hanium.gabojago.domain.Spot;
import com.hanium.gabojago.domain.SpotTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SpotTagRepository extends JpaRepository<SpotTag, Long> {
    @Query("select st from SpotTag st join fetch st.tag where st.spot=:spot")
    List<SpotTag> findAllBySpotWithTag(Spot spot);
}
