package com.example.newsfeed.domain.like.controller;

import com.example.newsfeed.domain.like.service.LikeService;
import com.example.newsfeed.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/likes")
public class LikeController {

    private final LikeService likeService;

    @PostMapping("/feed/{feedId}")
    public ResponseEntity<String> toggleLikeFeed(@SessionAttribute("LOGIN_USER") User user, @PathVariable Long feedId) {
        return ResponseEntity.ok(likeService.toggleLikeOnFeed(user, feedId));
    }

    @PostMapping("/comment/{commentId}")
    public ResponseEntity<String> toggleLikeOnComment(@SessionAttribute("LOGIN_USER") User user, @PathVariable Long commentId) {
        return ResponseEntity.ok(likeService.toggleLikeComment(user, commentId));
    }
}
