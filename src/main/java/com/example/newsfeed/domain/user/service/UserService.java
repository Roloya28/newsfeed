package com.example.newsfeed.domain.user.service;

import com.example.newsfeed.domain.user.dto.request.UserUpdateRequestDto;
import com.example.newsfeed.domain.user.dto.response.UserResponseDto;
import com.example.newsfeed.domain.user.entity.User;
import com.example.newsfeed.domain.user.repository.UserRepository;
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
        return new UserResponseDto(user.getId(), user.getName(), user.getFollowersCount(), user.getFollowingsCount(), user.getRole());
    }

    @Transactional(readOnly = true)
    public List<UserResponseDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(user -> new UserResponseDto(user.getId(), user.getName(), user.getFollowersCount(), user.getFollowingsCount(), user.getRole()))
                .toList();
    }

    @Transactional
    public UserResponseDto updateUser(User loginUser, Long userId, UserUpdateRequestDto dto) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new RuntimeException("유저를 찾을 수 없습니다.")
        );
        if (!user.equals(loginUser)) {
            throw new RuntimeException("유저 본인만 수정이 가능합니다.");
        }
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
        return new UserResponseDto(user.getId(), user.getName(), user.getFollowersCount(), user.getFollowingsCount(), user.getRole());
    }

    @Transactional
    public void deleteUser(User loginUser, Long userId, String password) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new RuntimeException("유저를 찾을 수 없습니다.")
        );
        if (!user.equals(loginUser)) {
            throw new RuntimeException("유저 본인만 아이디 삭제가 가능합니다.");
        }
        if (!user.getPassword().equals(password)) {
            throw new RuntimeException("비밀번호가 일치하지 않습니다.");
        }
        userRepository.delete(user);
    }
}
