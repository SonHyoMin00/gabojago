package com.hanium.gabojago.service;

import com.hanium.gabojago.domain.Great;
import com.hanium.gabojago.domain.Post;
import com.hanium.gabojago.domain.User;
import com.hanium.gabojago.repository.GreatRepository;
import com.hanium.gabojago.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class GreatService {
    private final GreatRepository greatRepository;
    private final PostRepository postRepository;

    @Transactional
    public String insertGreat(Long id, User user) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("id " + id + "에 해당하는 게시글이 존재하지 않습니다."));
        log.info("---------post를 조회.");

        Optional<Great> greatOptional = greatRepository.findByUserAndPost(user, post);
        if (greatOptional.isPresent()) {
            throw new IllegalArgumentException("이미 좋아요가 표시된 게시글입니다.");
        }
        log.info("---------user와 post를 같이 조회해야 함.");

        Great great = Great.builder()
                .user(user)
                .post(post)
                .build();
        greatRepository.save(great);

        post.increaseGreatCnt();

        log.info("---------수정 쿼리가 나갈 예정.");
        return "좋아요 표시 성공";
    }

    @Transactional
    public String deleteGreat(Long id, User user) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("id " + id + "에 해당하는 게시글이 존재하지 않습니다."));
        log.info("---------post를 조회.");

        Great great = greatRepository.findByUserAndPost(user, post)
                .orElseThrow(() -> new IllegalArgumentException("좋아요 표시를 하지 않은 게시글입니다."));
        log.info("---------user와 post를 같이 조회해야 함.");

        if(!user.equals(great.getUser()))
            throw new IllegalArgumentException("잘못된 접근입니다.");
        log.info("---------user를 조회하는 쿼리 안나가야 함.");

        greatRepository.delete(great);

        post.decreaseGreatCnt();

        log.info("---------삭제&수정 쿼리가 나갈 예정.");
        return "좋아요 취소 성공";
    }
}
