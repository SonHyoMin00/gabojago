package com.hanium.gabojago.util.handler;

import com.hanium.gabojago.domain.Photo;

import com.hanium.gabojago.domain.Post;
import com.hanium.gabojago.domain.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class FileHandler {
    @Value("${images.path.post}")
    private String postPath;

    @Value("${images.path.profile}")
    private String profilePath;

    public List<Photo> parseFileInfo(List<MultipartFile> multipartFiles, Post post) {
        // 반환할 파일 리스트
        List<Photo> fileList = new ArrayList<>();

        // 파일이 첨부된 경우
        if(!CollectionUtils.isEmpty(multipartFiles)) {
            // 파일명을 업로드 한 날짜로 변환하여 저장
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter dateTimeFormatter =
                    DateTimeFormatter.ofPattern("yyyyMMdd");
            String current_date = now.format(dateTimeFormatter);

            // 파일을 저장할 세부 경로 지정
            String path = postPath + File.separator + current_date;
            File file = new File(path);

            // 디렉터리가 존재하지 않을 경우
            if(!file.exists()) {
                // 디렉터리 생성
                if(!file.mkdirs())
                    System.out.println("Error: file was not successful");
            }

            // 다중 파일 처리
            for(MultipartFile multipartFile : multipartFiles) {

                // 이미지 파일인지 검증
                validateContentType(multipartFile);

                // 유니크한 파일명 설정
                String new_file_name = System.nanoTime() + multipartFile.getOriginalFilename();

                // Photo 엔티티 생성
                Photo photo = Photo.builder()
                        .post(post)
                        .fileName(current_date + "/" + new_file_name)
                        .originalName(multipartFile.getOriginalFilename())
                        .fileSize(multipartFile.getSize()).build();

                // 생성 후 리스트에 추가
                fileList.add(photo);

                // 업로드 한 파일 데이터를 지정한 파일에 저장
                file = new File(path + File.separator + new_file_name);
                log.info("저장될 파일 경로: " + path + "/" + new_file_name);
                try {
                    multipartFile.transferTo(file);
                } catch (Exception e) {
                    throw new IllegalArgumentException(e);
                }
            }
        }

        return fileList;
    }

    public String parseFileInfo(MultipartFile multipartFile, User user) {
        // 파일이 첨부되지 않은 경우
        if (multipartFile == null)
            throw new IllegalArgumentException("파일이 첨부되지 않았습니다.");

        // 확장자명이 존재하지 않을 경우 에러
        validateContentType(multipartFile);

        // 프로필 사진이 설정되어 있다면 삭제
       if(user.getProfilePhoto() != null) {
            File originalFilePath = new File(profilePath + user.getProfilePhoto());

           if (originalFilePath.exists()) {
               if(!originalFilePath.delete()) log.error("기존에 설정된 프로필 사진 삭제에 실패했습니다.");
           }
        }

        // 파일명 저장 경로 및 파일명 설정
        String fileName = user.getUserId() + "_" + multipartFile.getOriginalFilename();
        String filePath = profilePath + fileName;

        // 업로드 한 파일 데이터를 지정한 파일에 저장
        File file = new File(filePath);
        log.info("프로필 사진 저장 경로: " + filePath);
        try {
            multipartFile.transferTo(file);
        } catch (Exception e) {
            throw new IllegalArgumentException("프로필 사진 변경에 실패했습니다: " + e);
        }

        return fileName;
    }

    private void validateContentType(MultipartFile multipartFile) {
        // 파일의 확장자 추출
        String contentType = multipartFile.getContentType();

        if(ObjectUtils.isEmpty(contentType)) {
            throw new IllegalArgumentException("사진 파일만 업로드해주세요.");
        }
        // 확장자가 jpeg, png인 파일들만 받아서 처리
        else if(!contentType.contains("image/jpeg") && !contentType.contains("image/png")) {

            throw new IllegalArgumentException("사진 파일만 업로드해주세요.");
        }
    }
}
