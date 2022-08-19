package com.hanium.gabojago.controller;

import com.hanium.gabojago.dto.SpotBookmarkPageResponse;
import com.hanium.gabojago.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RequiredArgsConstructor
@RequestMapping("users")
@RestController
public class UserController {
    private final UserService userService;

    //사용자 북마크 조회
    @GetMapping("bookmark")
    public SpotBookmarkPageResponse userBookmarks(@RequestParam String email,
                                                  @RequestParam(required = false, defaultValue = "1", value = "page")int page,
                                                  @RequestParam(required = false, defaultValue = "10", value = "size")int size) {

        return userService.getUserBookmarks(email, page - 1, size);
    }
}
