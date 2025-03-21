package com.example.newsfeed.domain.follow.service;

import com.example.newsfeed.domain.follow.entity.Follow;
import com.example.newsfeed.domain.follow.repository.FollowRepository;
import com.example.newsfeed.domain.user.entity.User;
import com.example.newsfeed.domain.user.entity.UserRole;
import com.example.newsfeed.domain.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class FollowServiceTest {

    @Mock
    private FollowRepository followRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private FollowService followService;

    @Test
    void 다른_사용자를_팔로우하면_FOLLWED_상태를_반환() {
        // Given
        User follower = new User("follower@sample.com", "password1234", "follower", UserRole.USER);
        User following = new User("following@sample.com", "password2345", "following", UserRole.USER);

        // Reflection 을 사용하여 id 값을 강제로 설정
        ReflectionTestUtils.setField(follower, "id", 1L);
        ReflectionTestUtils.setField(following, "id", 2L);

        given(userRepository.findById(1L)).willReturn(Optional.of(follower));
        given(userRepository.findById(2L)).willReturn(Optional.of(following));
        given(followRepository.findByFollowerAndFollowing(follower, following)).willReturn(Optional.empty());

        // When
        FollowService.FollowStatus status = followService.toggleFollow(follower.getId(), following.getId());

        // Then
        assertThat(status).isEqualTo(FollowService.FollowStatus.FOLLOWED);
        verify(followRepository).save(any(Follow.class));
    }

    @Test
    void 이미_팔로우_상태에서_다시_팔로우시_UNFOLLOWED_상태를_반환() {
        // Given
        User follower = new User("follower@sample.com", "password1234", "follower", UserRole.USER);
        User following = new User("following@sample.com", "password2345", "following", UserRole.USER);
        ReflectionTestUtils.setField(follower, "id", 1L);
        ReflectionTestUtils.setField(following, "id", 2L);

        Follow follow = Follow.builder()
                .follower(follower)
                .following(following)
                .build();

        given(userRepository.findById(1L)).willReturn(Optional.of(follower));
        given(userRepository.findById(2L)).willReturn(Optional.of(following));
        given(followRepository.findByFollowerAndFollowing(follower, following)).willReturn(Optional.of(follow));

        // When
        FollowService.FollowStatus status = followService.toggleFollow(follower.getId(), following.getId());

        // Then
        assertThat(status).isEqualTo(FollowService.FollowStatus.UNFOLLOWED);
        verify(followRepository).delete(follow);
    }

    @Test
    void 자신을_팔로우하면_예외처리() {
        // Given
        User user = new User("test@sample.com", "password1234", "나", UserRole.USER);
        ReflectionTestUtils.setField(user, "id", 1L);

        given(userRepository.findById(1L)).willReturn(Optional.of(user));

        // When & Then
        assertThatThrownBy(() -> followService.toggleFollow(user.getId(), user.getId()))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("자기 자신을 팔로우 할 수 없습니다.");
    }

    @Test
    void 없는_사용자를_팔로우할경우_예외처리() {
        // Given
        User follower = new User("test@sample.com", "password1234", "팔로워", UserRole.USER);
        ReflectionTestUtils.setField(follower, "id", 1L);

        given(userRepository.findById(1L)).willReturn(Optional.of(follower));
        given(userRepository.findById(2L)).willReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> followService.toggleFollow(follower.getId(), 2L))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("사용자를 찾을 수 없습니다.");
    }
}