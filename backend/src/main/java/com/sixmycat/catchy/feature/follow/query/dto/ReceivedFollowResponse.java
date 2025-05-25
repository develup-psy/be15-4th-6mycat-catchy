package com.sixmycat.catchy.feature.follow.query.dto;

public record ReceivedFollowResponse(
        Long memberId,
        String nickname,
        String profileImage
) {}

