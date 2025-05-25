package com.sixmycat.catchy.feature.game.command.application.service;

public interface GameCommandService {
    void saveIfBestScore(Long memberId, int newScore);
}
