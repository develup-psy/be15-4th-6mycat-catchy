package com.sixmycat.catchy.feature.game.command.application.controller;

import com.sixmycat.catchy.common.dto.ApiResponse;
import com.sixmycat.catchy.feature.game.command.application.dto.request.GameScoreRequest;
import com.sixmycat.catchy.feature.game.command.application.service.GameCommandService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/games/scores")
@RequiredArgsConstructor
public class GameCommandController {

    private final GameCommandService gameScoreService;

    /* 게임 점수 저장 핸들러 */
    @PostMapping("")
    public ResponseEntity<ApiResponse<Void>> saveGameScore(
            @RequestBody GameScoreRequest request,
            @AuthenticationPrincipal String memberId
    ) {
        gameScoreService.saveIfBestScore(Long.parseLong(memberId), request.getScore());
        return ResponseEntity.ok(ApiResponse.success(null));
    }
}

