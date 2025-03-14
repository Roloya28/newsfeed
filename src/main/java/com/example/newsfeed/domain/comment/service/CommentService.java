package com.example.newsfeed.domain.comment.service;

import com.example.newsfeed.domain.comment.dto.request.CommentRequestDto;
import com.example.newsfeed.domain.comment.dto.response.CommentResponseDto;
import com.example.newsfeed.domain.comment.entity.Comment;
import com.example.newsfeed.domain.comment.repository.CommentRepository;
import com.example.newsfeed.domain.feed.entity.Feed;
import com.example.newsfeed.domain.feed.repository.FeedRepository;
import com.example.newsfeed.domain.user.entity.User;
import com.example.newsfeed.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final FeedRepository feedRepository;
    private final UserRepository userRepository;

    @Transactional
    public CommentResponseDto createComment(Long userId, Long feedId, CommentRequestDto dto) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new RuntimeException("사용자를 찾을 수 없습니다.")
        );
        Feed feed = feedRepository.findById(feedId).orElseThrow(
                () -> new RuntimeException("게스글을 찾을 수 없습니다.")
        );
        Comment comment = Comment.builder()
                .user(user)
                .feed(feed)
                .content(dto.getContent())
                .build();
        commentRepository.save(comment);
        return new CommentResponseDto(comment);
    }

    @Transactional(readOnly = true)
    public List<CommentResponseDto> getCommentsByFeed(Long feedId) {
        Feed feed = feedRepository.findById(feedId).orElseThrow(
                () -> new RuntimeException("게시글을 찾을 수 없습니다.")
        );
        return commentRepository.findByFeed(feed).stream()
                .map(CommentResponseDto::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public CommentResponseDto updateComment(Long userId, Long commentId, CommentRequestDto dto) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(
                () -> new RuntimeException("댓글을 찾을 수 없습니다.")
        );
        if (!comment.getUser().getId().equals(userId)) {
            throw new RuntimeException("수정할 권한이 없습니다.");
        }
        comment.updateContent(dto.getContent());
        return new CommentResponseDto(comment);
    }

    @Transactional
    public void deleteComment(Long userId, Long commentId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(
                () -> new RuntimeException("댓글을 찾을 수 없습니다.")
        );
        boolean isOwner = comment.getUser().getId().equals(userId);
        boolean isPostOwner = comment.getFeed().getUser().getId().equals(userId);

        if (!isOwner && !isPostOwner) {
            throw new RuntimeException("삭제할 권한이 없습니다.");
        }
        commentRepository.delete(comment);
    }
}
