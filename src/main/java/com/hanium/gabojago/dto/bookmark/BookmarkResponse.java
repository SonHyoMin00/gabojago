package com.hanium.gabojago.dto.bookmark;

import com.hanium.gabojago.domain.Bookmark;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class BookmarkResponse {
    private String email;
    private String name;
    Byte age;

    @Builder
    public BookmarkResponse(Bookmark bookmark){
        this.email = bookmark.getUser().getEmail();
        this.name = bookmark.getUser().getName();
        this.age = bookmark.getUser().getAge();
    }
}
