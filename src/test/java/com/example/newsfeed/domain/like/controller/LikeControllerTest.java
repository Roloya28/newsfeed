package com.example.newsfeed.domain.like.controller;

import com.example.newsfeed.domain.like.service.LikeService;
import com.example.newsfeed.domain.user.entity.User;
import com.example.newsfeed.domain.user.entity.UserRole;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(LikeController.class)
class LikeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private LikeService likeService;

    @Test
    void 게시글_좋아요() throws Exception {
        // Given
        Long feedId = 1L;
        User user = new User("test@sample.com", "password1234", "테스트", UserRole.USER);

        when(likeService.toggleLikeOnFeed(any(User.class), any(Long.class)))
                .thenReturn("좋아요 됨");

        // When & Then
        mockMvc.perform(MockMvcRequestBuilders.post("/likes/feed/{feedId}", feedId)
                .sessionAttr("LOGIN_USER", user)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("좋아요 됨"));
    }

    @Test
    void 댓글_좋아요() throws Exception {
        // Given
        Long commentId = 1L;
        User user = new User(":test@sample.com", "password1234", "테스트", UserRole.USER);

        when(likeService.toggleLikeComment(any(User.class), any(Long.class)))
                .thenReturn("좋아요 됨");

        // When & Then
        mockMvc.perform(MockMvcRequestBuilders.post("/likes/comment/{commentId}", commentId)
                .sessionAttr("LOGIN_USER", user)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("좋아요 됨"));
    }
}