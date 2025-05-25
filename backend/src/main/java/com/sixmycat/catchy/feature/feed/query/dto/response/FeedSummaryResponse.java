package com.sixmycat.catchy.feature.feed.query.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class FeedSummaryResponse {
    private Long feedId;
    private String thumbnailUrl; // 첫 번째 이미지
    private LocalDateTime createdAt;
}