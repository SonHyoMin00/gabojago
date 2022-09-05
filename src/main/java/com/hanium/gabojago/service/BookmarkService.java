package com.hanium.gabojago.service;

import com.hanium.gabojago.domain.Bookmark;
import com.hanium.gabojago.domain.Spot;
import com.hanium.gabojago.domain.User;
import com.hanium.gabojago.dto.bookmark.BookmarkSaveRequest;
import com.hanium.gabojago.repository.BookmarkRepository;
import com.hanium.gabojago.repository.SpotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class BookmarkService {
    private final SpotRepository spotRepository;
    private final BookmarkRepository bookmarkRepository;

    // 북마크 추가하기
    @Transactional
    public Long saveBookmark(BookmarkSaveRequest bookmarkSaveRequest, User user){
        Long spotId = bookmarkSaveRequest.getSpotId();
        Spot spot = spotRepository.findById(spotId)
                .orElseThrow(() -> new IllegalArgumentException("해당 아이디의 핫플레이스가 없습니다."));

        Optional<Bookmark> bookmarkSpotUser = bookmarkRepository.findBySpotAndUser(spot,user);
        if (bookmarkSpotUser.isPresent()) {
            throw new IllegalArgumentException("중복된 북마크입니다");
        }

        Bookmark bookmark = Bookmark.builder()
                .spot(spot)
                .user(user)
                .build();

        return bookmarkRepository.save(bookmark).getBookmarkId();
    }

    // 북마크 삭제하기
    public Long deleteBookmark(Long spotId, User user){
        Spot spot = spotRepository.findById(spotId)
                .orElseThrow(() -> new IllegalArgumentException("해당 아이디의 핫플레이스가 없습니다."));

        Bookmark bookmark = bookmarkRepository.findBySpotAndUser(spot, user)
                .orElseThrow(() -> new IllegalArgumentException("해당 핫플레이스의 북마크가 없습니다."));

        bookmarkRepository.delete(bookmark);

        return spotId;
    }
}
