package com.example.newsfeed.domain.follow.controller;

import com.example.newsfeed.domain.follow.service.FollowService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/follow")
public class FollowController {

    private final FollowService followService;

    @PostMapping("/{followingId}")
    public ResponseEntity<FollowService.FollowStatus> toggleFollow(@RequestParam Long followerId, @PathVariable Long followingId) {
        FollowService.FollowStatus result = followService.toggleFollow(followerId, followingId);
        return ResponseEntity.ok(result);
    }
}
