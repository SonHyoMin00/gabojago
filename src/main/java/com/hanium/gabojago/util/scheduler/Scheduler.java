package com.hanium.gabojago.util.scheduler;

import com.hanium.gabojago.domain.Post;
import com.hanium.gabojago.dto.post.PostResponse;
import com.hanium.gabojago.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class Scheduler {
    private final PostRepository postRepository;
    private List<PostResponse> postResponses = null;

    @Scheduled(cron = "0 0 0 1 * ?")
    public void initTop3Posts() {
        List<Post> posts = findTop3Posts();
        postResponses = posts.stream().map(PostResponse::new).collect(Collectors.toList());
    }

    public List<Post> findTop3Posts() {
        int year = LocalDate.now().getYear();
        int preMonth = LocalDate.now().getMonthValue() - 1;
        if(preMonth == 0) {
            preMonth = 12;
            year--;
        }
        LocalDate startDate = LocalDate.of(year, preMonth, 1);
        LocalDateTime start = LocalDateTime.of(startDate, LocalTime.MIN);
        LocalDate endDate = startDate.plusDays(startDate.lengthOfMonth() - 1);
        LocalDateTime end = LocalDateTime.of(endDate, LocalTime.MAX);

        return postRepository
                .findTop3ByCreatedAtBetweenOrderByGreatCntDescViewCntDescCreatedAtAsc(start, end);
    }

    public List<PostResponse> getPostResponses() {
        return postResponses;
    }

    public void setPostResponses(List<PostResponse> postResponses) {
        this.postResponses = postResponses;
    }
}
