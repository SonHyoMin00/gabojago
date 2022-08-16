package com.hanium.gabojago.service;

import com.hanium.gabojago.domain.Comment;
import com.hanium.gabojago.domain.Post;
import com.hanium.gabojago.domain.User;
import com.hanium.gabojago.dto.comment.CommentPageResponse;
import com.hanium.gabojago.dto.comment.CommentRequest;
import com.hanium.gabojago.dto.comment.CommentResponse;
import com.hanium.gabojago.repository.CommentRepository;
import com.hanium.gabojago.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class CommentService {
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    public CommentPageResponse getComments(Long id, int page, int size) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("id " + id + "에 해당하는 게시글이 존재하지 않습니다."));

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Comment> comments = commentRepository.findAllByPostWithUser(post, pageable);

        //총 페이지 수
        int totalPages = comments.getTotalPages();

        //spotResponses DTO로 변환
        List<CommentResponse> commentResponses = comments.getContent()
                .stream().map(CommentResponse::new).collect(Collectors.toList());

        // 총 페이지 수 추가하여 반환
        return CommentPageResponse.builder()
                .totalPages(totalPages)
                .comments(commentResponses)
                .build();
    }

    @Transactional
    public Long makeComment(Long id, CommentRequest commentRequest, User user) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("id " + id + "에 해당하는 게시글이 존재하지 않습니다."));

        Comment comment = Comment.builder()
                    .user(user)
                    .post(post)
                    .context(commentRequest.getContext())
                    .build();

        commentRepository.save(comment);
        return comment.getCommentId();
    }

    @Transactional
    public Long updateComment(Long id, CommentRequest commentRequest, User user) {
        Comment comment = commentRepository.findByIdWithUser(id)
                .orElseThrow(()-> new IllegalArgumentException("id " + id + "에 해당하는 댓글이 존재하지 않습니다."));

        if(!user.equals(comment.getUser())) {
            throw new IllegalArgumentException("잘못된 접근입니다.");
        }

        comment.updateContext(commentRequest.getContext());
        return id;
    }

    @Transactional
    public Long deleteComment(Long id, User user) {
        Comment comment = commentRepository.findByIdWithUser(id)
                .orElseThrow(()-> new IllegalArgumentException("id " + id + "에 해당하는 댓글이 존재하지 않습니다."));

        if(!user.equals(comment.getUser())) {
            throw new IllegalArgumentException("잘못된 접근입니다.");
        }

        commentRepository.delete(comment);
        return id;
    }
}
