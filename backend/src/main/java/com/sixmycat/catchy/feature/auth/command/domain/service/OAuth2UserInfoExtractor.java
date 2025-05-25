package com.sixmycat.catchy.feature.auth.command.domain.service;

import com.sixmycat.catchy.feature.auth.command.domain.aggregate.TempMember;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;

public interface OAuth2UserInfoExtractor {
    String extractEmail(DefaultOAuth2User user, String platform);
    TempMember extractTempMember(DefaultOAuth2User user, String platform);
}
