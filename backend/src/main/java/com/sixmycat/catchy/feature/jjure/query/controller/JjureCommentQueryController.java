package com.sixmycat.catchy.feature.jjure.query.controller;

import com.sixmycat.catchy.common.dto.ApiResponse;
import com.sixmycat.catchy.common.dto.PageResponse;
import com.sixmycat.catchy.feature.jjure.query.dto.response.JjureCommentResponse;
import com.sixmycat.catchy.feature.jjure.query.service.JjureCommentQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/jjure")
public class JjureCommentQueryController {

    private final JjureCommentQueryService commentQueryService;

    @GetMapping("/{jjureId}/comments")
    public ResponseEntity<ApiResponse<PageResponse<JjureCommentResponse>>> getComments(
            @PathVariable Long jjureId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        PageResponse<JjureCommentResponse> comments = commentQueryService.getComments(jjureId, page, size);
        return ResponseEntity.ok(ApiResponse.success(comments));
    }
}
