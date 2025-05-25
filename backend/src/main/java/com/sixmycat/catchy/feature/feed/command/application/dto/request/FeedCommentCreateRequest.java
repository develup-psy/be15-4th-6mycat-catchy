package com.sixmycat.catchy.feature.feed.command.application.dto.request;

import com.sixmycat.catchy.common.domain.TargetType;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FeedCommentCreateRequest {
    private Long targetId;
    private TargetType targetType;
    private Long parentCommentId;
    private String content;
}