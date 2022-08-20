package com.hanium.gabojago.controller;

import com.hanium.gabojago.domain.User;
import com.hanium.gabojago.dto.post.PostPageResponse;
import com.hanium.gabojago.dto.user.NameUpdateRequest;
import com.hanium.gabojago.service.PostService;
import com.hanium.gabojago.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RequiredArgsConstructor
@RequestMapping("users")
@RestController
public class UserController {
    private final UserService userService;
    private final PostService postService;

    //특정 유저의 게시글 조회
    @GetMapping("/posts")
    public PostPageResponse userPosts(HttpServletRequest httpServletRequest,
                                      @RequestParam(required = false, defaultValue = "1", value = "page")int page,
                                      @RequestParam(required = false, defaultValue = "10", value = "size")int size) {
        String token = httpServletRequest.getHeader("Authorization");
        User user = userService.findUserByJwtToken(token);

        return postService.getUserPosts(user, page - 1, size);
    }

    // 닉네임 변경
    @PutMapping("/name")
    public String updateName(HttpServletRequest httpServletRequest,
                             @RequestBody NameUpdateRequest nameUpdateRequest) {
        String token = httpServletRequest.getHeader("Authorization");
        User user = userService.findUserByJwtToken(token);

        return userService.updateName(user, nameUpdateRequest);
    }

    // 프로필 사진 변경
    @PutMapping("/profile")
    public String updateProfilePhoto(HttpServletRequest httpServletRequest,
                                     @RequestPart(name = "profilePhoto") MultipartFile multipartFile) {
        String token = httpServletRequest.getHeader("Authorization");
        User user = userService.findUserByJwtToken(token);
        return userService.updateProfilePhoto(user, multipartFile);
    }

    // 프로필 사진 삭제
    @DeleteMapping("/profile")
    public String updateProfilePhoto(HttpServletRequest httpServletRequest) {
        String token = httpServletRequest.getHeader("Authorization");
        User user = userService.findUserByJwtToken(token);

        return userService.deleteProfilePhoto(user);
    }
}
