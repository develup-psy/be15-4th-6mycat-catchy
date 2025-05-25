package com.sixmycat.catchy.feature.game.query.controller;

import com.sixmycat.catchy.common.dto.ApiResponse;
import com.sixmycat.catchy.feature.game.query.dto.GameRankingResponse;
import com.sixmycat.catchy.feature.game.query.dto.GameScoreResponse;
import com.sixmycat.catchy.feature.game.query.service.GameQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/games/scores")
@RequiredArgsConstructor
public class GameQueryController {

    private final GameQueryService gameQueryService;

    /* 본인 최고 점수 조회 */
    @GetMapping("/")
    public ResponseEntity<ApiResponse<GameScoreResponse>> getMyBestScore(
            @AuthenticationPrincipal String memberId
    ) {
        GameScoreResponse response = gameQueryService.getMyBestScore(Long.parseLong(memberId));
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    /* 내 랭킹 + 전체 상위 10위 조회 */
    @GetMapping("/ranking")
    public ResponseEntity<ApiResponse<GameRankingResponse>> getRanking(
            @AuthenticationPrincipal String memberId
    ) {
        GameRankingResponse ranking = gameQueryService.getRanking(Long.parseLong(memberId), 10);
        return ResponseEntity.ok(ApiResponse.success(ranking));
    }
}
