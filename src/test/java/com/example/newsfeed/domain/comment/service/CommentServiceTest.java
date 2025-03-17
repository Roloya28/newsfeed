package com.example.newsfeed.domain.comment.service;

import com.example.newsfeed.domain.comment.dto.request.CommentRequestDto;
import com.example.newsfeed.domain.comment.dto.response.CommentResponseDto;
import com.example.newsfeed.domain.comment.entity.Comment;
import com.example.newsfeed.domain.comment.repository.CommentRepository;
import com.example.newsfeed.domain.feed.entity.Feed;
import com.example.newsfeed.domain.feed.repository.FeedRepository;
import com.example.newsfeed.domain.user.entity.User;
import com.example.newsfeed.domain.user.entity.UserRole;
import com.example.newsfeed.domain.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class CommentServiceTest {

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private FeedRepository feedRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CommentService commentService;

    @Test
    void 댓글_생성() {
        // Given
        Long userId = 1L;
        Long feedId = 2L;

        CommentRequestDto requestDto = new CommentRequestDto("테스트 댓글");

        User user = new User("test@sample.com", "password1234", "테스트", UserRole.USER);

        Feed feed = Feed.builder()
                .user(user)
                .title("제목")
                .content("내용")
                .build();

        Comment comment = Comment.builder()
                .user(user)
                .feed(feed)
                .content(requestDto.getContent())
                .build();

        given(userRepository.findById(userId)).willReturn(Optional.of(user));
        given(feedRepository.findById(feedId)).willReturn(Optional.of(feed));
        given(commentRepository.save(any(Comment.class))).willReturn(comment);

        // When
        CommentResponseDto responseDto = commentService.createComment(userId, feedId, requestDto);

        // Then
        assertThat(responseDto.getContent()).isEqualTo(requestDto.getContent());
        verify(commentRepository, times(1)).save(any(Comment.class));
    }

    @Test
    void 댓글_조회() {
        // Given
        Long feedId = 1L;

        User user = new User("test@sample.com", "password1234", "테스트", UserRole.USER);

        Feed feed = Feed.builder()
                .user(user)
                .title("피드 제목")
                .content("내용")
                .build();

        Comment comment1 = Comment.builder()
                .user(user)
                .feed(feed)
                .content("댓글1")
                .build();

        Comment comment2 = Comment.builder()
                .user(user)
                .feed(feed)
                .content("댓글2")
                .build();

        given(feedRepository.findById(feedId)).willReturn(Optional.of(feed));
        given(commentRepository.findByFeed(feed)).willReturn(List.of(comment1, comment2));

        // When
        List<CommentResponseDto> responseList = commentService.getCommentsByFeed(feedId);

        // Then
        assertThat(responseList).hasSize(2);
        assertThat(responseList.get(0).getContent()).isEqualTo("댓글1");
        assertThat(responseList.get(1).getContent()).isEqualTo("댓글2");
    }

    @Test
    void 댓글_수정() {
        // Given
        Long userId = 1L;
        Long commentId = 2L;

        CommentRequestDto requestDto = new CommentRequestDto("수정한 댓글");

        User user = new User("test@sample.com", "password1234", "테스트", UserRole.USER);
        ReflectionTestUtils.setField(user, "id", 1L);

        Feed feed = Feed.builder()
                .user(user)
                .title("제목")
                .content("내용")
                .build();

        Comment comment = Comment.builder()
                .user(user)
                .feed(feed)
                .content("원래 댓글")
                .build();

        given(commentRepository.findById(commentId)).willReturn(Optional.of(comment));

        // When
        CommentResponseDto responseDto = commentService.updateComment(userId, commentId, requestDto);

        // Then
        assertThat(responseDto.getContent()).isEqualTo(requestDto.getContent());
    }

    @Test
    void 댓글_삭제() {
        // Given
        Long userId = 1L;
        Long commentId = 2L;

        User user = new User("test@sample.com", "password1234", "테스트", UserRole.USER);
        ReflectionTestUtils.setField(user, "id", 1L);

        Feed feed = Feed.builder()
                .user(user)
                .title("제목")
                .content("내용")
                .build();

        Comment comment = Comment.builder()
                .user(user)
                .feed(feed)
                .content("삭제용 댓글")
                .build();

        given(commentRepository.findById(commentId)).willReturn(Optional.of(comment));

        // When
        commentService.deleteComment(userId, commentId);

        // Then
        verify(commentRepository, times(1)).delete(comment);
    }

    @Test
    void 댓글_삭제_권한없음() {
        // Given
        Long userId = 1L;
        Long otherUserId = 2L;
        Long commentId = 2L;

        User user = new User("test@sample.com", "password1234", "테스트", UserRole.USER);
        ReflectionTestUtils.setField(user, "id", 1L);

        User otherUser = new User("test2@sample.com", "password2345", "유저2", UserRole.USER);
        ReflectionTestUtils.setField(otherUser, "id", 2L);

        Feed feed = Feed.builder()
                .user(user)
                .title("제목")
                .content("내용")
                .build();

        Comment comment = Comment.builder()
                .user(user)
                .feed(feed)
                .content("삭제용 댓글")
                .build();

        given(commentRepository.findById(commentId)).willReturn(Optional.of(comment));

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> commentService.deleteComment(otherUserId, commentId));
        assertThat(exception.getMessage()).isEqualTo("삭제할 권한이 없습니다.");
    }
}