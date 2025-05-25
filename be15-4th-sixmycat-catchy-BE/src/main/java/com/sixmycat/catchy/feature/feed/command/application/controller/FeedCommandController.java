package com.sixmycat.catchy.feature.feed.command.application.controller;

import com.sixmycat.catchy.common.dto.ApiResponse;
import com.sixmycat.catchy.feature.feed.command.application.dto.request.FeedCreateRequest;
import com.sixmycat.catchy.feature.feed.command.application.dto.request.FeedUpdateRequest;
import com.sixmycat.catchy.feature.feed.command.application.service.FeedCommandService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/feeds")
public class FeedCommandController {

    private final FeedCommandService feedCommandService;

    @PostMapping
    public ApiResponse<Long> createFeed(@RequestBody FeedCreateRequest request,
                                        @AuthenticationPrincipal String memberId) {
        Long feedId = feedCommandService.createFeed(request, Long.parseLong(memberId));
        return ApiResponse.success(feedId);
    }

    @PutMapping("/{id}")
    public ApiResponse<Long> updateFeed(
            @PathVariable Long id,
            @RequestBody FeedUpdateRequest request,
            @AuthenticationPrincipal String memberId
    ) {
        feedCommandService.updateFeed(id, request, Long.parseLong(memberId));
        return ApiResponse.success(id);
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteFeed(@PathVariable Long id,
                                        @AuthenticationPrincipal String memberId) {
        feedCommandService.deleteFeed(id, Long.parseLong(memberId));
        return ApiResponse.success(null);
    }
}