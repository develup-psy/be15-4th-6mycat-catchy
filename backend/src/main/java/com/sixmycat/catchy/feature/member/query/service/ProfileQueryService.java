package com.sixmycat.catchy.feature.member.query.service;

import com.sixmycat.catchy.feature.member.query.dto.response.MyProfileResponse;
import com.sixmycat.catchy.feature.member.query.dto.response.OtherProfileResponse;

public interface ProfileQueryService {
    MyProfileResponse getMyProfile(Long memberId);
    OtherProfileResponse getOtherProfile(Long myId,Long memberId);
    MyProfileResponse getProfileByNickname(String nickname);
}
