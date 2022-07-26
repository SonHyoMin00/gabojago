package com.hanium.gabojago.service;

import com.hanium.gabojago.domain.Spot;
import com.hanium.gabojago.domain.SpotTag;
import com.hanium.gabojago.dto.SpotResponse;
import com.hanium.gabojago.repository.SpotRepository;
import com.hanium.gabojago.repository.SpotTagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class SpotService {
    private final SpotRepository spotRepository;
    private final SpotTagRepository spotTagRepository;

    // 실시간 핫플레이스 데이터 가져오기
    public List<SpotResponse> getRealtimeHotplaces() {
        List<Spot> spots = spotRepository.findTop10ByOrderByViewCntDesc();
        return spots.stream().map(SpotResponse::new).collect(Collectors.toList());
    }

    // 조회순으로 정렬된 핫플레이스 데이터 가져오기
    public List<SpotResponse> findHotplaceByViewCnt(int page, int size) {
        Page<Spot> spots = spotRepository.findAll(PageRequest.of(
                page, size, Sort.by(Sort.Direction.DESC, "viewCnt")));
        return spots.getContent()
                .stream().map(SpotResponse::new).collect(Collectors.toList());
    }

    // 지역별 핫플레이스 데이터 가져오기
    public List<SpotResponse> findHotplacesByRegion(String region, int page, int size) {
        Pageable pageable = PageRequest.of(
                page, size, Sort.by(Sort.Direction.DESC, "viewCnt"));
        Page<Spot> spots = spotRepository.findByRegion(region, pageable);
        return spots.getContent()
                .stream().map(SpotResponse::new).collect(Collectors.toList());
    }

    // 태그별 핫플레이스 데이터 가져오기
    public List<SpotResponse> findHotplaceByTag(int tagId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<SpotTag> spots = spotTagRepository.findHotplaceByTag(tagId, pageable);
        return spots.stream().map(SpotResponse::new).collect(Collectors.toList());
    }
}
