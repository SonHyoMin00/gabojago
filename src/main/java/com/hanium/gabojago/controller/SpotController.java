package com.hanium.gabojago.controller;

import com.hanium.gabojago.domain.User;
import com.hanium.gabojago.dto.bookmark.BookmarkSaveRequest;
import com.hanium.gabojago.dto.bookmark.SpotBookmarkPageResponse;
import com.hanium.gabojago.dto.spot.SpotMapResponse;
import com.hanium.gabojago.dto.spot.SpotPageResponse;
import com.hanium.gabojago.dto.spot.SpotResponse;
import com.hanium.gabojago.service.SpotService;
import com.hanium.gabojago.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.List;

@RequiredArgsConstructor
@RequestMapping("hotplaces")
@RestController
public class SpotController {
    private final SpotService spotService;
    private final UserService userService;

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
    @GetMapping("id/{idx}")
    public SpotMapResponse findHotplaceBySpotId(@PathVariable("idx") Long id ) {
        return spotService.findHotplaceBySpotId(id);
    }

    // 사용자 위치기반 조회
    @GetMapping("location")
    public List<SpotMapResponse> findLocationBySpotXAndSpotY(
            @RequestParam(required = false, defaultValue = "37.55", value = "xStart")BigDecimal xStart,
            @RequestParam(required = false, defaultValue = "37.65", value = "xEnd")BigDecimal xEnd,
            @RequestParam(required = false, defaultValue = "126.96", value = "yStart")BigDecimal yStart,
            @RequestParam(required = false, defaultValue = "126.97", value = "yEnd")BigDecimal yEnd) {
        return spotService.findLocationBySpotXAndSpotY(xStart, xEnd, yStart, yEnd);
    }

    // 북마크 순 조회
    /*
    북마크한 사용자들의 목록이 아니라 북마크 "수"를 전달하도록 수정이 필요함.
     */
    @GetMapping("bookmark")
    public SpotBookmarkPageResponse getHotplacesByBookmark(
            @RequestParam(required = false, defaultValue = "1", value = "page")int page,
            @RequestParam(required = false, defaultValue = "10", value = "size")int size) {
        return spotService.findHotplacesByBookmark(page - 1, size);
    }

    //북마크 추가하기
    @PostMapping("bookmark")
    public Long saveBookmark(@RequestBody BookmarkSaveRequest bookmarkSaveRequest,
                             HttpServletRequest httpServletRequest){

        String token = httpServletRequest.getHeader("Authorization");
        User user = userService.findUserByJwtToken(token);

        return spotService.saveBookmark(bookmarkSaveRequest, user);
    }

    //북마크 삭제하기
    @DeleteMapping("bookmark/{spotId}")
    public Long deleteBookmark(@PathVariable Long spotId, HttpServletRequest httpServletRequest) {

        String token = httpServletRequest.getHeader("Authorization");
        User user = userService.findUserByJwtToken(token);

        return spotService.deleteBookmark(spotId, user);
    }

}
