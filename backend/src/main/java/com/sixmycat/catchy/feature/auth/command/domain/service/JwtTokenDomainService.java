package com.sixmycat.catchy.feature.auth.command.domain.service;

import com.sixmycat.catchy.feature.member.command.domain.aggregate.Member;

public interface JwtTokenDomainService {
    String issueAndRedirect(String baseRedirectUrl, Member member);

    String createAccessToken(Long memberId);

    String createRefreshToken(Long memberId);

    void storeRefreshToken(Long memberId, String refreshToken);
}
