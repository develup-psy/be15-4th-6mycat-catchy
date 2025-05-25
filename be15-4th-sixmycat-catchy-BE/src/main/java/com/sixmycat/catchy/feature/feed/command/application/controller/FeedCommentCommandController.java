package com.sixmycat.catchy.feature.feed.command.application.controller;

import com.sixmycat.catchy.common.dto.ApiResponse;
import com.sixmycat.catchy.feature.feed.command.application.dto.request.FeedCommentCreateRequest;
import com.sixmycat.catchy.feature.feed.command.application.service.FeedCommentCommandService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/feeds/comments")
public class FeedCommentCommandController {

    private final FeedCommentCommandService commentService;

    @PostMapping
    public ResponseEntity<ApiResponse<Long>> createComment(
            @RequestBody FeedCommentCreateRequest request,
            @AuthenticationPrincipal String memberId
    ) {
        Long id = commentService.createComment(request, Long.parseLong(memberId));
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteComment(
            @PathVariable("id") Long commentId,
            @AuthenticationPrincipal String memberId
    ) {
        commentService.deleteComment(commentId, Long.parseLong(memberId));
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body(ApiResponse.success(null));
    }
}
