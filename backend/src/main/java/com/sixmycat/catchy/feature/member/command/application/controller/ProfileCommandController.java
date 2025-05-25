package com.sixmycat.catchy.feature.member.command.application.controller;

import com.sixmycat.catchy.feature.member.command.application.dto.request.AddCatRequest;
import com.sixmycat.catchy.feature.member.command.application.dto.request.UpdateProfileRequest;
import com.sixmycat.catchy.feature.member.command.application.dto.response.UpdateProfileResponse;
import com.sixmycat.catchy.feature.member.command.application.service.MemberCommandService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/profiles")
@RequiredArgsConstructor
public class ProfileCommandController {

    private final MemberCommandService memberCommandService;

    @PatchMapping(value = "/me", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UpdateProfileResponse> updateProfile(
            @AuthenticationPrincipal String memberId,
            @RequestPart("request") UpdateProfileRequest request,
            @RequestPart(value = "imageFile", required = false) MultipartFile imageFile
    ) {
        UpdateProfileResponse response = memberCommandService.updateProfile(Long.parseLong(memberId), request, imageFile);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/cats")
    public ResponseEntity<Void> addCat(
            @AuthenticationPrincipal String memberId,
            @RequestBody AddCatRequest request
    ) {
        memberCommandService.addCat(Long.parseLong(memberId), request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/cats/{catId}")
    public ResponseEntity<Void> deleteCat(
            @AuthenticationPrincipal String memberId,
            @PathVariable Long catId
    ) {
        memberCommandService.deleteCat(Long.parseLong(memberId), catId);
        return ResponseEntity.noContent().build();
    }
}
