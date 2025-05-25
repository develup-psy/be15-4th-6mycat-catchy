package com.sixmycat.catchy.feature.game.command.domain.service;

import com.sixmycat.catchy.feature.game.command.domain.aggregate.GameScore;
import org.springframework.stereotype.Service;

@Service
public class GameDomainService {

    public boolean shouldSaveNewScore(GameScore existing, int newScore) {
        return (existing == null || newScore < existing.getScore());
    }
}
