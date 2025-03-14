package com.example.newsfeed.domain.feed.dto.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class FeedResponseDto {

    private final Long id;
    private final String title;
    private final String content;
    private final Long userId;
}
