package com.hanium.gabojago.service;

import com.hanium.gabojago.domain.Spot;
import com.hanium.gabojago.repository.SpotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class SpotService {
    private final SpotRepository spotRepository;

    public List<Spot> getRealtimeHotplaces() {
        return spotRepository.findTop10ByOrderByViewCntDesc();
    }

    public Page<Spot> findHotplaceByViewCnt(int page) {
        return spotRepository.findAll(PageRequest.of(page, 10, Sort.by(Sort.Direction.DESC, "viewCnt")));
    }

    public List<Spot> daejeonFindHotplace(String region){
        return spotRepository.findTop20ByRegion("대전");
    }
}
