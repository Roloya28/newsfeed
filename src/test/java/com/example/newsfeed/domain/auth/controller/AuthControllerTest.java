package com.example.newsfeed.domain.auth.controller;

import com.example.newsfeed.domain.auth.dto.request.AuthRequestDto;
import com.example.newsfeed.domain.auth.service.AuthService;
import com.example.newsfeed.domain.user.entity.User;
import com.example.newsfeed.domain.user.entity.UserRole;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.server.ResponseStatusException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private AuthService authService;

    @Test
    void 회원가입_성공() throws Exception {
        // Given
        AuthRequestDto requestDto = new AuthRequestDto("test@sample.com", "Password1234!");
        User mockUser = new User("test@sample.com", "encodedPassword", "testUser", UserRole.USER);

        when(authService.signup(any(AuthRequestDto.class))).thenReturn(mockUser);

        // When & Then
        mockMvc.perform(MockMvcRequestBuilders.post("/auth/signup") // ✅ 변경된 경로 사용
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)))
                .andDo(print())
                .andExpect(status().isCreated()) // ✅ 기대하는 상태코드 201
                .andExpect(jsonPath("$.email").value("test@sample.com"))
                .andExpect(jsonPath("$.name").value("testUser"));
    }

    @Test
    void 이메일_중복으로_회원가입_실패() throws Exception {
        // Given
        AuthRequestDto requestDto = new AuthRequestDto("test@sample.com", "Password1234!");

        when(authService.signup(any(AuthRequestDto.class)))
                .thenThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST, "이미 존재하는 이메일입니다."));

        // When & Then
        mockMvc.perform(MockMvcRequestBuilders.post("/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("이미 존재하는 이메일입니다."));
    }
}