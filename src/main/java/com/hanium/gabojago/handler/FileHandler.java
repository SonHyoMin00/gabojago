package com.hanium.gabojago.handler;

import com.hanium.gabojago.domain.Photo;

import com.hanium.gabojago.domain.Post;
import lombok.extern.slf4j.Slf4j;
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

            // 프로젝트 디렉터리 내의 저장을 위한 절대 경로 설정
            // 경로 구분자 File.separator 사용
            String absolutePath = new File("").getAbsolutePath() + File.separator + File.separator;

            // 파일을 저장할 세부 경로 지정
            String path = "images" + File.separator + "post" + File.separator + current_date;
            File file = new File(path);

            // 디렉터리가 존재하지 않을 경우
            if(!file.exists()) {
                boolean wasSuccessful = file.mkdirs();

                // 디렉터리 생성에 실패했을 경우
                if(!wasSuccessful)
                    System.out.println("Error: file was not successful");
            }

            // 다중 파일 처리
            for(MultipartFile multipartFile : multipartFiles) {

                // 파일의 확장자 추출
                String originalFileExtension;
                String contentType = multipartFile.getContentType();

                // 확장자명이 존재하지 않을 경우 에러
                if(ObjectUtils.isEmpty(contentType)) {
                    throw new IllegalArgumentException("사진 파일만 업로드해주세요.");
                }
                else {  // 확장자가 jpeg, png인 파일들만 받아서 처리
                    if(contentType.contains("image/jpeg"))
                        originalFileExtension = ".jpg";
                    else if(contentType.contains("image/png"))
                        originalFileExtension = ".png";
                    else  // 다른 확장자일 경우 처리 x
                        throw new IllegalArgumentException("사진 파일만 업로드해주세요.");
                }

                // 파일명 중복 피하고자 나노초까지 얻어와 지정
                String new_file_name = System.nanoTime() + originalFileExtension;

                // 파일 DTO 이용하여 Photo 엔티티 생성
                Photo photo = Photo.builder()
                        .post(post)
                        .fileName(absolutePath + path + File.separator + new_file_name)
                        .originalName(multipartFile.getOriginalFilename())
                        .fileSize(multipartFile.getSize()).build();

                // 생성 후 리스트에 추가
                fileList.add(photo);

                // 업로드 한 파일 데이터를 지정한 파일에 저장
                file = new File(absolutePath + path + File.separator + new_file_name);
                log.info("저장될 파일 경로: " + absolutePath + path + File.separator + new_file_name);
                try {
                    multipartFile.transferTo(file);
                } catch (Exception e) {
                    throw new IllegalArgumentException(e);
                }
            }
        }

        return fileList;
    }

    public String parseFileInfo(MultipartFile multipartFile) {
        String fullFileName = null;

        // 파일이 1개 첨부된 경우
        if(multipartFile != null) {
            // 파일명을 업로드 한 날짜로 변환하여 저장
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter dateTimeFormatter =
                    DateTimeFormatter.ofPattern("yyyyMMdd");
            String current_date = now.format(dateTimeFormatter);

            // 프로젝트 디렉터리 내의 저장을 위한 절대 경로 설정
            // 경로 구분자 File.separator 사용
            String absolutePath = new File("").getAbsolutePath() + File.separator + File.separator;

            // 파일을 저장할 세부 경로 지정
            String path = "images" + File.separator + "profile" + File.separator + current_date;
            File file = new File(path);

            // 디렉터리가 존재하지 않을 경우
            if(!file.exists()) {
                boolean wasSuccessful = file.mkdirs();

                // 디렉터리 생성에 실패했을 경우
                if(!wasSuccessful)
                    System.out.println("Error: file was not successful");
            }

            // 파일의 확장자 추출
            String originalFileExtension;
            String contentType = multipartFile.getContentType();

            // 확장자명이 존재하지 않을 경우 에러
            if(ObjectUtils.isEmpty(contentType)) {
                throw new IllegalArgumentException("사진 파일만 업로드해주세요.");
            }
            else {  // 확장자가 jpeg, png인 파일들만 받아서 처리
                if(contentType.contains("image/jpeg"))
                    originalFileExtension = ".jpg";
                else if(contentType.contains("image/png"))
                    originalFileExtension = ".png";
                else  // 다른 확장자일 경우 처리 x
                    throw new IllegalArgumentException("사진 파일만 업로드해주세요.");
            }

            // 파일명 중복 피하고자 나노초까지 얻어와 지정
            String new_file_name = System.nanoTime() + originalFileExtension;

            // 업로드 한 파일 데이터를 지정한 파일에 저장
            fullFileName = absolutePath + path + File.separator + new_file_name;
            file = new File(fullFileName);
            log.info("저장될 파일 경로: " + fullFileName);
            try {
                multipartFile.transferTo(file);
            } catch (Exception e) {
                throw new IllegalArgumentException(e);
            }
        }

        if(fullFileName == null) throw new IllegalArgumentException("파일명이 지정되지 않았습니다.");
        return fullFileName;
    }
}
