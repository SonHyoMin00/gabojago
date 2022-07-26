package com.hanium.gabojago.controller;

import com.hanium.gabojago.dto.SpotResponse;
import com.hanium.gabojago.service.SpotService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

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
