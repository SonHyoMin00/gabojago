package com.hanium.gabojago.controller;

import com.hanium.gabojago.dto.SpotResponse;
import com.hanium.gabojago.service.SpotService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
    public List<SpotResponse> getHotplacesByViewCnt(
            @RequestParam(required = false, defaultValue = "0", value = "page")int page,
            @RequestParam(required = false, defaultValue = "10", value = "size")int size) {
        return spotService.findHotplaceByViewCnt(page, size);
    }

    // 상세정보 조회
    @GetMapping("hotplaces/id/{idx}")
    public List<SpotResponse> findHotplaceBySpotId(@PathVariable("idx") Long id ) {
        return spotService.findHotplaceBySpotId(id);
    }

    // 지역별 순위 조회
    @GetMapping("region/{region}")
    public List<SpotResponse> getHotplacesByRegion(
            @PathVariable String region,
            @RequestParam(required = false, defaultValue = "0", value = "page")int page,
            @RequestParam(required = false, defaultValue = "10", value = "size")int size) {
        return spotService.findHotplacesByRegion(region, page, size);
    }

    // 컨셉별 핫플레이스 조회
    @GetMapping("tag/{tagId}")
    public List<SpotResponse> getHotplacesByTag(
            @PathVariable int tagId,
            @RequestParam(required = false, defaultValue = "0", value = "page")int page,
            @RequestParam(required = false, defaultValue = "10", value = "size")int size) {
        return spotService.findHotplaceByTag(tagId, page, size);
    }
}
