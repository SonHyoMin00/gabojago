package com.hanium.gabojago.jwt;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public class BasicResponse {
    private final String message;
    private final HttpStatus httpStatus;
}
