package com.sixmycat.catchy.feature.auth.command.application.controller;

import com.sixmycat.catchy.common.dto.ApiResponse;
import com.sixmycat.catchy.common.utils.CookieUtils;
import com.sixmycat.catchy.feature.auth.command.application.dto.request.ExtraSignupRequest;
import com.sixmycat.catchy.feature.auth.command.application.dto.response.SocialLoginResponse;
import com.sixmycat.catchy.feature.auth.command.application.dto.response.SocialLoginResultResponse;
import com.sixmycat.catchy.feature.auth.command.application.service.AuthCommandService;
import com.sixmycat.catchy.feature.auth.command.domain.aggregate.TempMember;
import com.sixmycat.catchy.security.jwt.JwtTokenProvider;
import com.sixmycat.catchy.common.dto.TokenResponse;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.multipart.MultipartFile;

import static com.sixmycat.catchy.common.utils.CookieUtils.createRefreshTokenCookie;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/members")
public class AuthController {

    private final JwtTokenProvider jwtTokenProvider;
    private final AuthCommandService authCommandService;

    @PostMapping("/signup/extra")
    public ResponseEntity<ApiResponse<SocialLoginResponse>> registerExtraInfo(
            @ModelAttribute ExtraSignupRequest request,
            @RequestPart(value = "profileImage", required = false) MultipartFile profileImage,
            HttpServletResponse response
    ) {
        SocialLoginResultResponse result = authCommandService.registerNewMember(request, profileImage);

        String refreshToken = (String) RequestContextHolder.getRequestAttributes()
                .getAttribute("refreshToken", RequestAttributes.SCOPE_REQUEST);

        if (refreshToken != null) {
            ResponseCookie cookie = CookieUtils.createRefreshTokenCookie(refreshToken);
            response.addHeader("Set-Cookie", cookie.toString());
        }

        return ResponseEntity.ok(ApiResponse.success(result.getResponse()));
    }


    @GetMapping("/temp-info")
    public ResponseEntity<ApiResponse<TempMember>> getTempInfo(@RequestParam String email, @RequestParam String social) {
        TempMember temp = authCommandService.getTempMember(email, social.toUpperCase());
        return ResponseEntity.ok(ApiResponse.success(temp));
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout(
            @CookieValue(name = "refreshToken", required = false) String refreshToken
    ) {
        if (refreshToken != null) {
            authCommandService.logout(refreshToken);
        }

        ResponseCookie deleteCookie = CookieUtils.createDeleteRefreshTokenCookie();
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, deleteCookie.toString())
                .body(ApiResponse.success(null));
    }

    @GetMapping("/token")
    public ResponseEntity<ApiResponse<SocialLoginResponse>> reissueAccessToken(
            @CookieValue(name = "refreshToken", required = false) String refreshToken
    ) {
        // userId 추출
        Long userId = Long.parseLong(jwtTokenProvider.getUserIdFromJwt(refreshToken));

        // accessToken 재발급
        String newAccessToken = jwtTokenProvider.createToken(userId);

        SocialLoginResponse response = SocialLoginResponse.loggedInWithoutRefresh(userId, newAccessToken);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    /* 테스트 로그인 */
    @Operation(summary = "테스트용 로그인", description = "테스트용 로그인 후 JWT를 발급합니다.")
    @PostMapping("/login/test")
    public ResponseEntity<ApiResponse<TokenResponse>> login(){
        TokenResponse token = authCommandService.testLogin();
        return buildTokenResponse(token);
    }

    @DeleteMapping
    public ResponseEntity<ApiResponse<Void>> delete(
            @CookieValue(name = "refreshToken", required = false) String refreshToken
    ) {
        if (refreshToken != null) {
            authCommandService.delete(refreshToken);
        }

        ResponseCookie deleteCookie = CookieUtils.createDeleteRefreshTokenCookie();
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, deleteCookie.toString())
                .body(ApiResponse.success(null));
    }

    /* accessToken 과 refreshToken을 body와 쿠키에 담아 반환 */
    private ResponseEntity<ApiResponse<TokenResponse>> buildTokenResponse(TokenResponse tokenResponse) {
        ResponseCookie cookie = createRefreshTokenCookie(tokenResponse.getRefreshToken());  // refreshToken 쿠키 생성
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(ApiResponse.success(tokenResponse));
    }
}
