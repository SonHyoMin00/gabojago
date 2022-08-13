package com.hanium.gabojago.service;

import com.hanium.gabojago.domain.User;
import com.hanium.gabojago.oauth.kakao.KakaoOAuth2;
import com.hanium.gabojago.oauth.kakao.KakaoUserDto;
import com.hanium.gabojago.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService {
    private final KakaoOAuth2 kakaoOAuth2;
    private final UserRepository userRepository;

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

}
