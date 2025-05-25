package com.sixmycat.catchy.feature.block.query.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BlockResponse {
    private Long blockedId;
    private String blockedNickname;
    private LocalDateTime blockedAt;
    private String profileImage;
}