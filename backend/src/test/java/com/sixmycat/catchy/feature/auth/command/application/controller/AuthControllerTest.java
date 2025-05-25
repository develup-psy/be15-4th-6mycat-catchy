package com.sixmycat.catchy.feature.auth.command.application.controller;

import com.sixmycat.catchy.feature.auth.command.application.service.AuthCommandService;
import com.sixmycat.catchy.security.jwt.JwtTokenProvider;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import jakarta.servlet.http.Cookie;

@WebMvcTest(AuthController.class)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @SuppressWarnings("removal")
    @MockBean
    private AuthCommandService authCommandService;

    @SuppressWarnings("removal")
    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    @Test
    @WithMockUser(username = "10") // 이 부분을 본인 테이터베이스에 있는 member 의 id 값이어야 함!!
    @DisplayName("로그아웃 성공 - accessToken과 refreshToken이 올바르게 주어진 경우")
    void logout_success() throws Exception {
        // given
        String accessToken = "mockAccessToken";
        String refreshToken = "mockRefreshToken";

        // when
        MockHttpServletRequestBuilder request = post("/api/v1/members/logout")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .cookie(new Cookie("refreshToken", refreshToken))
                .with(csrf());

        // then
        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.SET_COOKIE, org.hamcrest.Matchers.containsString("refreshToken=;")))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.errorCode").doesNotExist());

        // verify
        verify(authCommandService).logout(refreshToken);
    }
}
