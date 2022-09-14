package com.hanium.gabojago.dto.user;

import com.hanium.gabojago.domain.User;
import lombok.Builder;
import lombok.Getter;

@Getter
public class UserMypageResponse {
    private final Long id;
    private final String name;
    private final String profilePhoto;
    private final Long postCnt;
    private final Long commentCnt;
    private final Long bookmarkCnt;

    @Builder
    public UserMypageResponse(User user, Long postCnt, Long commentCnt, Long bookmarkCnt) {
        this.id = user.getUserId();
        this.name = user.getName();
        this.profilePhoto = user.getProfilePhotoPath();
        this.postCnt = postCnt;
        this.commentCnt = commentCnt;
        this.bookmarkCnt = bookmarkCnt;
    }
}
