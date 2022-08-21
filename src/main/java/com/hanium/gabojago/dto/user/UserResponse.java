package com.hanium.gabojago.dto.user;

import com.hanium.gabojago.domain.User;
import com.hanium.gabojago.util.properties.ApplicationProperties;
import lombok.Builder;
import lombok.Getter;

@Getter
public class UserResponse {
    private final Long id;
    private final String name;
    private final String profilePhoto;
    private final Long postCnt;
    private final Long commentCnt;
    private final Long bookmarkCnt;

    @Builder
    public UserResponse(User user, Long postCnt, Long commentCnt, Long bookmarkCnt) {
        this.id = user.getUserId();
        this.name = user.getName();
        this.profilePhoto = ApplicationProperties.HOST_IMAGE_URL + user.getProfilePhoto();
        this.postCnt = postCnt;
        this.commentCnt = commentCnt;
        this.bookmarkCnt = bookmarkCnt;
    }
}
