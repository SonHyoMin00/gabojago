package com.hanium.gabojago.dto.comment;

import com.hanium.gabojago.domain.Comment;
import com.hanium.gabojago.domain.Post;
import com.hanium.gabojago.domain.User;
import lombok.Getter;

@Getter
public class CommentRequest {
    private String context;

    public Comment toComment(User user, Post post) {
        return Comment.builder()
                .user(user)
                .post(post)
                .context(context)
                .build();
    }
}
