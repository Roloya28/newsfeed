package com.example.newsfeed.domain.comment.dto.response;

import com.example.newsfeed.domain.comment.entity.Comment;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
@NoArgsConstructor(force = true)
public class CommentResponseDto {

    private final Long id;
    private final Long userId;
    private final Long feedId;
    private final String content;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public CommentResponseDto(Comment comment) {
        this.id = comment.getId();
        this.userId = comment.getUser().getId();
        this.feedId = comment.getFeed().getId();
        this.content = comment.getContent();
        this.createdAt = comment.getCreatedAt();
        this.updatedAt = comment.getUpdatedAt();
    }
}
