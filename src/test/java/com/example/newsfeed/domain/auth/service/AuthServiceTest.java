package com.example.newsfeed.domain.auth.service;

import com.example.newsfeed.domain.auth.dto.request.AuthRequestDto;
import com.example.newsfeed.domain.user.entity.User;
import com.example.newsfeed.domain.user.repository.UserRepository;
import com.example.newsfeed.domain.user.entity.UserRole;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthService authService;

    @Test
    void 회원_가입_성공() {
        // Given
        AuthRequestDto dto = new AuthRequestDto("test@sample.com", "Password1234!");
        User user = User.builder()
                .email(dto.getEmail())
                .password("encodedPassword")
                .name("test")
                .role(UserRole.USER)
                .build();

        when(userRepository.findByEmail(dto.getEmail())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(dto.getPassword())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);

        // When
        User savedUser = authService.signup(dto);

        // Then
        assertThat(savedUser).isNotNull();
        assertThat(savedUser.getEmail()).isEqualTo(dto.getEmail());
        assertThat(savedUser.getPassword()).isEqualTo("encodedPassword");
        assertThat(savedUser.getName()).isEqualTo("test");
        assertThat(savedUser.getRole()).isEqualTo(UserRole.USER);

        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void 중복_이메일로_회원가입_실패() {
        // Given
        AuthRequestDto dto = new AuthRequestDto("test@sample.com", "Password1234!");
        User existingUser = User.builder()
                .email(dto.getEmail())
                .password("encodedPassword")
                .name("test")
                .role(UserRole.USER)
                .build();

        when(userRepository.findByEmail(dto.getEmail())).thenReturn(Optional.of(existingUser));

        // When & Then
        assertThatThrownBy(() -> authService.signup(dto))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("이미 존재하는 이메일입니다.");

        verify(userRepository, never()).save(any(User.class));
    }
}