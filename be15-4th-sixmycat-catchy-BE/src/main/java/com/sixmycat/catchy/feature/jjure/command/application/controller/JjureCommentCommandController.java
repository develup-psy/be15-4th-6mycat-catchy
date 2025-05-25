package com.sixmycat.catchy.feature.jjure.command.application.controller;

import com.sixmycat.catchy.common.dto.ApiResponse;
import com.sixmycat.catchy.feature.jjure.command.application.dto.request.JjureCommentCreateRequest;
import com.sixmycat.catchy.feature.jjure.command.application.service.JjureCommentCommandService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/jjure/comments")
public class JjureCommentCommandController {

    private final JjureCommentCommandService commentService;

    @PostMapping
    public ResponseEntity<ApiResponse<Long>> createComment(
            @RequestBody JjureCommentCreateRequest request,
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
