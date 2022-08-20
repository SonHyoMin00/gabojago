package com.hanium.gabojago.dto.photo;

import com.hanium.gabojago.domain.Photo;
import com.hanium.gabojago.util.properties.ApplicationProperties;
import lombok.Builder;
import lombok.Getter;

@Getter
public class PhotoResponse {
    private Long fileId;
    private String fileName; // 기존 파일명임
    private String filePath; // 파일 위치

    @Builder
    public PhotoResponse(Photo photo) {
        this.fileId = photo.getFileId();
        this.fileName = photo.getOriginalName();
        this.filePath = ApplicationProperties.HOST_IMAGE_URL + "post/" + photo.getFileName();
    }
}
