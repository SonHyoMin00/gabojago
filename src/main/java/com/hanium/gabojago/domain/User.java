package com.hanium.gabojago.domain;

import com.hanium.gabojago.util.properties.ApplicationProperties;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class User extends BaseTimeEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    private String email;

    private String name;

    private Byte age;

    private String profilePhoto;

    @Builder
    public User(String email, String name, Byte age, String profilePhoto) {
        this.email = email;
        this.name = name;
        this.age = age;
        this.profilePhoto = profilePhoto;
    }

    public void updateName(String name) {
        this.name = name;
    }

    public void updateProfilePhoto(String profilePhoto) {
        this.profilePhoto = profilePhoto;
    }

    public String getProfilePhotoPath() {
        if(profilePhoto == null) return null;
        else if(profilePhoto.contains("http://k.kakaocdn.net/")) return profilePhoto;

        return ApplicationProperties.HOST_IMAGE_URL + "profile/" + profilePhoto;
    }

    public void deleteProfile() {
        this.profilePhoto = null;
    }
}
