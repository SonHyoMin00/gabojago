package com.hanium.gabojago.repository;

import com.hanium.gabojago.domain.Bookmark;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {
    Bookmark findByBookmarkId(Long bookmarkId);
}
