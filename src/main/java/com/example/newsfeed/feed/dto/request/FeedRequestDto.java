package com.example.newsfeed.feed.dto.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class FeedRequestDto {

    private String title;
    private String content;
}
