package com.sixmycat.catchy.feature.feed.query.controller;

import com.sixmycat.catchy.common.dto.ApiResponse;
import com.sixmycat.catchy.common.dto.PageResponse;
import com.sixmycat.catchy.feature.feed.query.dto.response.FeedCommentResponse;
import com.sixmycat.catchy.feature.feed.query.service.FeedCommentQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/feeds")
public class FeedCommentQueryController {

    private final FeedCommentQueryService commentQueryService;

    @GetMapping("/{feedId}/comments")
    public ResponseEntity<ApiResponse<PageResponse<FeedCommentResponse>>> getComments(
            @PathVariable Long feedId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        PageResponse<FeedCommentResponse> comments = commentQueryService.getComments(feedId, page, size);
        return ResponseEntity.ok(ApiResponse.success(comments));
    }
}
