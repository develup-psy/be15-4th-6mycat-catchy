package com.sixmycat.catchy.feature.block.command.domain.aggregate;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "block")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Block {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "blocker_id", nullable = false)
    private Long blockerId;

    @Column(name = "blocked_id", nullable = false)
    private Long blockedId;

    @Column(name = "blocked_at", nullable = false)
    private LocalDateTime blockedAt;

    public static Block create(Long blockerId, Long blockedId) {
        Block block = new Block();
        block.blockerId = blockerId;
        block.blockedId = blockedId;
        block.blockedAt = LocalDateTime.now();
        return block;
    }
}
