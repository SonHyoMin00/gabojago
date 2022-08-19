package com.hanium.gabojago.controller;

import com.hanium.gabojago.domain.User;
import com.hanium.gabojago.jwt.JwtTokenProvider;
import com.hanium.gabojago.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;

@RequiredArgsConstructor
@RestController
public class HomeController {
    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;

    // 카카오 로그인 콜백함수 -> 회원가입 및 로그인 일괄처리
    @GetMapping("/users/kakao/callback")
    public ResponseEntity<Object> kakaoLoginCallback(@RequestParam String code) {
        User user = userService.findUserByAuthorizedCode(code);

        HttpHeaders headers = new HttpHeaders();
        String token = jwtTokenProvider.createToken(user.getUserId(), user.getEmail());

        headers.set("Access-Token", token);
        headers.setLocation(URI.create("http://localhost:8080/5501/html/index.html"));
        return new ResponseEntity<>(headers, HttpStatus.MOVED_PERMANENTLY);
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
