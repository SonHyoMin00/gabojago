package com.hanium.gabojago.controller;

import com.hanium.gabojago.domain.Spot;
import com.hanium.gabojago.service.SpotService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class SpotController {
    private final SpotService spotService;

    // 실시간 순위 조회(top 10)
    @GetMapping("hotplaces/realtime")
    public List<Spot> getRealtimeHotplaces() {
        return spotService.getRealtimeHotplaces();
    }

    // 조회순 조회
    @GetMapping("hotplaces")
    public Page<Spot> getHotplacesByViewCnt(
            @RequestParam(required = false, defaultValue = "0", value = "page")int page) {
        return spotService.findHotplaceByViewCnt(page);
    }

    // 대전지역 20개 핫플 조회
    @GetMapping("test")
    public List<Spot> daejeonFindHotplace(){
        return spotService.daejeonFindHotplace("대전");
    }

    // 상세정보 조회
    @GetMapping("hotplaces/id/{idx}")
    public List<Spot> daejeonFindHotplace(@PathVariable("idx") Long id ) {
        return spotService.findHotplaceBySpotId(id);
    }


}
