package com.sixmycat.catchy.feature.feed.command.infrastructure.repository;

import com.sixmycat.catchy.feature.feed.command.domain.aggregate.Feed;
import com.sixmycat.catchy.feature.feed.command.domain.repository.FeedRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaFeedRepository extends FeedRepository, JpaRepository<Feed, Long> {
}
