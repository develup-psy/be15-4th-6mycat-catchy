package com.sixmycat.catchy.feature.game.query.service;

import com.sixmycat.catchy.exception.BusinessException;
import com.sixmycat.catchy.exception.ErrorCode;
import com.sixmycat.catchy.feature.game.command.domain.aggregate.GameScore;
import com.sixmycat.catchy.feature.game.command.domain.repository.GameScoreRepository;
import com.sixmycat.catchy.feature.game.query.dto.GameRankingResponse;
import com.sixmycat.catchy.feature.game.query.dto.GameScoreResponse;
import com.sixmycat.catchy.feature.game.query.dto.RankerDto;
import com.sixmycat.catchy.feature.game.query.mapper.GameScoreMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class GameQueryServiceImplTest {

    @Mock
    private GameScoreRepository gameScoreRepository;

    @Mock
    private GameScoreMapper gameScoreMapper;

    @InjectMocks
    private GameQueryServiceImpl gameQueryService;

    public GameQueryServiceImplTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Nested
    @DisplayName("getMyBestScore 테스트")
    class GetMyBestScoreTest {

        @Test
        @DisplayName("존재하는 memberId로 최고 점수 조회 성공")
        void getMyBestScore_success() {
            // given
            Long memberId = 1L;
            GameScore gameScore =  GameScore.of(memberId,1500);
            when(gameScoreRepository.findByMemberId(memberId)).thenReturn(Optional.of(gameScore));

            // when
            GameScoreResponse response = gameQueryService.getMyBestScore(memberId);

            // then
            assertThat(response).isNotNull();
            assertThat(response.getScore()).isEqualTo(1500);
            verify(gameScoreRepository, times(1)).findByMemberId(memberId);
        }

        @Test
        @DisplayName("존재하지 않는 memberId로 조회 시 예외 발생")
        void getMyBestScore_fail_memberNotFound() {
            // given
            Long memberId = 999L;
            when(gameScoreRepository.findByMemberId(memberId)).thenReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> gameQueryService.getMyBestScore(memberId))
                    .isInstanceOf(BusinessException.class)
                    .hasMessageContaining(ErrorCode.MEMBER_NOT_FOUND.getMessage());

            verify(gameScoreRepository, times(1)).findByMemberId(memberId);
        }
    }

    @Nested
    @DisplayName("getRanking 테스트")
    class GetRankingTest {

        @Test
        @DisplayName("랭킹 정상 조회 성공")
        void getRanking_success() {
            // given
            Long memberId = 1L;
            int myScore = 1200;
            int myRank = 3;
            int limit = 10;

            when(gameScoreMapper.findScoreByMemberId(memberId)).thenReturn(Optional.of(myScore));
            when(gameScoreMapper.findRankByScore(myScore)).thenReturn(myRank);
            when(gameScoreMapper.findTopRankers(limit)).thenReturn(List.of(
                    new RankerDto(1,1, "user1", 1500),
                    new RankerDto(2,2, "user2", 1400),
                    new RankerDto(3, 3, "user3", 1300)
            ));

            // when
            GameRankingResponse response = gameQueryService.getRanking(memberId, limit);

            // then
            assertThat(response).isNotNull();
            assertThat(response.getMyScore()).isEqualTo(myScore);
            assertThat(response.getMyRank()).isEqualTo(myRank);
            assertThat(response.getTopRankers()).hasSize(3);

            verify(gameScoreMapper, times(1)).findScoreByMemberId(memberId);
            verify(gameScoreMapper, times(1)).findRankByScore(myScore);
            verify(gameScoreMapper, times(1)).findTopRankers(limit);
        }

        @Test
        @DisplayName("존재하지 않는 점수일 경우 예외 발생")
        void getRanking_fail_scoreNotFound() {
            // given
            Long memberId = 999L;
            int limit = 10;

            when(gameScoreMapper.findScoreByMemberId(memberId)).thenReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> gameQueryService.getRanking(memberId, limit))
                    .isInstanceOf(BusinessException.class)
                    .hasMessageContaining(ErrorCode.GAME_SCORE_NOT_FOUND.getMessage());

            verify(gameScoreMapper, times(1)).findScoreByMemberId(memberId);
            verify(gameScoreMapper, never()).findRankByScore(anyInt());
            verify(gameScoreMapper, never()).findTopRankers(anyInt());
        }
    }
}
