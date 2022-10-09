package com.hanium.gabojago.service;

import com.hanium.gabojago.domain.*;
import com.hanium.gabojago.dto.bookmark.SpotBookmarkPageResponse;
import com.hanium.gabojago.dto.bookmark.SpotBookmarkResponse;
import com.hanium.gabojago.dto.spot.SpotDetailResponse;
import com.hanium.gabojago.dto.spot.SpotMapResponse;
import com.hanium.gabojago.dto.spot.SpotPageResponse;
import com.hanium.gabojago.dto.spot.SpotResponse;
import com.hanium.gabojago.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class SpotService {
    private final SpotRepository spotRepository;
    private final PostRepository postRepository;
    private final BookmarkRepository bookmarkRepository;
    private final SpotTagRepository spotTagRepository;
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
    public SpotDetailResponse findHotplaceBySpotId(Long spotId, User user){
        Spot spot = spotRepository.findById(spotId)
                .orElseThrow(() -> new IllegalArgumentException("해당 아이디의 핫플레이스가 없습니다."));
        spot.addViewCnt();

        // 관련된 최신 post 3개
        List<Post> posts = postRepository.findTop3BySpotOrderByCreatedAtDesc(spot);

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
            List<SpotTag> spotTags = spotTagRepository.findAllBySpotWithTag(spot);
            Set<Tag> tagsLinkedWithSpot = new HashSet<>();
            for (SpotTag spotTag : spotTags) tagsLinkedWithSpot.add(spotTag.getTag());

            // 다른 방법으로 구현 고민 필요
            List<UserTagStatistic> userTagStatistics = userTagStatisticRepository.findAllByUser(user);
            for (Tag tag : tagsLinkedWithSpot) {
                boolean flag = false;

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

        return SpotDetailResponse.builder()
                .entity(spot)
                .posts(posts)
                .bookmarkCnt(bookmarkCnt)
                .build();
    }

    // 사용자 위치기반 데이터 가져오기
    public List<SpotMapResponse> findLocationBySpotXAndSpotY(BigDecimal xStart, BigDecimal xEnd,
                                                             BigDecimal yStart, BigDecimal yEnd){
        List<Spot> spots = spotRepository.findAllBySpotXBetweenAndSpotYBetween(xStart, xEnd, yStart, yEnd);
        List<SpotMapResponse> res = new ArrayList<>();
        for(Spot spot : spots){
            //북마크 수
            Long bookmarkCnt = bookmarkRepository.countBySpot(spot);
            log.info("북마크 수: " + bookmarkCnt);

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

    // 사용자 추천 핫플레이스 데이터 가져오기
    public List<SpotResponse> getRecommendedHotplaces(User user) {
        List<Spot> spots;
        byte age = user.getAge();
        List<SpotAgeStatistic> spotAgeStatistics;
        if(age == 10) spotAgeStatistics = spotAgeStatisticRepository.findTop10ByOrderByAge10Desc();
        else if(age == 20) spotAgeStatistics = spotAgeStatisticRepository.findTop10ByOrderByAge20Desc();
        else if(age == 30) spotAgeStatistics = spotAgeStatisticRepository.findTop10ByOrderByAge30Desc();
        else if(age == 40) spotAgeStatistics = spotAgeStatisticRepository.findTop10ByOrderByAge40Desc();
        else spotAgeStatistics = spotAgeStatisticRepository.findTop10ByOrderByAge50Desc();

        // spot_id로 핫플레이스 조회
        spots = spotRepository.findAllById(spotAgeStatistics.stream()
                .map(SpotAgeStatistic::getSpotId).collect(Collectors.toList()));

        // 사용자가 가장 자주 조회한 태그 2개 조회
        List<UserTagStatistic> userTagStatistics = userTagStatisticRepository.findTop2ByUserOrderByTagCntDesc(user);

        // 태그당 각각 10개씩 조회
        for (UserTagStatistic userTagStatistic : userTagStatistics) {
            spots.addAll(spotRepository.findAllByTagOrderByRandom(
                    userTagStatistic.getTag().getTagId()));
        }

        // 핫플레이스 중복 제거 후 랜덤으로 10개 추출하여 리턴
        spots = spots.stream().distinct().collect(Collectors.toList());
        Collections.shuffle(spots);
        List<Spot> res = new ArrayList<>();
        for (int i = 0; i < 10; i++) res.add(spots.get(i));
        return res.stream().map(SpotResponse::new).collect(Collectors.toList());
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
