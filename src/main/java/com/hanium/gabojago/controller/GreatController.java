package com.hanium.gabojago.controller;

import com.hanium.gabojago.domain.User;
import com.hanium.gabojago.service.GreatService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RequiredArgsConstructor
@RequestMapping("posts/great")
@RestController
public class GreatController {
    private final UserService userService;
    private final GreatService greatService;

    //의미는 Post인데..? 뭔가 부족한 코드.
    @PostMapping("{id}")
    public String insertGreat(@PathVariable Long id,
                              HttpServletRequest httpServletRequest) {

        String token = httpServletRequest.getHeader("Authorization");
        User user = userService.findUserByJwtToken(token);
        if (user == null)
            throw new IllegalStateException("잘못된 접근입니다.");

        return greatService.insertGreat(id, user);
    }

    @DeleteMapping("{id}")
    public String deleteGreat(@PathVariable Long id,
                            HttpServletRequest httpServletRequest) {

        String token = httpServletRequest.getHeader("Authorization");
        User user = userService.findUserByJwtToken(token);
        if (user == null)
            throw new IllegalStateException("잘못된 접근입니다.");

        return greatService.deleteGreat(id, user);
    }
}
