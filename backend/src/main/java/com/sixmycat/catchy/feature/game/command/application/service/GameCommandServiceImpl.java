package com.sixmycat.catchy.feature.game.command.application.service;

import com.sixmycat.catchy.feature.game.command.domain.aggregate.GameScore;
import com.sixmycat.catchy.feature.game.command.domain.repository.GameScoreRepository;
import com.sixmycat.catchy.feature.game.command.domain.service.GameDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class GameCommandServiceImpl implements GameCommandService {

    private final GameScoreRepository gameScoreRepository;
    private final GameDomainService gameScoreDomainService;

    @Override
    public void saveIfBestScore(Long memberId, int newScore) {

        Optional<GameScore> existing = gameScoreRepository.findByMemberId(memberId);

        //최단시간만 업데이트
        if (gameScoreDomainService.shouldSaveNewScore(existing.orElse(null), newScore)) {
            if (existing.isPresent()) {
                GameScore entity = existing.get();
                entity.updateScore(newScore);
            } else {
                GameScore newRecord = GameScore.of(memberId, newScore);
                gameScoreRepository.save(newRecord);
            }
        }
    }
}
