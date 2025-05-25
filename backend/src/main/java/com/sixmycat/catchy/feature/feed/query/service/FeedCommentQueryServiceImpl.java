package com.sixmycat.catchy.feature.feed.query.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sixmycat.catchy.common.dto.PageResponse;
import com.sixmycat.catchy.exception.BusinessException;
import com.sixmycat.catchy.exception.ErrorCode;
import com.sixmycat.catchy.feature.feed.query.dto.response.FeedCommentResponse;
import com.sixmycat.catchy.feature.feed.query.mapper.FeedQueryMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FeedCommentQueryServiceImpl implements FeedCommentQueryService {

    private final FeedQueryMapper feedQueryMapper;

    @Override
    public PageResponse<FeedCommentResponse> getComments(Long feedId, int page, int size) {
        if (!feedQueryMapper.existsByFeedId(feedId)) {
            throw new BusinessException(ErrorCode.FEED_NOT_FOUND);
        }

        PageHelper.startPage(page, size);
        PageInfo<FeedCommentResponse> pageInfo =
                new PageInfo<>(feedQueryMapper.findCommentsByFeedId(feedId));

        return PageResponse.from(pageInfo);
    }
}

