package com.sixmycat.catchy.feature.jjure.command.application.dto.request;

import com.sixmycat.catchy.common.domain.TargetType;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class JjureCommentCreateRequest {
    private Long targetId;
    private TargetType targetType;
    private Long parentCommentId;
    private String content;
}