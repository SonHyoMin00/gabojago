package com.hanium.gabojago.service;

import com.hanium.gabojago.domain.Great;
import com.hanium.gabojago.domain.Post;
import com.hanium.gabojago.domain.User;
import com.hanium.gabojago.repository.GreatRepository;
import com.hanium.gabojago.repository.PostRepository;
import com.hanium.gabojago.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class GreatService {
    private final GreatRepository greatRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    @Transactional
    public String insertGreat(Long id, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("잘못된 접근입니다."));

        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("id " + id + "에 해당하는 게시글이 존재하지 않습니다."));

        Optional<Great> greatOptional = greatRepository.findByUserAndPost(user, post);
        if (greatOptional.isPresent()) {
            throw new IllegalArgumentException("이미 좋아요가 표시된 게시글입니다.");
        }

        Great great = Great.builder()
                .user(user)
                .post(post)
                .build();
        greatRepository.save(great);

        post.increaseGreatCnt();
        return "좋아요 표시 성공";
    }

    @Transactional
    public String deleteGreat(Long id, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("잘못된 접근입니다."));

        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("id " + id + "에 해당하는 게시글이 존재하지 않습니다."));

        Great great = greatRepository.findByUserAndPost(user, post)
                .orElseThrow(() -> new IllegalArgumentException("좋아요 표시를 하지 않은 게시글입니다."));

        greatRepository.delete(great);

        post.decreaseGreatCnt();
        return "좋아요 취소 성공";
    }
}
