package com.example.newsfeed.domain.like.repository;

import com.example.newsfeed.domain.comment.entity.Comment;
import com.example.newsfeed.domain.feed.entity.Feed;
import com.example.newsfeed.domain.user.entity.User;
import com.example.newsfeed.domain.user.entity.UserRole;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LikeRepositoryTest {

    @Mock
    private LikeRepository likeRepository;

    @Test
    void 유저가_게시글에_좋아요_눌럿는지_확인() {
        // Given
        User user = new User("test@sample.com", "password1234", "테스트", UserRole.USER    );
        ReflectionTestUtils.setField(user, "id", 1L);

        Feed feed = Feed.builder()
                .user(user)
                .title("피드 제목")
                .content("내용")
                .build();
        ReflectionTestUtils.setField(feed, "id", 1L);

        given(likeRepository.existsByUserAndFeed(user, feed)).willReturn(true);

        // When
        boolean result = likeRepository.existsByUserAndFeed(user, feed);

        // Then
        assertThat(result).isTrue();
        verify(likeRepository, times(1)).existsByUserAndFeed(user, feed);
    }

    @Test
    void 유저가_댓글에_좋아요_눌럿는지_확인() {
        // Given
        User user = new User("test@sample.com", "password1234", "테스트", UserRole.USER);
        ReflectionTestUtils.setField(user, "id", 1L);

        Feed feed = Feed.builder()
                .user(user)
                .title("피드 제목")
                .content("내용")
                .build();
        ReflectionTestUtils.setField(feed, "id", 1L);

        Comment comment = Comment.builder()
                .user(user)
                .feed(feed)
                .content("댓글")
                .build();
        ReflectionTestUtils.setField(comment, "id", 1L);

        given(likeRepository.existsByUserAndComment(user, comment)).willReturn(true);

        // When
        boolean result = likeRepository.existsByUserAndComment(user, comment);

        // Then
        assertThat(result).isTrue();
        verify(likeRepository, times(1)).existsByUserAndComment(user, comment);
    }

    @Test
    void 유저가_게시글에_좋아요_취소() {
        // Given
        User user = new User("test@sample.com", "password1234", "테스트", UserRole.USER);
        ReflectionTestUtils.setField(user, "id", 1L);

        Feed feed = Feed.builder()
                .user(user)
                .title("피드 제목")
                .content("내용")
                .build();
        ReflectionTestUtils.setField(feed, "id", 1L);

        doNothing().when(likeRepository).deleteByUserAndFeed(user, feed);

        // When
        likeRepository.deleteByUserAndFeed(user, feed);

        // Then
        verify(likeRepository, times(1)).deleteByUserAndFeed(user, feed);
    }

    @Test
    void 유저가_특정댓글에_좋아요_취소() {
        // Given
        User user = new User("test@sample.com", "password1234", "테스트", UserRole.USER);
        ReflectionTestUtils.setField(user, "id", 1L);

        Feed feed = Feed.builder()
                .user(user)
                .title("피드 제목")
                .content("내용")
                .build();
        ReflectionTestUtils.setField(feed, "id", 1L);

        Comment comment = Comment.builder()
                .user(user)
                .feed(feed)
                .content("댓글")
                .build();
        ReflectionTestUtils.setField(comment, "id", 1L);

        doNothing().when(likeRepository).deleteByUserAndComment(user, comment);

        // When
        likeRepository.deleteByUserAndComment(user, comment);

        // Then
        verify(likeRepository, times(1)).deleteByUserAndComment(user, comment);
    }
}
