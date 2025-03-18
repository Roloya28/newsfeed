package com.example.newsfeed.domain.like.repository;

import com.example.newsfeed.domain.comment.entity.Comment;
import com.example.newsfeed.domain.comment.repository.CommentRepository;
import com.example.newsfeed.domain.feed.entity.Feed;
import com.example.newsfeed.domain.feed.repository.FeedRepository;
import com.example.newsfeed.domain.like.entity.Like;
import com.example.newsfeed.domain.user.entity.User;
import com.example.newsfeed.domain.user.entity.UserRole;
import com.example.newsfeed.domain.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
class LikeRepositoryTest {

    @Autowired
    private LikeRepository likeRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FeedRepository feedRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Test
    void 게시글_좋아요_확인() {
        // Given
        User user = userRepository.save(new User("test@sample.com", "password1234", "테스트", UserRole.USER));
        Feed feed = feedRepository.save(new Feed(user, "제목", "내용", user.getId()));
        likeRepository.save(new Like(user, feed, null));

        // When
        boolean exists = likeRepository.existsByUserAndFeed(user, feed);

        // Then
        assertThat(exists).isTrue();
    }

    @Test
    void 댓글_좋아요_확인() {
        // Given
        User user = userRepository.save(new User("test@sample.com", "password1234", "테스트", UserRole.USER));
        Feed feed = feedRepository.save(new Feed(user, "제목", "내용", user.getId()));
        Comment comment = commentRepository.save(new Comment(user, feed, "댓글"));
        likeRepository.save(new Like(user, null, comment));

        // When
        boolean exists = likeRepository.existsByUserAndComment(user, comment);

        // Then
        assertThat(exists).isTrue();
    }

    @Test
    void 게시글_좋아요_취소() {
        // Given
        User user = userRepository.save(new User("test@sample.com", "password1234", "테스트", UserRole.USER));
        Feed feed = feedRepository.save(new Feed(user, "제목", "내용", user.getId()));
        Like like = likeRepository.save(new Like(user, feed, null));

        // When
        likeRepository.deleteByUserAndFeed(user, feed);
        boolean exists = likeRepository.existsByUserAndFeed(user, feed);

        // Then
        assertThat(exists).isFalse();
    }

    @Test
    void 댓글_좋아요_취소() {
        // Given
        User user = userRepository.save(new User("test@sample.com", "password1234", "테스트", UserRole.USER));
        Feed feed = feedRepository.save(new Feed(user, "제목", "내용", user.getId()));
        Comment comment = commentRepository.save(new Comment(user, feed, "댓글"));
        Like like = likeRepository.save(new Like(user, null, comment));

        // When
        likeRepository.deleteByUserAndComment(user, comment);
        boolean exists = likeRepository.existsByUserAndComment(user, comment);

        // Then
        assertThat(exists).isFalse();
    }
}
