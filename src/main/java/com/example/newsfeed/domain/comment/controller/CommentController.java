package com.example.newsfeed.domain.comment.controller;

import com.example.newsfeed.domain.comment.dto.request.CommentRequestDto;
import com.example.newsfeed.domain.comment.dto.response.CommentResponseDto;
import com.example.newsfeed.domain.comment.service.CommentService;
import com.example.newsfeed.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/comments")
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/{feedId}")
    public ResponseEntity<CommentResponseDto> createComment(@SessionAttribute("LOGIN_USER") User user, @PathVariable Long feedId, @RequestBody CommentRequestDto dto) {
        return ResponseEntity.ok(commentService.createComment(user.getId(), feedId, dto));
    }

    @GetMapping("/{feedId}")
    public ResponseEntity<List<CommentResponseDto>> getCommentsByFeed(@PathVariable Long feedId) {
        return ResponseEntity.ok(commentService.getCommentsByFeed(feedId));
    }

    @PatchMapping("/{commentId}")
    public ResponseEntity<CommentResponseDto> updateComment(@SessionAttribute("LOGIN_USER") User user, @PathVariable Long commentId, @RequestBody CommentRequestDto dto) {
        return ResponseEntity.ok(commentService.updateComment(user.getId(), commentId, dto));
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(@SessionAttribute("LOGIN_USER") User user, @PathVariable Long commentId) {
        commentService.deleteComment(user.getId(), commentId);
        return ResponseEntity.noContent().build();
    }
}
