package com.hanium.gabojago.service;

import com.hanium.gabojago.domain.Comment;
import com.hanium.gabojago.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CommentService {
    private final CommentRepository commentRepository;

    public Long makeComment(String email) {
        return null;
    }

    public Long updateComment(Long id, String email) {
        Comment comment = commentRepository.findByIdWithUser(id)
                .orElseThrow(()-> new IllegalArgumentException("id " + id + "에 해당하는 댓글이 존재하지 않습니다."));

        if(!email.equals(comment.getUser().getEmail())) {
            throw new IllegalArgumentException("잘못된 접근입니다.");
        }

        return id;
    }

    public Long deleteComment(Long id, String email) {
        Comment comment = commentRepository.findByIdWithUser(id)
                .orElseThrow(()-> new IllegalArgumentException("id " + id + "에 해당하는 댓글이 존재하지 않습니다."));

        if(!email.equals(comment.getUser().getEmail())) {
            throw new IllegalArgumentException("잘못된 접근입니다.");
        }

        commentRepository.delete(comment);
        return id;
    }
}
