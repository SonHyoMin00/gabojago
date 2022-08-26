package com.hanium.gabojago.service;

import com.hanium.gabojago.domain.Bookmark;
import com.hanium.gabojago.domain.Spot;
import com.hanium.gabojago.domain.User;
import com.hanium.gabojago.dto.bookmark.BookmarkSaveRequest;
import com.hanium.gabojago.dto.bookmark.SpotBookmarkPageResponse;
import com.hanium.gabojago.dto.bookmark.SpotBookmarkResponse;
import com.hanium.gabojago.dto.spot.SpotMapResponse;
import com.hanium.gabojago.dto.spot.SpotPageResponse;
import com.hanium.gabojago.dto.spot.SpotResponse;
import com.hanium.gabojago.repository.BookmarkRepository;
import com.hanium.gabojago.repository.SpotRepository;
import com.hanium.gabojago.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class SpotService {
    private final SpotRepository spotRepository;
    private final BookmarkRepository bookmarkRepository;

    //Page<Spot>을 SpotPageResponse(dto)로 바꾸는 함수
    private SpotPageResponse convertSpotsToSpotPageResponse(Page<Spot> spots) {
        //총 페이지 수
        int totalPages = spots.getTotalPages();
        log.info("총 페이지 수: " + spots.getTotalPages());

        //spotResponses DTO로 변환
        List<SpotResponse> spotResponses = spots.getContent()
                .stream().map(SpotResponse::new).collect(Collectors.toList());

        // 총 페이지 수 추가하여 반환
        return SpotPageResponse.builder()
                .spotResponses(spotResponses)
                .totalPages(totalPages)
                .build();
    }

    // 실시간 핫플레이스 데이터 가져오기
    public List<SpotResponse> getRealtimeHotplaces() {
        List<Spot> spots = spotRepository.findTop10ByOrderByViewCntDesc();
        return spots.stream().map(SpotResponse::new).collect(Collectors.toList());
    }

    // 조회순으로 정렬된 핫플레이스 데이터 가져오기
    public  SpotPageResponse findHotplacesByViewCnt(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Spot> spots = spotRepository.findAllByOrderByViewCntDescSpotIdAsc(pageable);
        return convertSpotsToSpotPageResponse(spots);
    }

    // 지역별 핫플레이스 데이터 가져오기
    public SpotPageResponse findHotplacesByRegion(String region, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Spot> spots = spotRepository.findHotplacesByRegion(region, pageable);
        return convertSpotsToSpotPageResponse(spots);
    }

    // 태그별 핫플레이스 데이터 가져오기
    public SpotPageResponse findHotplacesByTag(int tagId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Spot> spots = spotRepository.findHotplaceByTag(tagId, pageable);
        return convertSpotsToSpotPageResponse(spots);
    }

    // 상세 핫플레이스 데이터 가져오기
    @Transactional
    public SpotMapResponse findHotplaceBySpotId(Long spotId){
        Spot spot = spotRepository.findBySpotId(spotId).orElseThrow(() -> new IllegalArgumentException("해당 아이디의 핫플레이스가 없습니다."));
        spot.addViewCnt();

        Spot spots = spotRepository.findAllBySpotId(spotId);

        //북마크 수
        Long bookmarkCnt = bookmarkRepository.countBySpot(spots);
        log.info("북마크 수: " + bookmarkRepository.countBySpot(spots));

        return new SpotMapResponse(spots, bookmarkCnt);
    }

    // 사용자 위치기반 데이터 가져오기
    public List<SpotMapResponse> findLocationBySpotXAndSpotY(BigDecimal xStart, BigDecimal xEnd, BigDecimal yStart, BigDecimal yEnd){
        List<Spot> spots = spotRepository.findAllBySpotXBetweenAndSpotYBetween(xStart, xEnd, yStart, yEnd);
        List<SpotMapResponse> res = new ArrayList<>();
        for(Spot spot : spots){
            //북마크 수
            Long bookmarkCnt = bookmarkRepository.countBySpot(spot);
            log.info("북마크 수: " + bookmarkRepository.countBySpot(spot));

           res.add(new SpotMapResponse(spot, bookmarkCnt));
        }
        return res;
    }

    //Page<Spot>을 SpotBookmarkPageResponse(dto)로 바꾸는 함수
    private SpotBookmarkPageResponse convertSpotsToSpotBookmarkPageResponse(Page<Spot> spots) {
        //총 페이지 수
        int totalPages = spots.getTotalPages();
        log.info("총 페이지 수: " + spots.getTotalPages());

        //spotBookmarkResponses로 변환
        List<SpotBookmarkResponse> res = new ArrayList<>();
        for(Spot spot : spots){
            //북마크 수
            Long bookmarkCnt = bookmarkRepository.countBySpot(spot);
            log.info("북마크 수: " + bookmarkRepository.countBySpot(spot));

            res.add(new SpotBookmarkResponse(spot, bookmarkCnt));
        }

        // 총 페이지 수 추가하여 반환
        return SpotBookmarkPageResponse.builder()
                .spotBookmarkResponses(res)
                .totalPages(totalPages)
                .build();
    }

    // 북마크 순 데이터 가져오기
    public SpotBookmarkPageResponse findHotplacesByBookmark(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Spot> spots = spotRepository.findAllByBookmarkGroupBySpotId(pageable);
        return convertSpotsToSpotBookmarkPageResponse(spots);
    }

    // 북마크 추가하기
    @Transactional
    public Long saveBookmark(BookmarkSaveRequest bookmarkSaveRequest, User user){
        Long userId = user.getUserId();

        Long spotId = bookmarkSaveRequest.getSpotId();
        Spot spot = spotRepository.findBySpotId(spotId).orElseThrow(() -> new IllegalArgumentException("해당 아이디의 핫플레이스가 없습니다."));

        List<Bookmark> bookmarkSpotUser = bookmarkRepository.findBySpot_SpotIdAndUser_UserId(spotId,userId);
        if (!bookmarkSpotUser.isEmpty()) {
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
        Spot spot = spotRepository.findBySpotId(spotId).orElseThrow(() -> new IllegalArgumentException("해당 아이디의 핫플레이스가 없습니다."));

        Bookmark bookmark = bookmarkRepository.findBySpotAndUser(spot, user)
                .orElseThrow(() -> new IllegalArgumentException("해당 핫플레이스의 북마크가 없습니다."));

        bookmarkRepository.delete(bookmark);

        return spotId;
    }

    // 사용자 북마크 조회(마이페이지)
    public SpotBookmarkPageResponse getUserBookmarks(User user, int page, int size) {
        //1. 북마크 테이블에서 사용자가 북마크한 핫플 찾기
        List<Bookmark> bookmark = bookmarkRepository.findByUser(user);

        //2. 핫플들 정보 페이지마다 보내주기
        Pageable pageable = PageRequest.of(page, size);
        Page<Spot> spots = spotRepository.findAllByBookmarksIn(bookmark, pageable);
        return convertSpotsToSpotBookmarkPageResponse(spots);
    }
}
