package com.hanium.gabojago.service;

import com.hanium.gabojago.domain.*;
import com.hanium.gabojago.dto.bookmark.SpotBookmarkPageResponse;
import com.hanium.gabojago.dto.bookmark.SpotBookmarkResponse;
import com.hanium.gabojago.dto.spot.SpotMapResponse;
import com.hanium.gabojago.dto.spot.SpotPageResponse;
import com.hanium.gabojago.dto.spot.SpotResponse;
import com.hanium.gabojago.repository.BookmarkRepository;
import com.hanium.gabojago.repository.SpotAgeStatisticRepository;
import com.hanium.gabojago.repository.SpotRepository;
import com.hanium.gabojago.repository.UserTagStatisticRepository;
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
    private final SpotAgeStatisticRepository spotAgeStatisticRepository;
    private final UserTagStatisticRepository userTagStatisticRepository;

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
        Page<Spot> spots = spotRepository.findAllByRegionOrderByViewCntDescSpotIdAsc(region, pageable);
        return convertSpotsToSpotPageResponse(spots);
    }

    // 태그별 핫플레이스 데이터 가져오기
    public SpotPageResponse findHotplacesByTag(int tagId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Spot> spots = spotRepository.findHotplacesByTag(tagId, pageable);
        return convertSpotsToSpotPageResponse(spots);
    }

    // 상세 핫플레이스 데이터 가져오기
    @Transactional
    public SpotMapResponse findHotplaceBySpotId(Long spotId, User user){
        Spot spot = spotRepository.findById(spotId)
                .orElseThrow(() -> new IllegalArgumentException("해당 아이디의 핫플레이스가 없습니다."));
        spot.addViewCnt();

        //북마크 수
        Long bookmarkCnt = bookmarkRepository.countBySpot(spot);
        log.info("북마크 수: " + bookmarkCnt);

        if (user != null) {
            // 연령대별 조회수 증가
            byte age = user.getAge();
            SpotAgeStatistic spotAgeStatistic = spotAgeStatisticRepository.findById(spotId)
                    .orElseGet(() -> saveDefaultSpotAgeStatistic(spot));
            spotAgeStatistic.increaseAgeViewCnt(age);

            // 핫플레이스에 등록된 사용자의 태그별 조회수 증가
            List<SpotTag> spotTags = spot.getSpotTags();
            List<UserTagStatistic> userTagStatistics = userTagStatisticRepository.findAllByUser(user);
            for (SpotTag spotTag : spotTags) {
                boolean flag = false;
                Tag tag = spotTag.getTag();

                for (UserTagStatistic userTagStatistic : userTagStatistics) {
                    if(userTagStatistic.getTag() == tag) {
                        flag = true;
                        userTagStatistic.increaseTagViewCnt();
                        break;
                    }
                }
                if (!flag) {
                    UserTagStatistic userTagStatistic = saveDefaultUserTagStatistic(user, tag);
                    userTagStatistic.increaseTagViewCnt();
                }
            }
        }

        return new SpotMapResponse(spot, bookmarkCnt);
    }

    // 사용자 위치기반 데이터 가져오기
    public List<SpotMapResponse> findLocationBySpotXAndSpotY(BigDecimal xStart, BigDecimal xEnd,
                                                             BigDecimal yStart, BigDecimal yEnd){
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

    // 북마크 순 데이터 가져오기
    public SpotBookmarkPageResponse findHotplacesByBookmark(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Spot> spots = spotRepository.findHotplacesByBookmarkCnt(pageable);
        return convertSpotsToSpotBookmarkPageResponse(spots);
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

    private SpotAgeStatistic saveDefaultSpotAgeStatistic(Spot spot) {
        SpotAgeStatistic spotAgeStatistic = SpotAgeStatistic.builder()
                .spot(spot)
                .build();
        return spotAgeStatisticRepository.save(spotAgeStatistic);
    }

    private UserTagStatistic saveDefaultUserTagStatistic(User user, Tag tag) {
        UserTagStatistic userTagStatistic = UserTagStatistic.builder()
                .user(user)
                .tag(tag)
                .build();
        return userTagStatisticRepository.save(userTagStatistic);
    }

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
}
