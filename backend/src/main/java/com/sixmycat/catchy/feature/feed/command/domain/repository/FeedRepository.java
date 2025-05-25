package com.sixmycat.catchy.feature.feed.command.domain.repository;

import com.sixmycat.catchy.feature.feed.command.domain.aggregate.Feed;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface FeedRepository {
    Feed save(Feed feed);

    Optional<Feed> findById(Long feedId);

    @Query("SELECT f.memberId FROM Feed f WHERE f.id = :feedId")
    Long findMemberIdById(@Param("feedId") Long feedId);
}