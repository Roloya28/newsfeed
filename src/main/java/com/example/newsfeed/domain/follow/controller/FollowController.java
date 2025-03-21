package com.example.newsfeed.domain.follow.controller;

import com.example.newsfeed.domain.follow.service.FollowService;
import com.example.newsfeed.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/follow")
public class FollowController {

    private FollowService followService;

    @PostMapping("/{followingId}")
    public ResponseEntity<String> toggleFollow(@SessionAttribute("LOGIN_USER") User user, @PathVariable Long followingId) {
        FollowService.FollowStatus result = followService.toggleFollow(user.getId(), followingId);
        return ResponseEntity.ok(result.name());
    }
}
