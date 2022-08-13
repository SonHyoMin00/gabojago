package com.hanium.gabojago.controller;

import com.hanium.gabojago.domain.User;
import com.hanium.gabojago.jwt.JwtTokenProvider;
import com.hanium.gabojago.repository.UserRepository;
import com.hanium.gabojago.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@RestController
public class HomeController {
    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;
    private final UserRepository userRepository;

    @GetMapping("/user/kakao/callback")
    public String kakaoLoginCallback(@RequestParam String code) {
        return code;
    }

    // 회원가입
    @GetMapping("/user/kakao/join")
    public String kakaoJoin(@RequestParam String code) {
        userService.kakaoJoin(code);
        return "회원가입 성공";
    }

    // 로그인
    @PostMapping("/user/kakao/login")
    public String login(@RequestBody Map<String, String> email) {
        log.info("user email = {}", email);
        User member = userRepository.findByEmail(email.get("email"))
                .orElseThrow(() -> new IllegalArgumentException("가입되지 않은 E-MAIL 입니다."));

        return jwtTokenProvider.createToken(member.getEmail());
    }

    @GetMapping("/test")
    public String test() {
        return "로그인 테스트 성공";
    }
}
