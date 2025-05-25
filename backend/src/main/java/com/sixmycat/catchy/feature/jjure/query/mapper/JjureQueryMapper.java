package com.sixmycat.catchy.feature.jjure.query.mapper;

import com.sixmycat.catchy.common.dto.CommentPreview;
import com.sixmycat.catchy.feature.jjure.query.dto.response.JjureBaseInfo;
import com.sixmycat.catchy.feature.jjure.query.dto.response.JjureCommentResponse;
import com.sixmycat.catchy.feature.jjure.query.dto.response.JjureSummaryResponse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

@Mapper
public interface JjureQueryMapper {

    List<JjureCommentResponse> findCommentsByJjureId(@Param("jjureId") Long jjureId);

    boolean existsByJjureId(@Param("jjureId") Long jjureId);

    Optional<JjureBaseInfo> findJjureBaseInfo(@Param("jjureId") Long jjureId);

    Optional<CommentPreview> findLatestCommentPreview(@Param("jjureId") Long jjureId);

    boolean isJjureLikedByUser(@Param("jjureId") Long jjureId, @Param("userId") Long userId);

    List<JjureBaseInfo> findJjureList(Long memberId);

    List<JjureSummaryResponse> findLikedJjures(Long memberId);

    List<JjureSummaryResponse> findMyJjures(Long memberId);

    List<JjureSummaryResponse> findJjuresByMemberId(Long memberId);
}
