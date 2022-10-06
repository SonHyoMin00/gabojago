package com.hanium.gabojago.config.exception;


import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.impl.FileSizeLimitExceededException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MultipartException;

@Slf4j
@RestControllerAdvice
public class ControllerAdvice {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> runtimeException(RuntimeException e) {
        log.error(e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

    @ExceptionHandler(MultipartException.class)
    public ResponseEntity<String> fileSizeLimitExceededException(MultipartException e) {
        Throwable cause = e.getCause().getCause();
        if(cause instanceof FileSizeLimitExceededException)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("파일 업로드 용량을 초과했습니다. 제한된 용량: 50MB");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("지원하지 않는 파일 첨부 요청입니다.");
    }

}
