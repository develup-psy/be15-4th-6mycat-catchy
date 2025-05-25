package com.sixmycat.catchy.feature.feed.query.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class FeedCommentResponse {
    private Long commentId;
    private Long memberId;
    private String nickname;
    private String content;
    private LocalDateTime createdAt;
    private Long parentCommentId;
}
