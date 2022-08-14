package com.hanium.gabojago.service;

import com.hanium.gabojago.domain.Bookmark;
import com.hanium.gabojago.domain.Spot;
import com.hanium.gabojago.domain.User;
import com.hanium.gabojago.dto.*;
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
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class SpotService {
    private final SpotRepository spotRepository;
    private final UserRepository userRepository;
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
    public List<SpotMapResponse> findHotplaceBySpotId(Long spotId){
        Spot spot = spotRepository.findBySpotId(spotId).orElseThrow(() -> new IllegalArgumentException("해당 아이디의 사용자가 없습니다."));
        spot.addViewCnt();

        List<Spot> spots = spotRepository.findAllBySpotId(spotId);
        return spots.stream().map(SpotMapResponse::new).collect(Collectors.toList());
    }

    // 사용자 위치기반 데이터 가져오기
    public List<SpotMapResponse> findLocationBySpotXAndSpotY(BigDecimal xStart, BigDecimal xEnd, BigDecimal yStart, BigDecimal yEnd){
        List<Spot> spots = spotRepository.findAllBySpotXBetweenAndSpotYBetween(xStart, xEnd, yStart, yEnd);
        return spots.stream().map(SpotMapResponse::new).collect(Collectors.toList());
    }

    // 북마크 정보 가져오기
    public List<SpotBookmarkResponse> findBookmarkBySpotId(Long spotId){
        List<Spot> spots = spotRepository.findAllBySpotId(spotId);
        return spots.stream().map(SpotBookmarkResponse::new).collect(Collectors.toList());
    }

    //Page<Spot>을 SpotBookmarkPageResponse(dto)로 바꾸는 함수
    private SpotBookmarkPageResponse convertSpotsToSpotBookmarkPageResponse(Page<Spot> spots) {
        //총 페이지 수
        int totalPages = spots.getTotalPages();
        log.info("총 페이지 수: " + spots.getTotalPages());

        //spotBookmarkResponses DTO로 변환
        List<SpotBookmarkResponse> spotBookmarkResponses = spots.getContent()
                .stream().map(SpotBookmarkResponse::new).collect(Collectors.toList());

        // 총 페이지 수 추가하여 반환
        return SpotBookmarkPageResponse.builder()
                .spotBookmarkResponses(spotBookmarkResponses)
                .totalPages(totalPages)
                .build();
    }

    // 북마크 순 데이터 가져오기
    public  SpotBookmarkPageResponse findBookmarkGroupBySpotId(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Spot> spots = spotRepository.findAllByBookmarkGroupBySpotId(pageable);
        return convertSpotsToSpotBookmarkPageResponse(spots);
    }

    @Transactional
    public Long saveBookmark(BookmarkSaveRequest bookmarkSaveRequest){
        String email = bookmarkSaveRequest.getEmail();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("해당 이메일의 사용자가 없습니다."));

        Long spotId = bookmarkSaveRequest.getSpotId();
        Spot spot = spotRepository.findBySpotId(spotId).orElseThrow(() -> new IllegalArgumentException("해당 아이디의 사용자가 없습니다."));

        Bookmark bookmark = Bookmark.builder()
                .spot(spot)
                .user(user)
                .build();

        return bookmarkRepository.save(bookmark).getBookmarkId();
    }

    public Long deleteBookmark(Long id, String email){
        Bookmark bookmark = bookmarkRepository.findByBookmarkId(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 아이디의 사용자가 없습니다."));

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("해당 이메일의 사용자가 없습니다."));
        if (!email.equals(user.getEmail())) {
            throw new IllegalArgumentException("해당 이메일의 사용자가 없습니다.");
        }
        bookmarkRepository.delete(bookmark);

        return id;
    }
}
