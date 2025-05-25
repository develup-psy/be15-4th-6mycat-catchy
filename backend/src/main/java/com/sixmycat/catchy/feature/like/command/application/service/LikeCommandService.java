package com.sixmycat.catchy.feature.like.command.application.service;

import com.sixmycat.catchy.common.domain.TargetType;

public interface LikeCommandService {
    void like(Long memberId, Long targetId, TargetType targetType);
    void unlike(Long memberId, Long targetId, TargetType targetType);
    Long resolveReceiverId(Long targetId, TargetType targetType);
}
