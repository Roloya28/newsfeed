package com.example.newsfeed.domain.user.controller;

import com.example.newsfeed.domain.user.dto.request.UserUpdateRequestDto;
import com.example.newsfeed.domain.user.dto.response.UserResponseDto;
import com.example.newsfeed.domain.user.entity.User;
import com.example.newsfeed.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @GetMapping("/{userId}")
    public ResponseEntity<UserResponseDto> getUser(@PathVariable Long userId) {
        UserResponseDto user = userService.getUserById(userId);
        return ResponseEntity.ok(user);
    }

    @GetMapping
    public ResponseEntity<List<UserResponseDto>> getAllUsers() {
        List<UserResponseDto> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<UserResponseDto> updateUser(@SessionAttribute("LOGIN_USER") User user, @RequestBody UserUpdateRequestDto dto) {
        UserResponseDto updatedUser = userService.updateUser(user.getId(), dto);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@SessionAttribute("LOGIN_USER") User user, @RequestParam String password) {
        userService.deleteUser(user.getId(), password);
        return ResponseEntity.noContent().build();
    }
}
