package com.sixmycat.catchy.feature.like.command.application.dto.request;

import com.sixmycat.catchy.common.domain.TargetType;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LikeRequest {
    Long targetId;
    TargetType targetType;
}
