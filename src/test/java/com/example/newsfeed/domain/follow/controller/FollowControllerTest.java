package com.example.newsfeed.domain.follow.controller;

import com.example.newsfeed.domain.follow.service.FollowService;
import com.example.newsfeed.domain.user.entity.User;
import com.example.newsfeed.domain.user.entity.UserRole;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MockitoExtension.class)
class FollowControllerTest {

    private MockMvc mockMvc;

    @Mock
    private FollowService followService;

    @InjectMocks
    private FollowController followController;

    public FollowControllerTest() {
        MockitoAnnotations.openMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(followController).build();
    }

    @Test
    void 다른_사람을_팔로우하면_FOLLOWED_상태반환() throws Exception {
        // Given
        Long followingId = 2L;
        User follower = new User("test@sample.com", "password1234", "테스트", UserRole.USER);
        ReflectionTestUtils.setField(follower, "id", 1L);

        when(followService.toggleFollow(any(Long.class), any(Long.class)))
                .thenReturn(FollowService.FollowStatus.FOLLOWED);

        // When & Then
        mockMvc.perform(post("/follow/{followingId}", followingId)
                .sessionAttr("LOGIN_USER", follower)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("FOLLOWED"));
    }

    @Test
    void 이미_팔로우한_사용자에게_다시요청시_UNFOLLOWED_상태반환() throws Exception {
        // Given
        Long followingId = 2L;
        User follower = new User("test@sample.com", "password1234", "테스트", UserRole.USER);
        ReflectionTestUtils.setField(follower, "id", 1L);

        when(followService.toggleFollow(eq(follower.getId()), eq(followingId)))
                .thenReturn(FollowService.FollowStatus.UNFOLLOWED);

        // When & Then
        mockMvc.perform(post("/follow/{followingId}", followingId)
                .sessionAttr("LOGIN_USER", follower)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("UNFOLLOWED"));
    }
}

