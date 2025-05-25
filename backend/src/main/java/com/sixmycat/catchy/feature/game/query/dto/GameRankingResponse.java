package com.sixmycat.catchy.feature.game.query.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class GameRankingResponse {
    private final int myRank;
    private final int myScore;
    private final double topPercentage;
    private final List<RankerDto> topRankers;
}

