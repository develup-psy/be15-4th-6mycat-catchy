package com.sixmycat.catchy.feature.jjure.query.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class JjureSummaryResponse {
    private Long jjureId;
    private String thumbnailUrl; // 첫 번째 이미지
    private LocalDateTime createdAt;
}