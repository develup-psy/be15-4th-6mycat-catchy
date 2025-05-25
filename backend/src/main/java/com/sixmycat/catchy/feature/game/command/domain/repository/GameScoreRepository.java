package com.sixmycat.catchy.feature.game.command.domain.repository;

import com.sixmycat.catchy.feature.game.command.domain.aggregate.GameScore;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GameScoreRepository extends JpaRepository<GameScore, Long> {
    Optional<GameScore> findByMemberId(Long memberId);

    Optional<Object> findScoreByMemberId(Long memberId);
}
