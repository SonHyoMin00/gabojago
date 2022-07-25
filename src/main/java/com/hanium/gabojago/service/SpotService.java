package com.hanium.gabojago.service;

import com.hanium.gabojago.domain.Spot;
import com.hanium.gabojago.dto.SpotResonse;
import com.hanium.gabojago.repository.SpotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class SpotService {
    private final SpotRepository spotRepository;

    public List<SpotResonse> getRealtimeHotplaces() {
        List<Spot> spots = spotRepository.findTop10ByOrderByViewCntDesc();
        return spots.stream().map(SpotResonse::new).collect(Collectors.toList());
    }

    public List<SpotResonse> findHotplaceByViewCnt(int page, int size) {
        Page<Spot> spots = spotRepository.findAll(PageRequest.of(
                page, size, Sort.by(Sort.Direction.DESC, "viewCnt")));
        return spots.getContent()
                .stream().map(SpotResonse::new).collect(Collectors.toList());
    }
}
