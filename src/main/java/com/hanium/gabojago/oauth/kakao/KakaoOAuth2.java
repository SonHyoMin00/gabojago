package com.hanium.gabojago.oauth.kakao;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Component
public class KakaoOAuth2 {
    @Value("${oauth2.kakao.client-id}")
    String clientId;

    private String getAccessToken(String authorizedCode) {
        // HttpHeader 오브젝트 생성
        HttpHeaders headers = new HttpHeaders();

        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // HttpBody 오브젝트 생성
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", clientId);
        params.add("redirect_uri", "http://localhost:8080/user/kakao/callback");
        params.add("code", authorizedCode);

        // HttpHeader와 HttpBody를 하나의 오브젝트에 담기
        RestTemplate rt = new RestTemplate();
        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest =
                new HttpEntity<>(params, headers);

        // Http 요청
        ResponseEntity<String> response = rt.exchange(
                "https://kauth.kakao.com/oauth/token",
                HttpMethod.POST,
                kakaoTokenRequest,
                String.class
        );

        // JSON -> 액세스 토큰 파싱
        String tokenJson = response.getBody();
        JSONObject json = new JSONObject(tokenJson);
        return json.getString("access_token");
    }

    private JSONObject requestUserInfoJsonObject(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // HttpHeader와 HttpBody를 하나의 오브젝트에 담기
        RestTemplate rt = new RestTemplate();
        HttpEntity<MultiValueMap<String, String>> kakaoProfileRequest = new HttpEntity<>(headers);

        // Http 요청하기
        ResponseEntity<String> response = rt.exchange(
                "https://kapi.kakao.com/v2/user/me",
                HttpMethod.POST,
                kakaoProfileRequest,
                String.class
        );

        // 사용자 정보 json 객체 리턴
        return new JSONObject(response.getBody());
    }

    private KakaoUserDto getUserByAccessToken(String accessToken) {
        JSONObject body = requestUserInfoJsonObject(accessToken);

        // 유저 정보 파싱
        JSONObject kakaoAccount = body.getJSONObject("kakao_account");
        JSONObject profile = kakaoAccount.getJSONObject("profile");
        String email = kakaoAccount.getString("email");
        String nickname = body.getJSONObject("properties").getString("nickname");
        String ageString = kakaoAccount.getString("age_range");
        String profilePhoto = profile.getString("profile_image_url");

        byte age = 10;
        switch (ageString.substring(0, 2)) {
            case "20":
                age = 20;
                break;
            case "30":
                age = 30;
            case "40":
                age = 40;
            case "50":
            case "60":
            case "70":
            case "80":
            case "90":
                age = 50;
                break;
        }

        return KakaoUserDto.builder()
                .email(email)
                .name(nickname)
                .age(age)
                .profilePhoto(profilePhoto)
                .build();
    }

    public KakaoUserDto getUserInfo(String authorizedCode) {
        // 1. 인가코드 -> 액세스 토큰
        String accessToken = getAccessToken(authorizedCode);
        // 2. 액세스 토큰 -> 카카오 사용자 정보
        KakaoUserDto user = getUserByAccessToken(accessToken);

        return user;
    }

    public String getUserEmail(String authorizedCode) {
        String accessToken = getAccessToken(authorizedCode);
        // HttpHeader 오브젝트 생성
        JSONObject body = requestUserInfoJsonObject(accessToken);
        // 사용자 이메일 리턴
        return body.getJSONObject("kakao_account").getString("email");
    }
}
