package com.sixmycat.catchy.feature.member.query.mapper;

import com.sixmycat.catchy.feature.member.query.dto.response.CatResponse;
import com.sixmycat.catchy.feature.member.query.dto.response.FollowResponse;
import com.sixmycat.catchy.feature.member.query.dto.response.MemberResponse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ProfileMapper {

    MemberResponse findMemberById(@Param("memberId") Long memberId);

    FollowResponse findFollowCountById(@Param("memberId") Long memberId);

    List<CatResponse> findCatsByMemberId(@Param("memberId") Long memberId);

    MemberResponse findMemberByNickname(@Param("nickname") String nickname);
}
