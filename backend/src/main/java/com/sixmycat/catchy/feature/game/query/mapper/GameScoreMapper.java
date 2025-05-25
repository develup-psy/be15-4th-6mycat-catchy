package com.sixmycat.catchy.feature.game.query.mapper;

import com.sixmycat.catchy.feature.game.query.dto.RankerDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Optional;

@Mapper
public interface GameScoreMapper {
    Optional<Integer> findScoreByMemberId(Long memberId);
    int findRankByScore(int score);
    List<RankerDto> findTopRankers(int limit);

    int countAllUsers();
}
