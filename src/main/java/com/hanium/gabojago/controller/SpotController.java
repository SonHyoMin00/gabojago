package com.hanium.gabojago.controller;

import com.hanium.gabojago.domain.Spot;
import com.hanium.gabojago.dto.SpotResonse;
import com.hanium.gabojago.service.SpotService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class SpotController {
    private final SpotService spotService;

    // 실시간 순위 조회(top 10)
    @GetMapping("hotplaces/realtime")
    public List<SpotResonse> getRealtimeHotplaces() {
        return spotService.getRealtimeHotplaces();
    }

    // 조회순 조회
    @GetMapping("hotplaces")
    public List<SpotResonse> getHotplacesByViewCnt(
            @RequestParam(required = false, defaultValue = "0", value = "page")int page,
            @RequestParam(required = false, defaultValue = "10", value = "size")int size) {
        return spotService.findHotplaceByViewCnt(page, size);
    }


}
