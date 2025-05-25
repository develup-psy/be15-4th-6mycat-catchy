package com.sixmycat.catchy.feature.jjure.query.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sixmycat.catchy.common.dto.PageResponse;
import com.sixmycat.catchy.exception.BusinessException;
import com.sixmycat.catchy.exception.ErrorCode;
import com.sixmycat.catchy.common.dto.AuthorInfo;
import com.sixmycat.catchy.common.dto.CommentPreview;
import com.sixmycat.catchy.feature.jjure.query.dto.response.JjureBaseInfo;
import com.sixmycat.catchy.feature.jjure.query.dto.response.JjureDetailResponse;
import com.sixmycat.catchy.feature.jjure.query.dto.response.JjureSummaryResponse;
import com.sixmycat.catchy.feature.jjure.query.mapper.JjureQueryMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class JjureQueryServiceImpl implements JjureQueryService {

    private final JjureQueryMapper jjureQueryMapper;

    @Override
    public JjureDetailResponse getJjureDetail(Long jjureId, Long userId) {
        // 1. 기본 정보
        JjureBaseInfo baseInfo = jjureQueryMapper.findJjureBaseInfo(jjureId)
                .orElseThrow(() -> new BusinessException(ErrorCode.JJURE_NOT_FOUND));

        // 2. 최근 댓글 1개
        CommentPreview commentPreview = jjureQueryMapper.findLatestCommentPreview(jjureId)
                .orElse(null);

        // 3. 좋아요 여부
        boolean isLiked = userId != null && jjureQueryMapper.isJjureLikedByUser(jjureId, userId);

        // 4. 작성자 본인 여부
        boolean isMine = userId != null && userId.equals(baseInfo.getAuthorId());

        // 5. 응답 조립
        return JjureDetailResponse.builder()
                .id(jjureId)
                .author(AuthorInfo.builder()
                        .authorId(baseInfo.getAuthorId())
                        .nickname(baseInfo.getNickname())
                        .profileImageUrl(baseInfo.getProfileImageUrl())
                        .build())
                .fileKey(baseInfo.getFileKey())
                .caption(baseInfo.getCaption())
                .musicUrl(baseInfo.getMusicUrl())
                .likeCount(baseInfo.getLikeCount())
                .commentCount(baseInfo.getCommentCount())
                .commentPreview(commentPreview)
                .isLiked(isLiked)
                .isMine(isMine)
                .createdAt(baseInfo.getCreatedAt())
                .build();
    }
    @Override
    public PageResponse<JjureDetailResponse> getJjureList(Long userId, int page, int size) {
        PageHelper.startPage(page + 1, size); // PageHelper는 1부터 시작 (Spring은 0부터 시작)

        List<JjureBaseInfo> baseInfos = jjureQueryMapper.findJjureList(userId);
        PageInfo<JjureBaseInfo> pageInfo = new PageInfo<>(baseInfos);

        List<JjureDetailResponse> result = baseInfos.stream().map(base -> {
            CommentPreview commentPreview = jjureQueryMapper.findLatestCommentPreview(base.getId())
                    .orElse(null);
            boolean isLiked = userId != null && jjureQueryMapper.isJjureLikedByUser(base.getId(), userId);
            boolean isMine = userId != null && userId.equals(base.getAuthorId());

            return JjureDetailResponse.builder()
                    .id(base.getId())
                    .author(new AuthorInfo(base.getAuthorId(), base.getNickname(), base.getProfileImageUrl()))
                    .fileKey(base.getFileKey())
                    .caption(base.getCaption())
                    .musicUrl(base.getMusicUrl())
                    .likeCount(base.getLikeCount())
                    .commentCount(base.getCommentCount())
                    .commentPreview(commentPreview)
                    .isLiked(isLiked)
                    .isMine(isMine)
                    .createdAt(base.getCreatedAt())
                    .build();
        }).toList();

        return PageResponse.from(pageInfo, result);
    }

    @Override
    public PageResponse<JjureSummaryResponse> getLikedJjureList(Long memberId, int page, int size) {
        PageHelper.startPage(page + 1, size); // PageHelper는 1부터 시작
        List<JjureSummaryResponse> list = jjureQueryMapper.findLikedJjures(memberId);
        return PageResponse.from(new PageInfo<>(list));
    }

    @Override
    public PageResponse<JjureSummaryResponse> getMyJjureList(Long memberId, int page, int size) {
        PageHelper.startPage(page + 1, size); // PageHelper는 1부터 시작
        List<JjureSummaryResponse> list = jjureQueryMapper.findMyJjures(memberId);
        return PageResponse.from(new PageInfo<>(list));
    }

    @Override
    public PageResponse<JjureSummaryResponse> getJjuresByMemberId(Long memberId, int page, int size) {
        PageHelper.startPage(page + 1, size); // PageHelper는 1부터 시작
        List<JjureSummaryResponse> list = jjureQueryMapper.findJjuresByMemberId(memberId);
        return PageResponse.from(new PageInfo<>(list));
    }
}
