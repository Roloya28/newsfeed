package com.example.newsfeed.user.service;

import com.example.newsfeed.user.dto.request.UserUpdateRequestDto;
import com.example.newsfeed.user.dto.response.UserResponseDto;
import com.example.newsfeed.user.entity.User;
import com.example.newsfeed.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public UserResponseDto getUserById(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new RuntimeException("유저를 찾을 수 없습니다.")
        );
        return new UserResponseDto(user.getId(), user.getName(), user.getFollowersCount(), user.getFollowingsCount());
    }

    @Transactional(readOnly = true)
    public List<UserResponseDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(user -> new UserResponseDto(user.getId(), user.getName(), user.getFollowersCount(), user.getFollowingsCount()))
                .toList();
    }

    @Transactional
    public UserResponseDto updateUser(Long userId, UserUpdateRequestDto dto) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new RuntimeException("유저를 찾을 수 없습니다.")
        );
        if (dto.getName() != null) user.updateName(dto.getName());
        if (dto.getChangePassword() != null) {
            if (!user.getPassword().equals(dto.getPassword())) {
                throw new RuntimeException("비밀번호가 일치하지 않습니다.");
            }
            if (user.getPassword().equals(dto.getChangePassword())) {
                throw new RuntimeException("기존 비밀번호와 동일합니다.");
            }
            user.updatePassword(dto.getChangePassword());
        }
        userRepository.save(user);
        return new UserResponseDto(user.getId(), user.getName(), user.getFollowersCount(), user.getFollowingsCount());
    }

    @Transactional
    public void deleteUser(Long userId, String password) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new RuntimeException("유저를 찾을 수 없습니다.")
        );
        if (!user.getPassword().equals(password)) {
            throw new RuntimeException("비밀번호가 일치하지 않습니다.");
        }
        userRepository.delete(user);
    }
}
