package com.sixmycat.catchy.common.dto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Builder
@Getter
@Schema(description = "JWT 토큰 응답 DTO")
@RequiredArgsConstructor
public class TokenResponse {
    @Schema(description = "Access Token")
    private final String accessToken;
    @Schema(description = "Refresh Token")
    private final String refreshToken;
}