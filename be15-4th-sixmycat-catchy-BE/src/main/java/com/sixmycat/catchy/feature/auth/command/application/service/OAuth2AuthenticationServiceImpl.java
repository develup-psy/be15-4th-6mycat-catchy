package com.sixmycat.catchy.feature.auth.command.application.service;

import com.sixmycat.catchy.feature.auth.command.application.dto.response.SocialLoginResponse;
import com.sixmycat.catchy.feature.auth.command.domain.aggregate.TempMember;
import com.sixmycat.catchy.feature.auth.command.domain.service.JwtTokenDomainService;
import com.sixmycat.catchy.feature.auth.command.domain.service.OAuth2UserInfoExtractor;
import com.sixmycat.catchy.feature.auth.command.domain.service.TempMemberRedisService;
import com.sixmycat.catchy.feature.member.command.domain.aggregate.Member;
import com.sixmycat.catchy.feature.member.command.domain.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OAuth2AuthenticationServiceImpl implements OAuth2AuthenticationService {

    private final MemberRepository memberRepository;
    private final JwtTokenDomainService jwtTokenDomainService;
    private final TempMemberRedisService tempMemberRedisService;
    private final OAuth2UserInfoExtractor userInfoExtractor;

    @Override
    public SocialLoginResponse handleOAuth2Login(DefaultOAuth2User oAuth2User, String registrationId) {
        String email = userInfoExtractor.extractEmail(oAuth2User, registrationId);

        Optional<Member> optionalMember = memberRepository.findByEmailAndSocialAndDeletedAtIsNull(email, registrationId.toUpperCase());
        if (optionalMember.isPresent()) {
            Member member = optionalMember.get();
            Long memberId = member.getId();

            String accessToken = jwtTokenDomainService.createAccessToken(memberId);
            String refreshToken = jwtTokenDomainService.createRefreshToken(memberId);
            jwtTokenDomainService.storeRefreshToken(memberId, refreshToken); // Redis 저장

            // 쿠키 전송용으로 request attribute에 저장
            RequestContextHolder.getRequestAttributes()
                    .setAttribute("refreshToken", refreshToken, RequestAttributes.SCOPE_REQUEST);

            // accessToken은 바디 응답용
            RequestContextHolder.getRequestAttributes()
                    .setAttribute("accessToken", accessToken, RequestAttributes.SCOPE_REQUEST);
            RequestContextHolder.getRequestAttributes()
                    .setAttribute("userId", memberId, RequestAttributes.SCOPE_REQUEST);

            // 응답 객체 반환 (refreshToken은 포함 안 함)
            return SocialLoginResponse.loggedInWithoutRefresh(memberId, accessToken);
        }

        // 신규 사용자 → 추가 정보 필요
        TempMember tempMember = userInfoExtractor.extractTempMember(oAuth2User, registrationId);
        tempMemberRedisService.save(tempMember);

        return SocialLoginResponse.requireAdditionalInfo(tempMember.getEmail());
    }
}
