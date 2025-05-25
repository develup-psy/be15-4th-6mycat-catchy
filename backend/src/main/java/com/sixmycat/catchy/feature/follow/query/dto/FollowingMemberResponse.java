package com.sixmycat.catchy.feature.follow.query.dto;

public record FollowingMemberResponse(
        Long memberId,
        String nickname,
        String profileImage
) {}
