package com.sixmycat.catchy.feature.feed.command.domain.service;

import com.sixmycat.catchy.feature.feed.command.domain.aggregate.Feed;

import java.util.List;

public interface FeedDomainService {

    void validateContentLength(String content);

    void validateImageCount(List<String> imageUrls);

    void validateFeedOwner(Feed feed, Long memberId);
}