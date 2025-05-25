package com.sixmycat.catchy.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sixmycat.catchy.common.utils.CookieUtils;
import com.sixmycat.catchy.feature.auth.command.application.dto.response.SocialLoginResponse;
import com.sixmycat.catchy.feature.auth.command.application.service.OAuth2AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final OAuth2AuthenticationService oAuth2AuthenticationService;
    private final ObjectMapper objectMapper;

    @Value("${app.front.signup-extra-url}")
    private String signupExtraPageUrl;

    @Value("${app.front.login-success-url}")
    private String loginSuccessPageUrl;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        OAuth2AuthenticationToken authToken = (OAuth2AuthenticationToken) authentication;
        DefaultOAuth2User oAuth2User = (DefaultOAuth2User) authToken.getPrincipal();

        // 로그인 처리
        SocialLoginResponse loginResponse = oAuth2AuthenticationService.handleOAuth2Login(
                oAuth2User,
                authToken.getAuthorizedClientRegistrationId()
        );

        // accessToken & refreshToken 쿠키 설정 (기존 유저만)
        if (!loginResponse.isNewUser()) {
            String refreshToken = (String) RequestContextHolder.getRequestAttributes()
                    .getAttribute("refreshToken", RequestAttributes.SCOPE_REQUEST);
            String accessToken = (String) RequestContextHolder.getRequestAttributes()
                    .getAttribute("accessToken", RequestAttributes.SCOPE_REQUEST);

            if (refreshToken != null) {
                ResponseCookie cookie = CookieUtils.createRefreshTokenCookie(refreshToken);
                response.addHeader("Set-Cookie", cookie.toString());
            }
            if (accessToken != null) {
                ResponseCookie cookie = CookieUtils.createAccessTokenCookie(accessToken);
                response.addHeader("Set-Cookie", cookie.toString());
            }
        }

        // 신규 유저는 추가 정보 입력 페이지로 리디렉트
        if (loginResponse.isNewUser()) {
            String social = authToken.getAuthorizedClientRegistrationId().toUpperCase(); // "NAVER" 또는 "KAKAO"

            String redirectUrl = UriComponentsBuilder.fromUriString(signupExtraPageUrl)
                    .queryParam("email", loginResponse.getEmail())
                    .queryParam("social", social)
                    .build().toUriString();

            response.sendRedirect(redirectUrl);
            return;
        }

        response.sendRedirect(loginSuccessPageUrl);
    }
}
