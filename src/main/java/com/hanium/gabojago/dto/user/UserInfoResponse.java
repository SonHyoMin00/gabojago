package com.hanium.gabojago.dto.user;

import com.hanium.gabojago.domain.User;
import lombok.Builder;
import lombok.Getter;

@Getter
public class UserInfoResponse {
    private Long userId;
    private String name;

    private String profilePhoto;

    @Builder
    public UserInfoResponse(User user) {
        this.userId = user.getUserId();
        this.name = user.getName();
        this.profilePhoto = user.getProfilePhotoPath();
    }
}
