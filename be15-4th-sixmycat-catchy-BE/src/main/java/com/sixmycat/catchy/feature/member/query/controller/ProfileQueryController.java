package com.sixmycat.catchy.feature.member.query.controller;

import com.sixmycat.catchy.feature.member.query.dto.response.MyProfileResponse;
import com.sixmycat.catchy.feature.member.query.service.ProfileQueryService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/profiles")
public class ProfileQueryController {

    private final ProfileQueryService profileQueryService;

    @GetMapping("/me")
    public ResponseEntity<MyProfileResponse> getMyProfile(@AuthenticationPrincipal String memberId) {
        MyProfileResponse response = profileQueryService.getMyProfile(Long.parseLong(memberId));
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{memberId}")
    public ResponseEntity<MyProfileResponse> getOtherProfile(@PathVariable String memberId) {
        MyProfileResponse response = profileQueryService.getOtherProfile(Long.parseLong(memberId));
        return ResponseEntity.ok(response);
    }

    @GetMapping("/nickname/{nickname}")
    public ResponseEntity<MyProfileResponse> getProfileByNickname(@PathVariable String nickname) {
        MyProfileResponse response = profileQueryService.getProfileByNickname(nickname);
        return ResponseEntity.ok(response);
    }

}
