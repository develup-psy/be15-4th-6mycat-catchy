package com.sixmycat.catchy.feature.feed.command.application.service;

import com.sixmycat.catchy.feature.feed.command.application.dto.request.FeedCreateRequest;
import com.sixmycat.catchy.feature.feed.command.application.dto.request.FeedUpdateRequest;

public interface FeedCommandService {
    Long createFeed(FeedCreateRequest request, Long memberId);
    void updateFeed(Long feedId, FeedUpdateRequest request, Long memberId);
    void deleteFeed(Long feedId, Long memberId);
}