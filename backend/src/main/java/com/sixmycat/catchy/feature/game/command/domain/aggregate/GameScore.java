package com.sixmycat.catchy.feature.game.command.domain.aggregate;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "game_score")
public class GameScore {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long memberId;

    private int score;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Builder(access = AccessLevel.PRIVATE) // 외부에서 builder 직접 사용 금지

    private GameScore(Long memberId, int score, LocalDateTime createdAt) {
        this.memberId = memberId;
        this.score = score;
        this.createdAt = createdAt;
    }

    public static GameScore of(Long memberId, int score) {
        return GameScore.builder()
                .memberId(memberId)
                .score(score)
                .createdAt(LocalDateTime.now())
                .build();
    }

    public void updateScore(int newScore) {
        this.score = newScore;
    }
}
