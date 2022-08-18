package com.hanium.gabojago.controller;

import com.hanium.gabojago.domain.User;
import com.hanium.gabojago.dto.post.PostPageResponse;
import com.hanium.gabojago.service.PostService;
import com.hanium.gabojago.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RequiredArgsConstructor
@RequestMapping("users/")
@RestController
public class MypageController {
    private final UserService userService;
    private final PostService postService;

    //특정 유저의 게시글 조회(마이페이지)
    @GetMapping("posts")
    public PostPageResponse userPosts(HttpServletRequest httpServletRequest,
                                      @RequestParam(required = false, defaultValue = "1", value = "page")int page,
                                      @RequestParam(required = false, defaultValue = "10", value = "size")int size) {
        String token = httpServletRequest.getHeader("Authorization");
        User user = userService.findUserByJwtToken(token);

        return postService.getUserPosts(user, page - 1, size);
    }
}
