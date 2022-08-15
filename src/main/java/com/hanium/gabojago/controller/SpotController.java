package com.hanium.gabojago.controller;

import com.hanium.gabojago.dto.spot.SpotPageResponse;
import com.hanium.gabojago.dto.spot.SpotResponse;
import com.hanium.gabojago.dto.*;
import com.hanium.gabojago.service.SpotService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RequiredArgsConstructor
@RequestMapping("hotplaces")
@RestController
public class SpotController {
    private final SpotService spotService;

    // 실시간 순위 조회(top 10)
    @GetMapping("realtime")
    public List<SpotResponse> getRealtimeHotplaces() {
        return spotService.getRealtimeHotplaces();
    }

    // 조회순 조회
    @GetMapping
    public SpotPageResponse getHotplacesByViewCnt(
            @RequestParam(required = false, defaultValue = "1", value = "page")int page,
            @RequestParam(required = false, defaultValue = "10", value = "size")int size) {
        return spotService.findHotplacesByViewCnt(page - 1, size);
    }

    // 지역별 순위 조회
    @GetMapping("region/{region}")
    public SpotPageResponse getHotplacesByRegion(
            @PathVariable String region,
            @RequestParam(required = false, defaultValue = "1", value = "page")int page,
            @RequestParam(required = false, defaultValue = "10", value = "size")int size) {
        return spotService.findHotplacesByRegion(region, page - 1, size);
    }

    // 컨셉별 핫플레이스 조회
    @GetMapping("tag/{tagId}")
    public SpotPageResponse getHotplacesByTag(
            @PathVariable int tagId,
            @RequestParam(required = false, defaultValue = "1", value = "page")int page,
            @RequestParam(required = false, defaultValue = "10", value = "size")int size) {
        return spotService.findHotplacesByTag(tagId, page - 1, size);
    }

    // 상세정보 조회
    /*
    상세정보 조회는 select 걸과가 1건밖에 없음.
    List<SpotMapResponse>가 아니라 SpotMapResponse를 반환하도록 설계해야 함.
    밑에있는 상세정보 조회랑 똑같은 기능인 것 같음.
    큰 이유가 없다면 여기에 북마크 갯수를 추가하던가 밑에꺼를 북마크 갯수를 보여주도록 수정하던가 해야할 것 같음.
     */
    @GetMapping("id/{idx}")
    public List<SpotMapResponse> findHotplaceBySpotId(@PathVariable("idx") Long id ) {
        return spotService.findHotplaceBySpotId(id);
    }

    // 사용자 위치기반 조회 1
//    @GetMapping("location/{xStart}/{xEnd}/{yStart}/{yEnd}")
//    public List<SpotMapResponse> findLocationBySpotXAndSpotY(
//            @PathVariable("xStart")BigDecimal xStart,
//            @PathVariable("xEnd")BigDecimal xEnd,
//            @PathVariable("yStart")BigDecimal yStart,
//            @PathVariable("yEnd")BigDecimal yEnd) {
//        return spotService.findLocationBySpotXAndSpotY(xStart, xEnd, yStart, yEnd);
//    }

    // 사용자 위치기반 조회 2
    @GetMapping("location")
    public List<SpotMapResponse> findLocationBySpotXAndSpotY(
            @RequestParam(required = false, defaultValue = "37.55", value = "xStart")BigDecimal xStart,
            @RequestParam(required = false, defaultValue = "37.65", value = "xEnd")BigDecimal xEnd,
            @RequestParam(required = false, defaultValue = "126.96", value = "yStart")BigDecimal yStart,
            @RequestParam(required = false, defaultValue = "126.97", value = "yEnd")BigDecimal yEnd) {
        return spotService.findLocationBySpotXAndSpotY(xStart, xEnd, yStart, yEnd);
    }

    // 상세정보 조회
    /*
    물어보기: 위에있는 상세정보 조회에서 xSpot, ySpot을 뺀것만 따로 만든 이유가 궁금!
    특정 핫플레이스를 북마크한 모든 사용자들을 다 넘겨주도록 되어있는데,
    북마크한 사용자들의 개인정보가 프론트에 주어지면 안됨.
    특정 핫플레이스를 북마크한 사용자들의 "수"를 제공해야 함.
    마찬가지로, 상세정보 조회는 select 걸과가 1건밖에 없음.
    List<SpotBookmarkResponse>가 아니라 SpotBookmarkResponse를 반환하도록 설계해야 함.
    조회수 증가 쿼리가 빠져있음.
     */
    @GetMapping("bookmark/{idx}")
    public List<SpotBookmarkResponse> findBookmarkBySpotId(@PathVariable("idx") Long id ) {
        return spotService.findBookmarkBySpotId(id);
    }

    // 북마크 순 조회
    /*
    특정 핫플레이스를 북마크한 사용자들의 개인정보가 모두 조회되어 전달되고 있음.
    북마크한 사용자들의 목록이 아니라 북마크 "수"를 전달하도록 수정이 필요함.
    즉, user 테이블 select 쿼리가 나가지 않도록 해야 함.
    ++ 중요한건 아니지만 북마크 순으로 정렬된 "핫플레이스"를 조회하는 것인데, 함수명이 findBookmark으로 되어있음!
    수정 안해도 무관.
     */
    @GetMapping("bookmark")
    public SpotBookmarkPageResponse findBookmarkGroupBySpotId(
            @RequestParam(required = false, defaultValue = "1", value = "page")int page,
            @RequestParam(required = false, defaultValue = "10", value = "size")int size) {
        return spotService.findBookmarkGroupBySpotId(page - 1, size);
    }

    // 북마크 중복등록 예외처리가 안되어있음.
    @PostMapping("bookmark")
    public Long saveBookmark(@RequestBody BookmarkSaveRequest bookmarkSaveRequest){
        return spotService.saveBookmark(bookmarkSaveRequest);
    }

    // bookmark id가 아니라 post id를 넘겨주고 user와 post를 가지고 bookmark를 조회해야 함
    // 프론트에서 bookmark id를 알 수 없기 때문
    @DeleteMapping("bookmark/{id}")
    public Long deleteBookmark(@PathVariable Long id, @RequestParam String email) {
        return spotService.deleteBookmark(id, email);
    }

}
