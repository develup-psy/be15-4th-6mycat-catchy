package com.sixmycat.catchy.feature.member.query.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class FollowResponse {
    private int followerCount;
    private int followingCount;
}

