package com.sixmycat.catchy.feature.auth.command.domain.service;

import com.sixmycat.catchy.feature.auth.command.domain.aggregate.RefreshToken;
import com.sixmycat.catchy.feature.member.command.domain.aggregate.Member;
import com.sixmycat.catchy.security.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class JwtTokenDomainServiceImpl implements JwtTokenDomainService {

    private final JwtTokenProvider jwtTokenProvider;
    private final RedisTemplate<String, RefreshToken> refreshTokenRedisTemplate;

    @Value("${jwt.refresh-expiration}")
    private Long refreshTokenExpiration;

    @Override
    public String issueAndRedirect(String baseRedirectUrl, Member member) {
        Long memberId = member.getId();
        String accessToken = jwtTokenProvider.createToken(memberId);
        String refreshToken = jwtTokenProvider.createRefreshToken(memberId);

        refreshTokenRedisTemplate.opsForValue().set(
                "REFRESH_TOKEN:" + memberId,
                new RefreshToken(refreshToken),
                Duration.ofMillis(refreshTokenExpiration)
        );

        return UriComponentsBuilder.fromUriString(baseRedirectUrl)
                .queryParam("accessToken", accessToken)
                .queryParam("refreshToken", refreshToken)
                .queryParam("userId", memberId)
                .build().toUriString();
    }

    @Override
    public String createAccessToken(Long memberId) {
        return jwtTokenProvider.createToken(memberId);
    }

    @Override
    public String createRefreshToken(Long memberId) {
        return jwtTokenProvider.createRefreshToken(memberId);
    }

    @Override
    public void storeRefreshToken(Long memberId, String refreshToken) {
        refreshTokenRedisTemplate.opsForValue().set(
                "REFRESH_TOKEN:" + memberId,
                new RefreshToken(refreshToken),
                Duration.ofMillis(refreshTokenExpiration)
        );
    }
}
