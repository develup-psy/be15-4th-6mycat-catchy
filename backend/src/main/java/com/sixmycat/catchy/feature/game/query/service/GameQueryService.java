package com.sixmycat.catchy.feature.game.query.service;

import com.sixmycat.catchy.feature.game.query.dto.GameRankingResponse;
import com.sixmycat.catchy.feature.game.query.dto.GameScoreResponse;

public interface GameQueryService {
    GameScoreResponse getMyBestScore(Long memberId);
    GameRankingResponse getRanking(Long memberId, int limit);
}
