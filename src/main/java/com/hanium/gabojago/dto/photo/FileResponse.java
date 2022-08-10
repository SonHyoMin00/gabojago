package com.hanium.gabojago.dto.photo;

import com.hanium.gabojago.domain.Photo;
import com.hanium.gabojago.domain.PostTag;
import lombok.Builder;
import lombok.Getter;

@Getter
public class FileResponse {
    private Long fileId;
    private String fileName; // 기존 파일명임
    private String filePath; // 파일 위치

    @Builder
    public FileResponse(Photo photo) {
        this.fileId = photo.getFileId();
        this.fileName = photo.getOriginalName();
        this.filePath = photo.getFileName();
    }
}
