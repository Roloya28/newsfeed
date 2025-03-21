package com.example.newsfeed.domain.auth.controller;

import com.example.newsfeed.domain.auth.dto.request.AuthRequestDto;
import com.example.newsfeed.domain.auth.dto.response.AuthResponseDto;
import com.example.newsfeed.domain.auth.service.AuthService;
import com.example.newsfeed.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<AuthResponseDto> signup(@RequestBody AuthRequestDto dto) {
        User user = authService.signup(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(new AuthResponseDto(user));
    }

    // Spring 이 ResponseStatusException 을 감지 -> 400 상태 코드와 메세지를 Json 응답으로 반환
    @ExceptionHandler(ResponseStatusException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleResponseStatusException(ResponseStatusException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("message", ex.getReason());
        return error;
    }
}
