package com.example.newsfeed.domain.follow.repository;

import com.example.newsfeed.domain.follow.entity.Follow;
import com.example.newsfeed.domain.user.entity.User;
import com.example.newsfeed.domain.user.entity.UserRole;
import com.example.newsfeed.domain.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
class FollowRepositoryTest {

    @Autowired
    private FollowRepository followRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    void 팔로우_저장_조회() {
        // Given
        User follower = userRepository.save(new User("follower@sample.com", "password1234", "follower", UserRole.USER));
        User following = userRepository.save(new User("following@sample.com", "password2345", "following", UserRole.USER));

        Follow follow = followRepository.save(new Follow(follower, following));

        // When
        Optional<Follow> foundFollow = followRepository.findByFollowerAndFollowing(follower, following);

        // Then
        assertThat(foundFollow).isPresent();
        assertThat(foundFollow.get().getFollower().getId()).isEqualTo(follower.getId());
        assertThat(foundFollow.get().getFollowing().getId()).isEqualTo(following.getId());
    }
}