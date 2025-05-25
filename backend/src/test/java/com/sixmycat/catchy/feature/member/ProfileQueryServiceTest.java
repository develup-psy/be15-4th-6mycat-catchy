package com.sixmycat.catchy.feature.member;

import com.sixmycat.catchy.common.dto.PageResponse;
import com.sixmycat.catchy.exception.BusinessException;
import com.sixmycat.catchy.exception.ErrorCode;
import com.sixmycat.catchy.feature.feed.query.dto.response.FeedSummaryResponse;
import com.sixmycat.catchy.feature.feed.query.service.FeedQueryService;
import com.sixmycat.catchy.feature.game.query.dto.GameRankingResponse;
import com.sixmycat.catchy.feature.game.query.service.GameQueryService;
import com.sixmycat.catchy.feature.member.command.domain.aggregate.Cat;
import com.sixmycat.catchy.feature.member.command.domain.aggregate.Member;
import com.sixmycat.catchy.feature.member.query.dto.response.CatResponse;
import com.sixmycat.catchy.feature.member.query.dto.response.FollowResponse;
import com.sixmycat.catchy.feature.member.query.dto.response.MemberResponse;
import com.sixmycat.catchy.feature.member.query.dto.response.MyProfileResponse;
import com.sixmycat.catchy.feature.member.query.mapper.ProfileMapper;
import com.sixmycat.catchy.feature.member.query.service.ProfileQueryServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class ProfileQueryServiceTest {

    @InjectMocks
    private ProfileQueryServiceImpl profileQueryService;

    @Mock
    private FeedQueryService feedQueryService;

    @Mock
    private GameQueryService gameQueryService;

    @Mock
    private ProfileMapper profileMapper;


    @Test
    @DisplayName("프로필 정보 조회 - 성공")
    void getMyProfile() {
        Long memberId = 1L;

        Member member = Member.builder()
                .id(memberId)
                .nickname("길동이")
                .statusMessage("안녕하세요!")
                .profileImage("default1.png")
                .cats(List.of(
                        new Cat("나비", "F", "코리안숏헤어", LocalDate.of(2020, 5, 1), 4, null)
                ))
                .build();

        // given
        given(profileMapper.findMemberById(memberId))
                .willReturn(new MemberResponse(
                        memberId,
                        "test@example.com",   // email
                        "길동이",              // nickname
                        "홍길동",              // name
                        "01012345678",        // contactNumber
                        "default1.png",       // profileImage
                        "KAKAO",              // social
                        null
                ));


        given(profileMapper.findFollowCountById(memberId))
                .willReturn(new FollowResponse(0, 0));

        given(profileMapper.findCatsByMemberId(memberId))
                .willReturn(List.of(
                        new CatResponse(1L, "나비", "F", "코리안숏헤어", LocalDate.of(2020, 5, 1), 4)
                ));

        @SuppressWarnings("unchecked")
        PageResponse<FeedSummaryResponse> mockedPageResponse = (PageResponse<FeedSummaryResponse>) mock(PageResponse.class);
        when(mockedPageResponse.getTotalElements()).thenReturn(0L);

        given(feedQueryService.getMyFeeds(memberId, 0, 1)).willReturn(mockedPageResponse);

        given(gameQueryService.getRanking(memberId, 1))
                .willThrow(new BusinessException(ErrorCode.GAME_SCORE_NOT_FOUND));

        // when
        MyProfileResponse response = profileQueryService.getMyProfile(memberId);

        // then
        assertThat(response.getMember().getId()).isEqualTo(memberId);
        assertThat(response.getMember().getNickname()).isEqualTo("길동이");
        assertThat(response.getMember().getProfileImage()).isEqualTo("default1.png");
        assertThat(response.getMember().getContactNumber()).isEqualTo("01012345678"); // 예시
        assertThat(response.getMember().getEmail()).isEqualTo("test@example.com");    // 예시
        assertThat(response.getMember().getSocial()).isEqualTo("KAKAO");              // 예시

        assertThat(response.getContents().getFeedCount()).isEqualTo(0);
        assertThat(response.getBadges().isTopRanker()).isFalse();
        assertThat(response.getBadges().isBirthday()).isFalse();

        assertThat(response.getCats()).hasSize(1);
        assertThat(response.getCats().get(0).getName()).isEqualTo("나비");

    }

    @Test
    @DisplayName("타인 프로필 조회 - 성공")
    void getOtherProfile_success() {
        // given
        Long targetMemberId = 2L;

        // 더미 응답 데이터 구성 (7개 인자 모두 전달)
        given(profileMapper.findMemberById(targetMemberId))
                .willReturn(new MemberResponse(
                        targetMemberId,
                        "other@example.com",    // email
                        "다른유저",              // nickname
                        "홍길순",                 // name
                        "01022223333",          // contactNumber
                        "other-profile.png",    // profileImage
                        "KAKAO",                // social
                        null
                ));

        given(profileMapper.findFollowCountById(targetMemberId))
                .willReturn(new FollowResponse(12, 5));

        given(profileMapper.findCatsByMemberId(targetMemberId))
                .willReturn(List.of(
                        new CatResponse(1L, "코코", "M", "러시안블루", LocalDate.of(2019, 3, 10), 5)
                ));

        @SuppressWarnings("unchecked")
        PageResponse<FeedSummaryResponse> mockPageResponse = mock(PageResponse.class);
        when(mockPageResponse.getTotalElements()).thenReturn(10L);

        given(feedQueryService.getMyFeeds(targetMemberId, 0, 1)).willReturn(mockPageResponse);

        given(gameQueryService.getRanking(targetMemberId, 1))
                .willReturn(new GameRankingResponse(3, 100, 92.5, List.of())); // 1등 아님

        // when
        MyProfileResponse response = profileQueryService.getOtherProfile(targetMemberId);

        // then
        assertThat(response.getMember().getId()).isEqualTo(targetMemberId); // 🔄 수정됨
        assertThat(response.getMember().getNickname()).isEqualTo("다른유저");
        assertThat(response.getMember().getProfileImage()).isEqualTo("other-profile.png");

        assertThat(response.getFollows().getFollowerCount()).isEqualTo(12);
        assertThat(response.getFollows().getFollowingCount()).isEqualTo(5);

        assertThat(response.getCats()).hasSize(1);
        assertThat(response.getCats().get(0).getName()).isEqualTo("코코");

        assertThat(response.getContents().getFeedCount()).isEqualTo(10);
        assertThat(response.getBadges().isTopRanker()).isFalse(); // 랭킹 1등 아님
    }

}


