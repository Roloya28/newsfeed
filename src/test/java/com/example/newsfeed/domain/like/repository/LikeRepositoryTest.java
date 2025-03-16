package com.example.newsfeed.domain.like.repository;

import com.example.newsfeed.domain.comment.repository.CommentRepository;
import com.example.newsfeed.domain.feed.entity.Feed;
import com.example.newsfeed.domain.feed.repository.FeedRepository;
import com.example.newsfeed.domain.like.entity.Like;
import com.example.newsfeed.domain.user.entity.User;
import com.example.newsfeed.domain.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

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
    void 게시물_좋아요_여부_확인() {
        // Given
        User user = userRepository.save(new User("test@sample.com", "password1234", "test"));
        Feed feed = feedRepository.save(Feed.builder()
                .user(user)
                .title("제목")
                .content("내용")
                .build());
        likeRepository.save(new Like(user, feed, null));

        // When
        boolean exists = likeRepository.existsByUserAndFeed(user, feed);

        // Then
        assertThat(exists).isTrue();
    }

}