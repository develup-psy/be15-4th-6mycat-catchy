package com.sixmycat.catchy.feature.auth.command.application.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SocialLoginResultResponse {
    private final SocialLoginResponse response;
    private final String refreshToken;

    public SocialLoginResultResponse(SocialLoginResponse response, String refreshToken) {
        this.response = response;
        this.refreshToken = refreshToken;
    }
}
