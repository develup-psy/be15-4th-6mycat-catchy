package com.sixmycat.catchy.feature.feed.query.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sixmycat.catchy.common.dto.AuthorInfo;
import com.sixmycat.catchy.common.dto.CommentPreview;
import com.sixmycat.catchy.common.dto.PageResponse;
import com.sixmycat.catchy.exception.BusinessException;
import com.sixmycat.catchy.exception.ErrorCode;
import com.sixmycat.catchy.feature.feed.query.dto.response.*;
import com.sixmycat.catchy.feature.feed.query.mapper.FeedQueryMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class FeedQueryServiceImpl implements FeedQueryService {

    private final FeedQueryMapper feedQueryMapper;

    @Override
    public FeedDetailResponse getFeedDetail(Long feedId, Long userId) {
        // 1. 기본 정보
        FeedBaseInfo baseInfo = feedQueryMapper.findFeedBaseInfo(feedId)
                .orElseThrow(() -> new BusinessException(ErrorCode.FEED_NOT_FOUND));

        // 2. 이미지 리스트
        List<String> imageUrls = feedQueryMapper.findFeedImageUrls(feedId);

        // 3. 최근 댓글 1개
        CommentPreview commentPreview = feedQueryMapper.findLatestCommentPreview(feedId)
                .orElse(null);

        // 4. 좋아요 여부
        boolean isLiked = userId != null && feedQueryMapper.isFeedLikedByUser(feedId, userId);

        // 5. 작성자 본인 여부
        boolean isMine = userId != null && userId.equals(baseInfo.getAuthorId());

        // 6. 응답 조립
        return FeedDetailResponse.builder()
                .id(feedId)
                .author(AuthorInfo.builder()
                        .authorId(baseInfo.getAuthorId())
                        .nickname(baseInfo.getNickname())
                        .profileImageUrl(baseInfo.getProfileImageUrl())
                        .build())
                .imageUrls(imageUrls)
                .content(baseInfo.getContent())
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
    public PageResponse<FeedSummaryResponse> getMyFeeds(Long memberId, int page, int size) {
        PageHelper.startPage(page + 1, size); // PageHelper는 1부터 시작
        List<FeedSummaryResponse> list = feedQueryMapper.findMyFeeds(memberId);
        return PageResponse.from(new PageInfo<>(list));
    }

    @Override
    public PageResponse<FeedSummaryResponse> getLikedFeedList(Long memberId, int page, int size) {
        PageHelper.startPage(page + 1, size); // PageHelper는 1부터 시작
        List<FeedSummaryResponse> list = feedQueryMapper.findLikedFeeds(memberId);
        return PageResponse.from(new PageInfo<>(list));
    }

    @Override
    public PageResponse<FeedDetailResponse> getFeedList(Long userId, int page, int size) {
        PageHelper.startPage(page + 1, size); // PageHelper 사용 (1-based page index)

        // 🔥 userId를 전달해서 차단 필터 반영
        List<FeedBaseInfo> baseInfos = feedQueryMapper.findFeedList(userId);
        PageInfo<FeedBaseInfo> pageInfo = new PageInfo<>(baseInfos);

        List<FeedDetailResponse> result = baseInfos.stream().map(base -> {
            List<String> imageUrls = feedQueryMapper.findFeedImageUrls(base.getId());
            CommentPreview commentPreview = feedQueryMapper.findLatestCommentPreview(base.getId())
                    .orElse(null);
            boolean isLiked = userId != null && feedQueryMapper.isFeedLikedByUser(base.getId(), userId);
            boolean isMine = userId != null && userId.equals(base.getAuthorId());

            return FeedDetailResponse.builder()
                    .id(base.getId())
                    .author(new AuthorInfo(base.getAuthorId(), base.getNickname(), base.getProfileImageUrl()))
                    .imageUrls(imageUrls)
                    .content(base.getContent())
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
    public PageResponse<FeedSummaryResponse> getFeedsByMemberId(Long memberId, int page, int size) {
        PageHelper.startPage(page + 1, size);
        List<FeedSummaryResponse> list = feedQueryMapper.findFeedsByMemberId(memberId);
        return PageResponse.from(new PageInfo<>(list));
    }
}