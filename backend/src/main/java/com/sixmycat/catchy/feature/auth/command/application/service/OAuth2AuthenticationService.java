package com.sixmycat.catchy.feature.auth.command.application.service;

import com.sixmycat.catchy.feature.auth.command.application.dto.response.SocialLoginResponse;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;

public interface OAuth2AuthenticationService {
    SocialLoginResponse handleOAuth2Login(DefaultOAuth2User oAuth2User, String registrationId);
}