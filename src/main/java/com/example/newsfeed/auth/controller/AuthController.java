package com.example.newsfeed.auth.controller;

import com.example.newsfeed.auth.dto.request.AuthRequestDto;
import com.example.newsfeed.auth.dto.response.AuthResponseDto;
import com.example.newsfeed.auth.service.AuthService;
import com.example.newsfeed.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/signup")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<AuthResponseDto> signup(@RequestBody AuthRequestDto dto) {
        User user = authService.signup(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(new AuthResponseDto(user));
    }
}
