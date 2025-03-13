package com.example.newsfeed.user.dto.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class UserUpdateRequestDto {

    private String name;
    private String password;
    private String changePassword;
}
