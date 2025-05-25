package com.sixmycat.catchy.feature.member.query.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class MyProfileResponse {
    private MemberResponse member;
    private BadgeResponse badges;
    private FollowResponse follows;
    private FeedResponse contents;
    private List<CatResponse> cats;
}

