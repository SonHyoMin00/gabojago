package com.hanium.gabojago.controller;

import com.hanium.gabojago.domain.User;
import com.hanium.gabojago.dto.bookmark.BookmarkSaveRequest;
import com.hanium.gabojago.service.BookmarkService;
import com.hanium.gabojago.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RequiredArgsConstructor
@RequestMapping("hotplaces/bookmark")
@RestController
public class BookmarkController {
    private final UserService userService;
    private final BookmarkService bookmarkService;

    //북마크 추가하기
    @PostMapping
    public Long saveBookmark(@RequestBody BookmarkSaveRequest bookmarkSaveRequest,
                             HttpServletRequest httpServletRequest){

        String token = httpServletRequest.getHeader("Authorization");
        User user = userService.findUserByJwtToken(token);

        return bookmarkService.saveBookmark(bookmarkSaveRequest, user);
    }

    //북마크 삭제하기
    @DeleteMapping("{spotId}")
    public Long deleteBookmark(@PathVariable Long spotId, HttpServletRequest httpServletRequest) {

        String token = httpServletRequest.getHeader("Authorization");
        User user = userService.findUserByJwtToken(token);

        return bookmarkService.deleteBookmark(spotId, user);
    }
}
