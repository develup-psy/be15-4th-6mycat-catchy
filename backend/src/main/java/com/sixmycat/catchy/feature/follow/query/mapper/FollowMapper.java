package com.sixmycat.catchy.feature.follow.query.mapper;

import com.sixmycat.catchy.feature.follow.query.dto.FollowingMemberResponse;
import com.sixmycat.catchy.feature.follow.query.dto.ReceivedFollowResponse;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface FollowMapper {

    List<FollowingMemberResponse> findFollowingMembers(Long memberId);

    List<ReceivedFollowResponse> findReceivedFollows(Long memberId);
}
