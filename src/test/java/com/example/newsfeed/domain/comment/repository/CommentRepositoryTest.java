package com.example.newsfeed.domain.comment.repository;

import com.example.newsfeed.domain.comment.entity.Comment;
import com.example.newsfeed.domain.feed.entity.Feed;
import com.example.newsfeed.domain.feed.repository.FeedRepository;
import com.example.newsfeed.domain.user.entity.User;
import com.example.newsfeed.domain.user.entity.UserRole;
import com.example.newsfeed.domain.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class CommentRepositoryTest {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private FeedRepository feedRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    void 댓글_저장() {
        // Given
        User user = userRepository.save(User.builder()
                .email("test@sample.com")
                .password("password1234")
                .name("테스트")
                .role(UserRole.USER)
                .build());

        Feed feed = feedRepository.save(Feed.builder()
                .user(user)
                .title("피드 제목")
                .content("내용")
                .build());

        Comment comment = Comment.builder()
                .user(user)
                .feed(feed)
                .content("댓글 테스트임")
                .build();

        // When
        Comment savedComment = commentRepository.save(comment);

        // Then
        assertThat(savedComment).isNotNull();
        assertThat(savedComment.getId()).isNotNull();
        assertThat(savedComment.getContent()).isEqualTo("댓글 테스트임");
    }

    @Test
    void 피드에_댓글_조회() {
        // Given
        User user = userRepository.save(User.builder()
                .email("test@sample.com")
                .password("password1234")
                .name("테스트")
                .role(UserRole.USER)
                .build());

        Feed feed = feedRepository.save(Feed.builder()
                .user(user)
                .title("피드 제목")
                .content("내용")
                .build());

        Comment comment1 = commentRepository.save(Comment.builder()
                .user(user)
                .feed(feed)
                .content("댓글1")
                .build());

        Comment comment2 = commentRepository.save(Comment.builder()
                .user(user)
                .feed(feed)
                .content("댓글2")
                .build());

        // When
        List<Comment> comments = commentRepository.findByFeed(feed);

        // Then
        assertThat(comments).hasSize(2);
        assertThat(comments).extracting("content").contains("댓글1", "댓글2");
    }

}