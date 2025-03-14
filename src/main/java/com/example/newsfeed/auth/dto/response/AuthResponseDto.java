package com.example.newsfeed.auth.dto.response;

import com.example.newsfeed.user.entity.User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class AuthResponseDto {

    private final Long id;
    private final String email;
    private final String name;

    public AuthResponseDto(User user) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.name = user.getName();
    }
}
