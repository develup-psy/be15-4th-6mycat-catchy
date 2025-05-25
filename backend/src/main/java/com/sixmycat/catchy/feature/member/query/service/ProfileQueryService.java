package com.sixmycat.catchy.feature.member.query.service;

import com.sixmycat.catchy.feature.member.query.dto.response.MyProfileResponse;

public interface ProfileQueryService {
    MyProfileResponse getMyProfile(Long memberId);
    MyProfileResponse getOtherProfile(Long memberId);
    MyProfileResponse getProfileByNickname(String nickname);
}
