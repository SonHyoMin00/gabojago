package com.hanium.gabojago.service;

import com.hanium.gabojago.domain.User;
import com.hanium.gabojago.dto.user.NameUpdateRequest;
import com.hanium.gabojago.dto.user.UserResponse;
import com.hanium.gabojago.handler.FileHandler;
import com.hanium.gabojago.repository.BookmarkRepository;
import com.hanium.gabojago.repository.CommentRepository;
import com.hanium.gabojago.repository.PostRepository;
import com.hanium.gabojago.util.properties.ApplicationProperties;
import com.hanium.gabojago.util.properties.JwtProperties;
import com.hanium.gabojago.oauth.kakao.KakaoOAuth2;
import com.hanium.gabojago.oauth.kakao.KakaoUserDto;
import com.hanium.gabojago.repository.UserRepository;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserService {
    private final KakaoOAuth2 kakaoOAuth2;
    private final FileHandler fileHandler;
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final BookmarkRepository bookmarkRepository;

    // authorizedCode로 가입된 사용자 조회
    @Transactional
    public User findUserByAuthorizedCode(String authorizedCode) {
        // 카카오 OAuth2 를 통해 카카오 사용자 정보 조회
        KakaoUserDto kakaoUserDto = kakaoOAuth2.getUserInfo(authorizedCode);
        String email = kakaoUserDto.getEmail();

        Optional<User> optionalUser = userRepository.findByEmail(email);

        if(optionalUser.isPresent()) return optionalUser.get();
        // 가입된 유저가 아니라면 회원가입 진행
        else {
            String name = kakaoUserDto.getName();
            Byte age = kakaoUserDto.getAge();
            String profilePhoto = kakaoUserDto.getProfilePhoto();

            User user = User.builder()
                    .email(email)
                    .name(name)
                    .age(age)
                    .profilePhoto(profilePhoto)
                    .build();

            return userRepository.save(user);
        }
    }

    @Transactional(readOnly = true)
    public User findUserByJwtToken(String token) {
        log.info("-----JWT:" + token);
        log.info("-----secretKey:" + JwtProperties.SECRETKEY);
        String email = Jwts.parser().setSigningKey(JwtProperties.SECRETKEY.getBytes())
                .parseClaimsJws(token).getBody().getSubject();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("이메일 \"" + email + "\"에 해당하는 사용자가 존재하지 않습니다."));
    }

    public Long deleteUser(User user) {
        userRepository.delete(user);
        return user.getUserId();
    }

    @Transactional
    public String updateName(User user, NameUpdateRequest nameUpdateRequest) {
        user.updateName(nameUpdateRequest.getName());
        return nameUpdateRequest.getName();
    }

    @Transactional
    public UserResponse getDefaultUserInfo(User user) {
        Long postCnt = postRepository.countByUser(user);
        Long commentCnt = commentRepository.countByUser(user);
        Long bookmarkCnt = bookmarkRepository.countByUser(user);

        return UserResponse.builder()
                .user(user)
                .postCnt(postCnt)
                .commentCnt(commentCnt)
                .bookmarkCnt(bookmarkCnt)
                .build();
    }

    @Transactional
    public String updateProfilePhoto(User user, MultipartFile multipartFile) {
        String profilePhoto = fileHandler.parseFileInfo(multipartFile, user);
        user.updateProfilePhoto(profilePhoto);
        return ApplicationProperties.HOST_IMAGE_URL + "profile/" + profilePhoto;
    }

    @Transactional
    public String deleteProfilePhoto(User user) {
        String profilePath = ApplicationProperties.PROFILE_PATH + user.getProfilePhoto();

        File file = new File(profilePath);
        if(!file.delete()) throw new IllegalStateException("프로필 사진 삭제에 실패했습니다");

        user.deleteProfile();
        return "프로필 사진 삭제 성공";
    }
}
