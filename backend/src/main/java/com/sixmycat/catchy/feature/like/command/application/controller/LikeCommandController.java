package com.sixmycat.catchy.feature.like.command.application.controller;

import com.sixmycat.catchy.common.dto.ApiResponse;
import com.sixmycat.catchy.feature.like.command.application.dto.request.LikeRequest;
import com.sixmycat.catchy.feature.like.command.application.service.LikeCommandService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@Slf4j
public class LikeCommandController {

    private final LikeCommandService likeCommandService;

    @PostMapping("/likes")
    public ResponseEntity<ApiResponse<Void>> like(@RequestBody LikeRequest request, @AuthenticationPrincipal String memberId) {
        likeCommandService.like(Long.parseLong(memberId), request.getTargetId(), request.getTargetType());
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @DeleteMapping("/likes")
    public ResponseEntity<ApiResponse<Void>> unlike(@RequestBody LikeRequest request, @AuthenticationPrincipal String memberId) {
        likeCommandService.unlike(Long.parseLong(memberId), request.getTargetId(), request.getTargetType());
        return ResponseEntity.ok(ApiResponse.success(null));
    }
}
