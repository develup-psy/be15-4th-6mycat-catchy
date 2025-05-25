package com.sixmycat.catchy.feature.block.query.controller;

import com.sixmycat.catchy.common.dto.ApiResponse;
import com.sixmycat.catchy.common.dto.PageResponse;
import com.sixmycat.catchy.feature.block.query.dto.response.BlockResponse;
import com.sixmycat.catchy.feature.block.query.service.BlockQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/blocks")
public class BlockQueryController {

    private final BlockQueryService blockQueryService;

    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<BlockResponse>>> getBlockedUsers(
            @AuthenticationPrincipal String memberId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        PageResponse<BlockResponse> response = blockQueryService.getBlockedUsers(Long.parseLong(memberId), page, size);
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}
