package com.sixmycat.catchy.feature.feed.command.application.service;

import com.sixmycat.catchy.feature.feed.command.domain.repository.FeedRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FeedInternalServiceImpl implements FeedInternalService {

    private final FeedRepository feedRepository;

    @Override
    public Long findMemberIdByFeedId(Long feedId) {
        return feedRepository.findMemberIdById(feedId);
    }
}
