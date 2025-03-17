package com.example.newsfeed.domain.like.service;

import com.example.newsfeed.domain.comment.entity.Comment;
import com.example.newsfeed.domain.comment.repository.CommentRepository;
import com.example.newsfeed.domain.feed.entity.Feed;
import com.example.newsfeed.domain.feed.repository.FeedRepository;
import com.example.newsfeed.domain.like.entity.Like;
import com.example.newsfeed.domain.like.repository.LikeRepository;
import com.example.newsfeed.domain.user.entity.User;
import com.example.newsfeed.domain.user.entity.UserRole;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class LikeServiceTest {

    @Mock
    private LikeRepository likeRepository;

    @Mock
    private FeedRepository feedRepository;

    @Mock
    private CommentRepository commentRepository;

    @InjectMocks
    private LikeService likeService;

    @Test
    void 게시글_좋아요_추가_취소_테스트() {
        // Given
        Long feedId = 1L;
        Long userId = 1L;

        User user = new User("test@sample.com", "password1234", "테스트", UserRole.USER);
        ReflectionTestUtils.setField(user, "id", userId);

        User otherUser = new User("test2@sample.com", "password2345", "테스트2", UserRole.USER);
        ReflectionTestUtils.setField(otherUser, "id", 2L);

        Feed feed = Feed.builder()
                .user(otherUser)
                .title("제목")
                .content("피드")
                .build();
        ReflectionTestUtils.setField(feed, "id", feedId);

        given(feedRepository.findById(feedId)).willReturn(Optional.of(feed));
        given(likeRepository.existsByUserAndFeed(user, feed)).willReturn(false);

        // When 좋아요 추가
        String result = likeService.toggleLikeOnFeed(user, feedId);

        // Then
        assertThat(result).isEqualTo("좋아요 완료");
        verify(likeRepository, times(1)).save(any(Like.class));

        // Given 좋아요 취소
        given(likeRepository.existsByUserAndFeed(user, feed)).willReturn(true);

        // When 좋아요 취소
        result = likeService.toggleLikeOnFeed(user, feedId);

        // Then
        assertThat(result).isEqualTo("좋아요 취소");
        verify(likeRepository, times(1)).existsByUserAndFeed(user, feed);
    }

    @Test
    void 댓글_좋아요_추가_최소() {
        // Given
        Long commentId = 1L;
        Long userId = 1L;

        User user = new User("test@sample.com", "password1234", "테스트", UserRole.USER);
        ReflectionTestUtils.setField(user, "id", userId);

        User otherUser = new User("test2@sample.com", "password2345", "테스트2", UserRole.USER);
        ReflectionTestUtils.setField(otherUser, "id", 2L);

        Feed feed = Feed.builder()
                .user(otherUser)
                .title("제목")
                .content("피드")
                .build();
        ReflectionTestUtils.setField(feed, "id", 1L);

        Comment comment = Comment.builder()
                .user(otherUser)
                .feed(feed)
                .content("댓글")
                .build();
        ReflectionTestUtils.setField(comment, "id", commentId);

        given(commentRepository.findById(commentId)).willReturn(Optional.of(comment));
        given(likeRepository.existsByUserAndComment(user, comment)).willReturn(false);

        // When 좋아요 추가
        String cancelResult = likeService.toggleLikeComment(user, commentId);

        // Then
        assertThat(cancelResult).isEqualTo("좋아요 취소");
        verify(likeRepository, times(1)).deleteByUserAndComment(user, comment);
    }

    @Test
    void 본인_게시글_좋아요_예외처리() {
        // Given
        Long feedId = 1L;
        Long userId = 1L;

        User user = new User("test@sample.com", "password1234", "테스트", UserRole.USER);
        ReflectionTestUtils.setField(user, "id", userId);

        Feed feed = Feed.builder()
                .user(user)
                .title("본인 게시물")
                .content("내용")
                .build();
        ReflectionTestUtils.setField(feed, "id", feedId);

        given(feedRepository.findById(feedId)).willReturn(Optional.of(feed));

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> likeService.toggleLikeOnFeed(user, feedId));
        assertThat(exception.getMessage()).isEqualTo("본인 게시물에는 좋아요 불가");
    }

    @Test
    void 본인_댓글_좋아요_예외처리() {
        // Given
        Long commentId = 1L;
        Long userId = 1L;

        User user = new User("test@sample.com", "password1234", "테스트", UserRole.USER);
        ReflectionTestUtils.setField(user, "id", userId);

        Feed feed = Feed.builder()
                .user(user)
                .title("본인 게시물")
                .content("내용")
                .build();
        ReflectionTestUtils.setField(feed, "id", 1L);

        Comment comment = Comment.builder()
                .user(user)
                .feed(feed)
                .content("댓글")
                .build();
        ReflectionTestUtils.setField(comment, "id", commentId);

        given(commentRepository.findById(commentId)).willReturn(Optional.of(comment));

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> likeService.toggleLikeComment(user, commentId));
        assertThat(exception.getMessage()).isEqualTo("본인 댓글에는 좋아요 불가");
    }
}