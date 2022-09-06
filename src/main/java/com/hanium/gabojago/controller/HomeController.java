package com.hanium.gabojago.controller;

import com.hanium.gabojago.domain.User;
import com.hanium.gabojago.jwt.JwtTokenProvider;
import com.hanium.gabojago.service.UserService;
import com.hanium.gabojago.util.properties.OAuthProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RequiredArgsConstructor
@RestController
public class HomeController {
    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;

    // 카카오 로그인 콜백함수(개발용)
    @GetMapping("/users/kakao/callback")
    public ResponseEntity<String> kakaoLoginCallback(@RequestParam String code) {
        return ResponseEntity.ok().body(code);
    }

    // 회원가입 또는 로그인(개발용)
    @PostMapping("/users/kakao/login/test")
    public ResponseEntity<String> loginTest(@RequestBody Map<String, String> codeRequest) {
        User user = userService.findUserByAuthorizedCode(codeRequest.get("code"), OAuthProperties.REDIRECT_TEST);

        String token = jwtTokenProvider.createToken(user.getUserId(), user.getEmail());

        return ResponseEntity.ok()
                .header("Access-Token", token)
                .body("로그인 되었습니다.");
    }

    // 회원가입 또는 로그인
    @PostMapping("/users/kakao/login")
    public ResponseEntity<String> login(@RequestBody Map<String, String> codeRequest) {
        User user = userService.findUserByAuthorizedCode(codeRequest.get("code"), OAuthProperties.REDIRECT_PROD);

        String token = jwtTokenProvider.createToken(user.getUserId(), user.getEmail());

        return ResponseEntity.ok()
                .header("Access-Token", token)
                .body("로그인 되었습니다.");
    }

    // 회원 탈퇴
    @DeleteMapping("/users/withdrawal")
    public Long withdrawal(HttpServletRequest httpServletRequest) {
        String token = httpServletRequest.getHeader("Authorization");
        User user = userService.findUserByJwtToken(token);
        return userService.deleteUser(user);
    }

    @GetMapping("/test")
    public String test() {
        return "로그인 테스트 성공";
    }
}
