package com.example.newsfeed.domain.follow.repository;

import com.example.newsfeed.domain.follow.entity.Follow;
import com.example.newsfeed.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FollowRepository extends JpaRepository<Follow, Long> {

    Optional<Follow> findByFollowerAndFollowing(User follower, User following);
}
