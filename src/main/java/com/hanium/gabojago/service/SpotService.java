package com.hanium.gabojago.service;

import com.hanium.gabojago.domain.Spot;
import com.hanium.gabojago.dto.SpotPageResponse;
import com.hanium.gabojago.dto.SpotResponse;
import com.hanium.gabojago.repository.SpotRepository;
import com.hanium.gabojago.repository.SpotTagRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
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
    public  List<SpotResponse> findHotplacesByViewCnt(int page, int size) {
        List<Spot> spots = spotRepository.findHotplacesByViewCnt(PageRequest.of(page, size));

        return spots.stream().map(SpotResponse::new).collect(Collectors.toList());
//        //총 페이지 수
//        int totalPages = spots.getTotalPages();
//        log.info("총 페이지 수: " + spots.getTotalPages());
//
//        //spotResponses DTO로 변환
//        List<SpotResponse> spotResponses = spots.getContent()
//                .stream().map(SpotResponse::new).collect(Collectors.toList());
//
//        // 총 페이지 수 추가하여 반환
//        return SpotPageResponse.builder()
//                .spotResponses(spotResponses)
//                .totalPages(totalPages)
//                .build();
    }

    // 지역별 핫플레이스 데이터 가져오기
    public List<SpotResponse> findHotplacesByRegion(String region, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        List<Spot> spots = spotRepository.findHotplacesByRegion(region, pageable);
        return spots.stream().map(SpotResponse::new).collect(Collectors.toList());
//        //총 페이지 수
//        int totalPages = spots.getTotalPages();
//        log.info("총 페이지 수: " + spots.getTotalPages());
//
//        //spotResponses DTO로 변환
//        List<SpotResponse> spotResponses = spots.getContent()
//                .stream().map(SpotResponse::new).collect(Collectors.toList());
//
//        // 총 페이지 수 추가하여 반환
//        return SpotPageResponse.builder()
//                .spotResponses(spotResponses)
//                .totalPages(totalPages)
//                .build();
    }

    // 태그별 핫플레이스 데이터 가져오기
    public SpotPageResponse findHotplacesByTag(int tagId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Spot> spots = spotRepository.findHotplaceByTag(tagId, pageable);

        //총 페이지 수
        int totalPages = spots.getTotalPages();
        log.info("총 페이지 수 - " + spots.getTotalPages());

        //spotResponses DTO로 변환
        List<SpotResponse> spotResponses = spots.getContent()
                .stream().map(SpotResponse::new).collect(Collectors.toList());

        // 총 페이지 수 추가하여 반환
        return SpotPageResponse.builder()
                .spotResponses(spotResponses)
                .totalPages(totalPages)
                .build();
    }

    // 상세 핫플레이스 데이터 가져오기
    public List<SpotResponse> findHotplaceBySpotId(Long spotId){
        List<Spot> spots = spotRepository.findAllBySpotId(spotId);
        return spots.stream().map(SpotResponse::new).collect(Collectors.toList());
    }
}
