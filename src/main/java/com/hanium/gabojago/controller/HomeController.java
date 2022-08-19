package com.hanium.gabojago.controller;

import com.hanium.gabojago.domain.User;
import com.hanium.gabojago.jwt.JwtTokenProvider;
import com.hanium.gabojago.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RequiredArgsConstructor
@RestController
public class HomeController {
    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;

//    @GetMapping("/users/kakao/callback")
//    public String kakaoLoginCallback(@RequestParam String code) {
//        User user = userService.findUserByAuthorizedCode(code);
//        return "redirect:/";
//    }

    @GetMapping("/users/kakao/callback")
    public ResponseEntity<Object> kakaoLoginCallback(@RequestParam String code) {
        User user = userService.findUserByAuthorizedCode(code);
        HttpHeaders headers = new HttpHeaders();
        String token = jwtTokenProvider.createToken(user.getEmail());
        headers.setBearerAuth(token);
        headers.set("token", token);
        headers.setLocation(URI.create("http://localhost:5501/html/index.html"));
        return new ResponseEntity<>(headers, HttpStatus.MOVED_PERMANENTLY);
    }

    // 회원가입
//    @PostMapping("/users/kakao/join")
//    public String kakaoJoin(@RequestBody Map<String, String> code) {
//        userService.kakaoJoin(code.get("code"));
//        return "회원가입 성공";
//    }

    // 로그인
//    @PostMapping("/users/kakao/login")
//    public String login(@RequestBody Map<String, String> code) {
//        User user = userService.findUserByAuthorizedCode(code.get("code"));
//        return jwtTokenProvider.createToken(user.getEmail());
//    }

    @GetMapping("/test")
    public String test() {
        return "로그인 테스트 성공";
    }
}
