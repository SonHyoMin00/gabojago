package com.hanium.gabojago.service;

import com.hanium.gabojago.domain.Bookmark;
import com.hanium.gabojago.domain.Spot;
import com.hanium.gabojago.domain.User;
import com.hanium.gabojago.dto.SpotBookmarkPageResponse;
import com.hanium.gabojago.dto.SpotBookmarkResponse;
import com.hanium.gabojago.jwt.JwtProperties;
import com.hanium.gabojago.oauth.kakao.KakaoOAuth2;
import com.hanium.gabojago.oauth.kakao.KakaoUserDto;
import com.hanium.gabojago.repository.BookmarkRepository;
import com.hanium.gabojago.repository.SpotRepository;
import com.hanium.gabojago.repository.UserRepository;
import io.jsonwebtoken.Jwts;
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
public class UserService {
    private final KakaoOAuth2 kakaoOAuth2;
    private final UserRepository userRepository;
    private final BookmarkRepository bookmarkRepository;
    private final SpotRepository spotRepository;

    public void kakaoJoin(String authorizedCode) {
        // 카카오 OAuth2 를 통해 카카오 사용자 정보 조회
        KakaoUserDto user = kakaoOAuth2.getUserInfo(authorizedCode);
        String email = user.getEmail();
        String name = user.getName();
        Byte age = user.getAge();
        String profilePhoto = user.getProfilePhoto();

        // DB에 있는 회원인지 확인
        if (!userRepository.findByEmail(email).isPresent()) {
            // 없으면 회원가입
            User newUser = User.builder()
                    .email(email)
                    .name(name)
                    .age(age)
                    .profilePhoto(profilePhoto)
                    .build();

            userRepository.save(newUser);
        }
        else
            throw new IllegalStateException("이미 회원가입한 유저입니다.");
    }

    // authorizedCode로 가입된 사용자 조회
    public User findUserByAuthorizedCode(String authorizedCode) {
        String email = kakaoOAuth2.getUserEmail(authorizedCode);
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("이메일 \"" + email + "\"에 해당하는 사용자가 존재하지 않습니다."));
    }

    public User findUserByJwtToken(String token) {
        log.info("-----JWT:" + token);
        log.info("-----secretKey:" + JwtProperties.SECRETKEY);
        String email = Jwts.parser().setSigningKey(JwtProperties.SECRETKEY.getBytes())
                .parseClaimsJws(token).getBody().getSubject();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("이메일 \"" + email + "\"에 해당하는 사용자가 존재하지 않습니다."));
    }

    //Page<Spot>을 SpotBookmarkPageResponse(dto)로 바꾸는 함수
    private SpotBookmarkPageResponse convertSpotsToSpotBookmarkPageResponse(Page<Spot> spots) {
        //총 페이지 수
        int totalPages = spots.getTotalPages();
        log.info("총 페이지 수: " + spots.getTotalPages());

        //spotBookmarkResponses DTO로 변환
        List<SpotBookmarkResponse> spotBookmarkResponses = spots.getContent()
                .stream().map(SpotBookmarkResponse::new).collect(Collectors.toList());

        // 총 페이지 수 추가하여 반환
        return SpotBookmarkPageResponse.builder()
                .spotBookmarkResponses(spotBookmarkResponses)
                .totalPages(totalPages)
                .build();
    }

    // 사용자 북마크 조회(마이페이지)
    public SpotBookmarkPageResponse getUserBookmarks(String email, int page, int size) {
        //1. 이메일에 해당하는 사용자 찾기
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("해당 이메일의 사용자가 없습니다."));

        //2. 북마크 테이블에서 사용자가 북마크한 핫플 찾기
        List<Bookmark> bookmark = bookmarkRepository.findByUser(user);
        if (bookmark.isEmpty()) {
            throw new IllegalArgumentException("해당 사용자의 북마크가 없습니다.");
        }

        //3. 핫플들 정보 페이지마다 보내주기
        Pageable pageable = PageRequest.of(page, size);
        Page<Spot> spots = spotRepository.findAllByBookmarksIn(bookmark, pageable);
        return convertSpotsToSpotBookmarkPageResponse(spots);
    }
}
