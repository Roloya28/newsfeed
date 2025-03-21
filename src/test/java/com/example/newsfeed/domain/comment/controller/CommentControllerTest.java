package com.example.newsfeed.domain.comment.controller;

import com.example.newsfeed.domain.comment.dto.request.CommentRequestDto;
import com.example.newsfeed.domain.comment.dto.response.CommentResponseDto;
import com.example.newsfeed.domain.comment.service.CommentService;
import com.example.newsfeed.domain.user.entity.User;
import com.example.newsfeed.domain.user.entity.UserRole;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
        Long feedId = 1L;
        User user = new User("test@sample.com", "password1234", "테스트", UserRole.USER);

        ReflectionTestUtils.setField(user, "id", 1L);

        MockHttpSession session = new MockHttpSession();
        session.setAttribute("LOGIN_USER", user);

        CommentRequestDto requestDto = new CommentRequestDto("새 댓글");

        CommentResponseDto responseDto = new CommentResponseDto(1L, user.getId(), feedId, "새 댓글", LocalDateTime.now(), LocalDateTime.now());

        // CommentService 의 createComment 가 ResponseDto 를 반환하도록 설정
        given(commentService.createComment(anyLong(), anyLong(), any(CommentRequestDto.class))).willReturn(responseDto);

        // When & Then
        mockMvc.perform(MockMvcRequestBuilders.post("/comments/{feedId}", feedId)
                .session(session)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").value("새 댓글"))
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.userId").value(user.getId()))
                .andExpect(jsonPath("$.feedId").value(feedId));
    }

    @Test
    void 댓글_조회() throws Exception{
        // Given
        Long feedId = 1L;
        User user = new User("test@sample.com", "password1234", "테스트", UserRole.USER);
        ReflectionTestUtils.setField(user, "id", 1L);

        List<CommentResponseDto> responseList = List.of(
                new CommentResponseDto(1L, user.getId(), feedId, "댓글1", LocalDateTime.now(), LocalDateTime.now()),
                new CommentResponseDto(2L, user.getId(), feedId, "댓글2", LocalDateTime.now(), LocalDateTime.now())
        );

        given(commentService.getCommentsByFeed(anyLong())).willReturn(responseList);

        // When & Then
        mockMvc.perform(MockMvcRequestBuilders.get("/comments/{feedId}", feedId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].content").value("댓글1"))
                .andExpect(jsonPath("$[1].content").value("댓글2"));
    }

    @Test
    void 댓글_수정() throws Exception {
        // Given
        Long commentId= 1L;
        User user = new User("test@sample.com", "password1234", "테스트", UserRole.USER);
        ReflectionTestUtils.setField(user, "id", 1L);

        MockHttpSession session = new MockHttpSession();
        session.setAttribute("LOGIN_USER", user);

        CommentRequestDto requestDto = new CommentRequestDto("수정한 댓글");

        CommentResponseDto responseDto = new CommentResponseDto(commentId, user.getId(), 1L, "수정한 댓글", LocalDateTime.now(), LocalDateTime.now());

        given(commentService.updateComment(anyLong(), anyLong(), any(CommentRequestDto.class))).willReturn(responseDto);

        // When & Then
        mockMvc.perform(MockMvcRequestBuilders.patch("/comments/{commentId}", commentId)
                .session(session)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").value("수정한 댓글"));
    }

    @Test
    void 댓글_삭제() throws Exception {
        // Given
        Long commentId = 1L;
        User user = new User("test@sample.com", "password1234", "테스트", UserRole.USER);
        ReflectionTestUtils.setField(user, "id", 1L);

        MockHttpSession session = new MockHttpSession();
        session.setAttribute("LOGIN_USER", user);

        Mockito.doNothing().when(commentService).deleteComment(anyLong(), anyLong());

        // When & Then
        mockMvc.perform(MockMvcRequestBuilders.delete("/comments/{commentId}", commentId)
                .session(session)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }
}