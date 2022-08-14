package com.hanium.gabojago;

import com.hanium.gabojago.domain.Post;
import com.hanium.gabojago.domain.User;
import com.hanium.gabojago.repository.PostRepository;
import com.hanium.gabojago.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class BaseTimeEntityTest {
    @Autowired private UserRepository userRepository;
    @Autowired private PostRepository postRepository;

    @BeforeEach
    void setUser() {
        User user = User.builder()
                .email("testUser@gabojago.com")
                .name("testUser")
                .age((byte) 20)
                .build();

        userRepository.save(user);
    }

    @Test
    @DisplayName("User클래스에서 JpaAudigint 테스트")
    void UserTest() {
        // when
        User found = userRepository.findByEmail("testUser@gabojago.com").get();

        // then
        assertThat(found.getName()).isEqualTo("testUser");
        assertThat(found.getCreatedAt()).isNotNull();
        assertThat(found.getModifiedAt()).isNotNull();
    }

    @Test
    @DisplayName("Post클래스에서 JpaAudigint 테스트")
    void PostTest() {
        // given
        User found = userRepository.findByEmail("testUser@gabojago.com").get();
        Post post = Post.builder()
                .user(found)
                .title("testPost")
                .context("testContext")
                .build();

        // when
        Post savedPost = postRepository.save(post);

        // then
        assertThat(savedPost.getTitle()).isEqualTo("testPost");
        assertThat(savedPost.getContext()).isEqualTo("testContext");
        assertThat(savedPost.getCreatedAt()).isNotNull();
        assertThat(savedPost.getModifiedAt()).isNotNull();
    }
}
