package com.example.newsfeed.domain.like.service;

import com.example.newsfeed.domain.comment.entity.Comment;
import com.example.newsfeed.domain.comment.repository.CommentRepository;
import com.example.newsfeed.domain.feed.entity.Feed;
import com.example.newsfeed.domain.feed.repository.FeedRepository;
import com.example.newsfeed.domain.like.entity.Like;
import com.example.newsfeed.domain.like.repository.LikeRepository;
import com.example.newsfeed.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LikeService {

    private final LikeRepository likeRepository;
    private final FeedRepository feedRepository;
    private final CommentRepository commentRepository;

    @Transactional
    public String toggleLikeOnFeed(User user, Long feedId) {
        Feed feed = feedRepository.findById(feedId).orElseThrow(
                () -> new RuntimeException("게시글을 찾을 수 없습니다.")
        );
        if (feed.getUser().equals(user)) {
            throw new RuntimeException("본인 게시물에는 좋아요를 할 수 없습니다.");
        }
        boolean alreadyLiked = likeRepository.existsByUserAndFeed(user, feed);

        if (alreadyLiked) {
            likeRepository.deleteByUserAndFeed(user, feed);
            return "좋아요 취소";
        } else {
            likeRepository.save(Like.builder().user(user).feed(feed).build());
            return "좋아요 완료";
        }
    }

    @Transactional
    public String toggleLikeComment(User user, Long commentId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(
                () -> new RuntimeException("댓글을 찾을 수 없습니다.")
        );
        if (comment.getUser().equals(user)) {
            throw new RuntimeException("본인 댓글에는 좋아요를 할 수 없습니다.");
        }

        boolean alreadyLiked = likeRepository.existsByUserAndComment(user, comment);

        if (alreadyLiked) {
            likeRepository.deleteByUserAndComment(user, comment);
            return "좋아요 취소";
        } else {
            likeRepository.save(Like.builder().user(user).comment(comment).build());
            return "좋아요 완료";
        }
    }
}
