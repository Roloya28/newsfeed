package com.example.newsfeed.domain.comment.controller;

import com.example.newsfeed.domain.comment.dto.request.CommentRequestDto;
import com.example.newsfeed.domain.comment.dto.response.CommentResponseDto;
import com.example.newsfeed.domain.comment.service.CommentService;
import com.example.newsfeed.domain.user.entity.User;
import com.example.newsfeed.domain.user.entity.UserRole;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(CommentController.class)
class CommentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private CommentService commentService;

    @Test
    void 댓글_생성() throws Exception {
        // Given
        User user = new User("test@sample.com", "password1234", "테스트", UserRole.USER);
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("LOGIN_USER", user);

        Long feedId = 1L;
        Long commentId = 1L;
        String content = "새 댓글";
        LocalDateTime now = LocalDateTime.now();

        CommentRequestDto requestDto = new CommentRequestDto(content);
        CommentResponseDto responseDto = new CommentResponseDto(commentId, user.getId(), feedId, content, now, now);

        given(commentService.createComment(anyLong(), anyLong(), any(CommentRequestDto.class)))
                .willReturn(new CommentResponseDto(1L, 1L, feedId, "새 댓글", LocalDateTime.now(), LocalDateTime.now()));

        // When & Then
        mockMvc.perform(post("/comments/{feedId}", feedId)
                .session(session)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(commentId))
                .andExpect(jsonPath("$.userId").value(user.getId()))
                .andExpect(jsonPath("$.feedId").value(feedId))
                .andExpect(jsonPath("$.content").value(content));
    }


}