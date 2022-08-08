package com.hanium.gabojago.controller;

import com.hanium.gabojago.service.GreatService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("posts/great")
@RestController
public class GreatController {
    private final GreatService greatService;

    //의미는 Post인데..? 뭔가 부족한 코드.
    @GetMapping("{id}")
    public String insertGreat(@PathVariable Long id,
                            @RequestParam String email) {
        return greatService.insertGreat(id, email);
    }

    @DeleteMapping("{id}")
    public String deleteGreat(@PathVariable Long id,
                            @RequestParam String email) {
        return greatService.deleteGreat(id, email);
    }
}
