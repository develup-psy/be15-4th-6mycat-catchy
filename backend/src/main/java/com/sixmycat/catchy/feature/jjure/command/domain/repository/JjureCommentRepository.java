package com.sixmycat.catchy.feature.jjure.command.domain.repository;

import com.sixmycat.catchy.feature.feed.command.domain.aggregate.FeedComment;
import com.sixmycat.catchy.feature.jjure.command.domain.aggregate.JjureComment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JjureCommentRepository extends JpaRepository<JjureComment, Long> {
    boolean existsById(Long id);
    List<JjureComment> findAllByParentCommentId(Long parentCommentId);
}