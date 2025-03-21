package com.example.newsfeed.domain.feed.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class FeedRequestDto {

    @NotBlank
    private String title;

    @NotBlank
    private String content;
}
