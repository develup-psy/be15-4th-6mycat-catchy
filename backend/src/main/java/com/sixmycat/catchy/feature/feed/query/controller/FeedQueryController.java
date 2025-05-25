package com.sixmycat.catchy.feature.feed.query.controller;

import com.sixmycat.catchy.common.dto.ApiResponse;
import com.sixmycat.catchy.common.dto.PageResponse;
import com.sixmycat.catchy.feature.feed.query.dto.response.FeedDetailResponse;
import com.sixmycat.catchy.feature.feed.query.dto.response.FeedSummaryResponse;
import com.sixmycat.catchy.feature.feed.query.service.FeedQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/feeds")
@RequiredArgsConstructor
public class FeedQueryController {

    private final FeedQueryService feedQueryService;

    @GetMapping("/{feedId}")
    public ApiResponse<FeedDetailResponse> getFeedDetail(
            @PathVariable Long feedId,
            @AuthenticationPrincipal String memberId
    ) {
        FeedDetailResponse response = feedQueryService.getFeedDetail(feedId, Long.parseLong(memberId));
        return ApiResponse.success(response);
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<PageResponse<FeedSummaryResponse>>> getMyFeeds(
            @AuthenticationPrincipal String memberId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size
    ) {
        PageResponse<FeedSummaryResponse> result = feedQueryService.getMyFeeds(Long.parseLong(memberId), page, size);
        return ResponseEntity.ok(ApiResponse.success(result));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<FeedDetailResponse>>> getFeeds(
            @AuthenticationPrincipal Object principal,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ) {
        Long memberId = null;
        if (principal instanceof String str && !str.equals("anonymousUser")) {
            try {
                memberId = Long.parseLong(str);
            } catch (NumberFormatException ignored) {}
        }

        PageResponse<FeedDetailResponse> response = feedQueryService.getFeedList(memberId, page, size);
        return ResponseEntity.ok(ApiResponse.success(response));
    }


    @GetMapping("/likes")
    public ResponseEntity<ApiResponse<PageResponse<FeedSummaryResponse>>> getLikedFeeds(
            @AuthenticationPrincipal String memberId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ) {
        PageResponse<FeedSummaryResponse> response = feedQueryService.getLikedFeedList(Long.parseLong(memberId), page, size);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/member/{memberId}")
    public ResponseEntity<ApiResponse<PageResponse<FeedSummaryResponse>>> getFeedsByMemberId(
            @PathVariable Long memberId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size

    ) {
        PageResponse<FeedSummaryResponse> response = feedQueryService.getFeedsByMemberId(memberId, page, size);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

}