package com.sixmycat.catchy.feature.member.query.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class BadgeResponse {
    private boolean isTopRanker;
    private boolean isInfluencer;
    private boolean isBirthday;
}
