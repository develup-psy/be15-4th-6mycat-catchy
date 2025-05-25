package com.sixmycat.catchy.feature.auth.command.application.service;

import com.sixmycat.catchy.common.dto.TokenResponse;
import com.sixmycat.catchy.feature.auth.command.application.dto.request.ExtraSignupRequest;
import com.sixmycat.catchy.feature.auth.command.application.dto.response.SocialLoginResponse;
import com.sixmycat.catchy.feature.auth.command.application.dto.response.SocialLoginResultResponse;
import com.sixmycat.catchy.feature.auth.command.domain.aggregate.TempMember;
import org.springframework.web.multipart.MultipartFile;

public interface AuthCommandService {
    SocialLoginResultResponse registerNewMember(ExtraSignupRequest request, MultipartFile profileImage);
    TempMember getTempMember(String email, String social);
    TokenResponse testLogin();
    void logout(String refreshToken);
    void delete(String refreshToken);
}
