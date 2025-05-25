package com.sixmycat.catchy.feature.feed.command.domain.service;

import com.sixmycat.catchy.exception.BusinessException;
import com.sixmycat.catchy.exception.ErrorCode;
import com.sixmycat.catchy.feature.feed.command.domain.aggregate.Feed;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FeedDomainServiceImpl implements FeedDomainService {

    private static final int MAX_CONTENT_LENGTH = 500;
    private static final int MAX_IMAGE_COUNT = 5;

    @Override
    public void validateContentLength(String content) {
        if (content != null && content.length() > MAX_CONTENT_LENGTH) {
            throw new BusinessException(ErrorCode.FEED_CONTENT_TOO_LONG);
        }
    }

    @Override
    public void validateImageCount(List<String> imageUrls) {
        if (imageUrls == null || imageUrls.isEmpty()) {
            throw new BusinessException(ErrorCode.FEED_IMAGE_REQUIRED);
        }
        if (imageUrls.size() > MAX_IMAGE_COUNT) {
            throw new BusinessException(ErrorCode.FEED_IMAGE_TOO_MANY);
        }
    }

    @Override
    public void validateFeedOwner(Feed feed, Long memberId) {
        if (!feed.getMemberId().equals(memberId)) {
            throw new BusinessException(ErrorCode.UNAUTHORIZED_USER);
        }
    }
}