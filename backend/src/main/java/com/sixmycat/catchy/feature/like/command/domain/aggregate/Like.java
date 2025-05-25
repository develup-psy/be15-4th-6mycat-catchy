package com.sixmycat.catchy.feature.like.command.domain.aggregate;

import com.sixmycat.catchy.common.domain.TargetType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "`like`") // MySQL 예약어 주의
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Like {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long memberId;

    private Long targetId;

    @Enumerated(EnumType.STRING)
    private TargetType targetType;

    private LocalDateTime likedAt = LocalDateTime.now();

    public static Like of(Long memberId, Long targetId, TargetType targetType) {
        return new Like(null, memberId, targetId, targetType, LocalDateTime.now());
    }
}