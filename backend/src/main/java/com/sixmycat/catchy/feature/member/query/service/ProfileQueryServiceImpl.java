package com.sixmycat.catchy.feature.member.query.service;

import com.sixmycat.catchy.exception.BusinessException;
import com.sixmycat.catchy.exception.ErrorCode;
import com.sixmycat.catchy.feature.feed.query.service.FeedQueryService;
import com.sixmycat.catchy.feature.game.query.dto.GameRankingResponse;
import com.sixmycat.catchy.feature.game.query.service.GameQueryService;
import com.sixmycat.catchy.feature.member.query.dto.response.*;
import com.sixmycat.catchy.feature.member.query.mapper.ProfileMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ProfileQueryServiceImpl implements ProfileQueryService {

    private final ProfileMapper profileMapper;
    private final FeedQueryService feedQueryService;
    private final GameQueryService gameQueryService;

    @Override
    public MyProfileResponse getMyProfile(Long memberId) {
        return buildProfileResponse(memberId, true);
    }

    @Override
    public MyProfileResponse getOtherProfile(Long memberId) {
        return buildProfileResponse(memberId, false);
    }

    @Override
    public MyProfileResponse getProfileByNickname(String nickname) {
        MemberResponse member = profileMapper.findMemberByNickname(nickname);
        if (member == null) {
            throw new BusinessException(ErrorCode.MEMBER_NOT_FOUND);
        }

        return buildProfileResponse(member.getId(), false);
    }



    private MyProfileResponse buildProfileResponse(Long memberId, boolean isMine) {
        MemberResponse member = profileMapper.findMemberById(memberId);
        if (member == null) {
            throw new BusinessException(ErrorCode.MEMBER_NOT_FOUND);
        }

        FollowResponse follows = profileMapper.findFollowCountById(memberId);
        List<CatResponse> catResponses = profileMapper.findCatsByMemberId(memberId);

        boolean isBirthday = catResponses.stream().anyMatch(cat ->
                cat.getBirthDate() != null &&
                        cat.getBirthDate().getMonthValue() == LocalDate.now().getMonthValue() &&
                        cat.getBirthDate().getDayOfMonth() == LocalDate.now().getDayOfMonth()
        );

        boolean isTopRanker;
        try {
            GameRankingResponse ranking = gameQueryService.getRanking(memberId, 1);
            isTopRanker = ranking.getMyRank() == 1;
        } catch (BusinessException e) {
            isTopRanker = false;
        }

        int feedCount = (int) feedQueryService.getMyFeeds(memberId, 0, 1).getTotalElements();

        BadgeResponse badges = BadgeResponse.builder()
                .isTopRanker(isTopRanker)
                .isInfluencer(false)
                .isBirthday(isBirthday)
                .build();

        FeedResponse contents = FeedResponse.builder()
                .feedCount(feedCount)
                .build();

        return MyProfileResponse.builder()
                .member(member)
                .follows(follows)
                .badges(badges)
                .contents(contents)
                .cats(catResponses)
                .build();
    }
}


