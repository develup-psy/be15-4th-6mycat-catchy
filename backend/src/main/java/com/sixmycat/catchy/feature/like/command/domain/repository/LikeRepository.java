package com.sixmycat.catchy.feature.like.command.domain.repository;

import com.sixmycat.catchy.common.domain.TargetType;
import com.sixmycat.catchy.feature.like.command.domain.aggregate.Like;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {
    boolean existsByMemberIdAndTargetIdAndTargetType(Long memberId, Long targetId, TargetType targetType);

    Optional<Like> findByMemberIdAndTargetIdAndTargetType(Long memberId, Long targetId, TargetType targetType);
}