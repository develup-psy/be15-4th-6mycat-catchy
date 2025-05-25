package com.sixmycat.catchy.feature.auth.command.application.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SocialLoginResponse {
    private final boolean isNewUser;
    private final String email;
    private final Long id;
    private final String accessToken;

    // 명시적 생성자
    public SocialLoginResponse(boolean isNewUser, String email, Long id, String accessToken) {
        this.isNewUser = isNewUser;
        this.email = email;
        this.id = id;
        this.accessToken = accessToken;
    }

    // 정적 생성 메서드
    public static SocialLoginResponse loggedIn(Long id, String accessToken) {
        return new SocialLoginResponse(false, null, id, accessToken);
    }

    public static SocialLoginResponse requireAdditionalInfo(String email) {
        return new SocialLoginResponse(true, email, null, null);
    }

    public static SocialLoginResponse loggedInWithoutRefresh(Long id, String accessToken) {
        return new SocialLoginResponse(false, null, id, accessToken);
    }
}
