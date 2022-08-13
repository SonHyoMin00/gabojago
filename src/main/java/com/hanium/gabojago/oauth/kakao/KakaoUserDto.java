package com.hanium.gabojago.oauth.kakao;

import lombok.Builder;
import lombok.Getter;

@Getter
public class KakaoUserDto {
    private String email;
    private String name;
    private Byte age;
    private String profilePhoto;

    @Builder
    public KakaoUserDto(String email, String name, Byte age, String profilePhoto) {
        this.email = email;
        this.name = name;
        this.age = age;
        this.profilePhoto = profilePhoto;
    }
}
