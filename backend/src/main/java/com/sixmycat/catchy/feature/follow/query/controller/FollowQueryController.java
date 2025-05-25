package com.sixmycat.catchy.feature.follow.query.controller;

import com.sixmycat.catchy.common.dto.ApiResponse;
import com.sixmycat.catchy.common.dto.PageResponse;
import com.sixmycat.catchy.feature.follow.query.dto.FollowingMemberResponse;
import com.sixmycat.catchy.feature.follow.query.dto.ReceivedFollowResponse;
import com.sixmycat.catchy.feature.follow.query.service.FollowQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/follows")
@RequiredArgsConstructor
public class FollowQueryController {

    private final FollowQueryService followQueryService;

    /* 내가 팔로잉한 목록 조회 */
    @GetMapping("/following")
    public ResponseEntity<ApiResponse<PageResponse<FollowingMemberResponse>>> getFollowingList(
            @AuthenticationPrincipal String memberId,
            Pageable pageable
    ) {
        PageResponse<FollowingMemberResponse> result = followQueryService.getFollowingList(Long.parseLong(memberId), pageable);
        return ResponseEntity.ok(ApiResponse.success(result));
    }

    /* 팔로우 목록 조회(나를 팔로우 한 회원 목록 조회) */
    @GetMapping("/received")
    public ResponseEntity<ApiResponse<PageResponse<ReceivedFollowResponse>>> getReceivedFollows(
            @AuthenticationPrincipal String memberId,
            Pageable pageable
    ) {
        return ResponseEntity.ok(ApiResponse.success(followQueryService.getReceivedFollows(Long.parseLong(memberId), pageable)));
    }
}
