package com.sixmycat.catchy.feature.game.query.service;

import com.sixmycat.catchy.exception.BusinessException;
import com.sixmycat.catchy.exception.ErrorCode;
import com.sixmycat.catchy.feature.game.command.domain.aggregate.GameScore;
import com.sixmycat.catchy.feature.game.command.domain.repository.GameScoreRepository;
import com.sixmycat.catchy.feature.game.query.dto.RankerDto;
import com.sixmycat.catchy.feature.game.query.dto.GameRankingResponse;
import com.sixmycat.catchy.feature.game.query.dto.GameScoreResponse;
import com.sixmycat.catchy.feature.game.query.mapper.GameScoreMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GameQueryServiceImpl implements GameQueryService {

    private final GameScoreRepository gameScoreRepository;
    private final GameScoreMapper gameScoreMapper;

    /* 내 최고 점수 조회 */
    @Override
    public GameScoreResponse getMyBestScore(Long memberId) {

        GameScore gameScore = gameScoreRepository.findByMemberId(memberId).orElseThrow(
                () -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND)
        );

        return new GameScoreResponse(gameScore.getId(),gameScore.getScore(), gameScore.getCreatedAt());
    }

    /* 랭킹 조회 */
    @Override
    public GameRankingResponse getRanking(Long memberId, int limit) {

        int myScore = gameScoreMapper.findScoreByMemberId(memberId)
                .orElseThrow(() -> new BusinessException(ErrorCode.GAME_SCORE_NOT_FOUND));

        int myRank = gameScoreMapper.findRankByScore(myScore);

        int totalUserCount = gameScoreMapper.countAllUsers();

        double topPercentage = ((double)(myRank) / totalUserCount) * 100;

        List<RankerDto> topRankers = gameScoreMapper.findTopRankers(limit).stream()
                .map(r -> new RankerDto(r.getId(),r.getRank(), r.getNickname(), r.getScore()))
                .toList();

        return new GameRankingResponse(myRank, myScore, topPercentage, topRankers);
    }
}
