package com.example.newsfeed.domain.follow.service;

import com.example.newsfeed.domain.follow.entity.Follow;
import com.example.newsfeed.domain.follow.repository.FollowRepository;
import com.example.newsfeed.domain.user.entity.User;
import com.example.newsfeed.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FollowService {

    private final FollowRepository followRepository;
    private final UserRepository userRepository;

    public enum FollowStatus {
        FOLLOWED, UNFOLLOWED
    }

    @Transactional(readOnly = true)
    public FollowStatus toggleFollow(Long followerId, Long followingId) {
        User follower = userRepository.findById(followerId).orElseThrow(
                () -> new RuntimeException("사용자를 찾을 수 없습니다.")
        );
        User following = userRepository.findById(followingId).orElseThrow(
                () -> new RuntimeException("사용자를 찾을 수 없습니다.")
        );
        if (follower.equals(following)) {
            throw new RuntimeException("자기 자신을 팔로우 할 수 없습니다.");
        }
        return followRepository.findByFollowerAndFollowing(follower, following)
                .map(existingFollow -> {followRepository.delete(existingFollow);
                    return FollowStatus.UNFOLLOWED;
                }).orElseGet(() -> {
                    Follow follow = Follow.builder()
                            .follower(follower)
                            .following(following)
                            .build();
                    followRepository.save(follow);
                    return FollowStatus.FOLLOWED;
                });
    }
}
