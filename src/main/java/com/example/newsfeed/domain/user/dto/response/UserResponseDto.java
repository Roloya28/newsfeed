package com.example.newsfeed.domain.user.dto.response;

import com.example.newsfeed.domain.user.entity.UserRole;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class UserResponseDto {

    private final Long userId;
    private final String name;
    private final Long followersCount;
    private final Long followingsCount;
    private final UserRole role;
}
