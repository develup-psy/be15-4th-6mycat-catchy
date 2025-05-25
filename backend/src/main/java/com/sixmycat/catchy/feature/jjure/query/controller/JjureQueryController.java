package com.sixmycat.catchy.feature.jjure.query.controller;

import com.sixmycat.catchy.common.dto.ApiResponse;
import com.sixmycat.catchy.common.dto.PageResponse;
import com.sixmycat.catchy.feature.jjure.query.dto.response.JjureDetailResponse;
import com.sixmycat.catchy.feature.jjure.query.dto.response.JjureSummaryResponse;
import com.sixmycat.catchy.feature.jjure.query.service.JjureQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/jjures")
@RequiredArgsConstructor
public class JjureQueryController {

    private final JjureQueryService jjureQueryService;

    /* 쮸르 상세 조회 */
    @GetMapping("/{jjureId}")
    public ResponseEntity<ApiResponse<JjureDetailResponse>> getJjureDetail(
            @PathVariable Long jjureId,
            @AuthenticationPrincipal String memberId
    ) {
        JjureDetailResponse response = jjureQueryService.getJjureDetail(jjureId, Long.parseLong(memberId));
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    /* 쮸르 목록 조회 */
    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<JjureDetailResponse>>> getJjures(
            @AuthenticationPrincipal String memberId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        PageResponse<JjureDetailResponse> response = jjureQueryService.getJjureList(Long.parseLong(memberId), page, size);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    /* 좋아요한 쮸르 목록 조회 */
    @GetMapping("/likes")
    public ResponseEntity<ApiResponse<PageResponse<JjureSummaryResponse>>> getLikedJjures(
            @AuthenticationPrincipal String memberId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ) {
        PageResponse<JjureSummaryResponse> response = jjureQueryService.getLikedJjureList(Long.parseLong(memberId), page, size);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    /* 회원이 생성한 쮸르 목록 조회 */
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<PageResponse<JjureSummaryResponse>>> getMyJjures(
            @AuthenticationPrincipal String memberId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ) {
        PageResponse<JjureSummaryResponse> response = jjureQueryService.getMyJjureList(Long.parseLong(memberId), page, size);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    /* */
    @GetMapping("/member/{memberId}")
    public ResponseEntity<ApiResponse<PageResponse<JjureSummaryResponse>>> getJjuresByMemberId(
            @PathVariable Long memberId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size

    ) {
        PageResponse<JjureSummaryResponse> response = jjureQueryService.getJjuresByMemberId(memberId, page, size);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

}