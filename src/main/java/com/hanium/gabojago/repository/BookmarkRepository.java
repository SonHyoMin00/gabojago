package com.hanium.gabojago.repository;

import com.hanium.gabojago.domain.Bookmark;
import com.hanium.gabojago.domain.Spot;
import com.hanium.gabojago.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {
     Optional<Bookmark> findByBookmarkId(Long bookmarkId);

     Long countBySpot(Spot spot);

     List<Bookmark> findByUser_UserId(Long userId);
     List<Bookmark> findBySpot_SpotId(Long spotId);

     Optional<Bookmark> findBySpotAndUser(Spot spot, User user);
}
