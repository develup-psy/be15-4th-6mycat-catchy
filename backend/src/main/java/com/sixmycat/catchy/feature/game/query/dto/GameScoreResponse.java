package com.sixmycat.catchy.feature.game.query.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class GameScoreResponse {
    private Long id;
    private int score;
    private LocalDateTime createdAt;
}
