package com.hanium.gabojago.dto.user;

import com.hanium.gabojago.domain.User;
import lombok.Builder;
import lombok.Getter;

@Getter
public class UserPostResponse {
    private Long userId;
    private String name;

    @Builder
    public UserPostResponse(User user) {
        this.userId = user.getUserId();
        this.name = user.getName();
    }
}
