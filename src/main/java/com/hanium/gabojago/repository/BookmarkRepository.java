package com.hanium.gabojago.repository;

import com.hanium.gabojago.domain.Bookmark;
import com.hanium.gabojago.domain.Spot;
import com.hanium.gabojago.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {
     Long countBySpot(Spot spot);

     List<Bookmark> findBySpot_SpotIdAndUser_UserId(Long spotId, Long userId);

     Optional<Bookmark> findBySpotAndUser(Spot spot, User user);

     List<Bookmark> findByUser(User user);

     Optional<Bookmark> findByBookmarkId(Long bookmarkId);

     Long countByUser(User user);
}
