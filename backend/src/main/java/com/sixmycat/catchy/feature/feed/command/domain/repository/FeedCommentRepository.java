package com.sixmycat.catchy.feature.feed.command.domain.repository;

import com.sixmycat.catchy.feature.feed.command.domain.aggregate.FeedComment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FeedCommentRepository extends JpaRepository<FeedComment, Long> {
    boolean existsById(Long id);

    // 자식 댓글 찾기 (재귀 삭제용)
    List<FeedComment> findAllByParentCommentId(Long parentCommentId);
}
