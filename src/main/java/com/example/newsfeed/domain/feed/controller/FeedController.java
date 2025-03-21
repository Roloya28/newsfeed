package com.example.newsfeed.domain.feed.controller;

import com.example.newsfeed.domain.feed.dto.request.FeedRequestDto;
import com.example.newsfeed.domain.feed.dto.response.FeedResponseDto;
import com.example.newsfeed.domain.feed.service.FeedService;
import com.example.newsfeed.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/feeds")
public class FeedController {

    private final FeedService feedService;

    @PostMapping
    public ResponseEntity<FeedResponseDto> createFeed(@SessionAttribute("LOGIN_USER") Long userId, @RequestBody FeedRequestDto dto) {
        System.out.println("세션에서 받아온 유저: " + userId);
        return ResponseEntity.ok(feedService.createFeed(userId, dto));
    }

    @GetMapping("/{feedId}")
    public ResponseEntity<FeedResponseDto> getFeedById(@PathVariable Long feedId) {
        return ResponseEntity.ok(feedService.getFeedById(feedId));
    }

    @GetMapping
    public ResponseEntity<List<FeedResponseDto>> getAllFeeds(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(feedService.getAllFeeds(page, size));
    }

    @PatchMapping("/{feedId}")
    public ResponseEntity<FeedResponseDto> updateFeed(@SessionAttribute("LOGIN_USER") User user, @PathVariable Long feedId, @RequestBody FeedRequestDto dto) {
        return ResponseEntity.ok(feedService.updateFeed(user.getId(), feedId, dto));
    }

    @DeleteMapping("/{feedId}")
    public ResponseEntity<Void> deleteFeed(@SessionAttribute("LOGIN_USER") User user, @PathVariable Long feedId) {
        feedService.deleteFeed(user.getId(), feedId);
        return ResponseEntity.noContent().build();
    }

    // 정렬 기능
    @GetMapping("/sorted")
    public ResponseEntity<List<FeedResponseDto>> getFeedsSortedByUpdatedAt() {
        return ResponseEntity.ok(feedService.getFeedsSortedByUpdatedAt());
    }

    // 기간별 검색 기능
    @GetMapping("/search")
    public ResponseEntity<List<FeedResponseDto>> getFeedsByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        return ResponseEntity.ok(feedService.getFeedsByDateRange(startDate, endDate));
    }
}

