package com.sixmycat.catchy.feature.auth.command.domain.service;

import com.sixmycat.catchy.exception.BusinessException;
import com.sixmycat.catchy.exception.ErrorCode;
import com.sixmycat.catchy.feature.auth.command.domain.aggregate.TempMember;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class OAuth2UserInfoExtractorImpl implements OAuth2UserInfoExtractor {

    @Override
    public String extractEmail(DefaultOAuth2User user, String platform) {
        return switch (platform.toLowerCase()) {
            case "naver" -> ((Map<String, Object>) user.getAttributes().get("response")).get("email").toString();
            case "kakao" -> ((Map<String, Object>) user.getAttributes().get("kakao_account")).get("email").toString();
            case "google" -> user.getAttributes().get("email").toString();
            default -> throw new BusinessException(ErrorCode.SOCIAL_PLATFORM_NOT_SUPPORTED);
        };
    }

    @Override
    public TempMember extractTempMember(DefaultOAuth2User user, String platform) {
        System.out.println("attributes: " + user.getAttributes());
        return TempMember.builder()
                .email(extractEmail(user, platform))
                .social(platform.toUpperCase())
                .name(extractName(user, platform))
                .contactNumber(extractPhone(user, platform))
                .build();
    }

    private String extractName(DefaultOAuth2User user, String platform) {
        switch (platform) {
            case "naver" -> {
                return ((Map<String, Object>) user.getAttributes().get("response")).get("name").toString();
            }
            case "kakao" -> {
                Map<String, Object> kakaoAccount = (Map<String, Object>) user.getAttributes().get("kakao_account");
                if (kakaoAccount == null) return null;

                Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");
                if (profile == null) return null;

                Object nickname = profile.get("nickname");
                return nickname != null ? nickname.toString() : null;
            }
            case "google" -> {
                return user.getAttributes().get("name").toString();
            }
        }

        return "";
    }


    private String extractPhone(DefaultOAuth2User user, String platform) {
        switch (platform) {
            case "naver" -> {
                return ((Map<String, Object>) user.getAttributes().get("response")).get("mobile").toString();
            }
            case "kakao" -> {
                Map<String, Object> kakaoAccount = (Map<String, Object>) user.getAttributes().get("kakao_account");
                if (kakaoAccount == null) return null;

                Object phoneNumber = kakaoAccount.get("phone_number");
                return phoneNumber != null ? phoneNumber.toString() : null;
            }
            case "google" -> {
                return null;
            }
        }
        return null;

    }
}
