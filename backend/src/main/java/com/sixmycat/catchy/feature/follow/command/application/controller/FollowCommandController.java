package com.sixmycat.catchy.feature.follow.command.application.controller;

import com.sixmycat.catchy.common.dto.ApiResponse;
import com.sixmycat.catchy.feature.follow.command.application.service.FollowService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/follows")
@RequiredArgsConstructor
public class FollowCommandController {

    private final FollowService followService;

    /* 팔로우 신청 핸들러 */
    @PostMapping("/{followingId}")
    @Operation(summary = "팔로우 요청")
    public ResponseEntity<ApiResponse<Void>> follow(
            @AuthenticationPrincipal String memberId,
            @PathVariable Long followingId
    ) {
        followService.follow(Long.parseLong(memberId), followingId);
        return ResponseEntity.ok(ApiResponse.success(null));
    }
//
    /* 팔로우 취소*/
    @DeleteMapping("/{targetId}/cancel")
    @Operation(summary = "팔로우 취소")
    public ResponseEntity<ApiResponse<Void>> unfollow(
            @AuthenticationPrincipal String memberId,
            @PathVariable Long targetId
    ) {
        followService.unfollow(Long.parseLong(memberId), targetId);
        return ResponseEntity.ok(ApiResponse.success(null));
    }
//
//    /* 팔로우 수락 */
//    @PostMapping("/{requesterId}/accept")
//    public ResponseEntity<ApiResponse<Void>> acceptFollowRequest(
//            @AuthenticationPrincipal String memberId,
//            @PathVariable Long requesterId
//    ) {
//        followService.acceptFollowRequest(Long.parseLong(memberId), requesterId);
//        return ResponseEntity.ok(ApiResponse.success(null));
//    }
//
    /* 팔로우 거절 */
    @DeleteMapping("/{followerId}/reject")
    public ResponseEntity<ApiResponse<Void>> deleteFollower(
            @AuthenticationPrincipal String memberId,
            @PathVariable Long followerId
    ) {
        followService.deleteFollower(Long.parseLong(memberId),followerId);
        return ResponseEntity.ok(ApiResponse.success(null));
    }


}

