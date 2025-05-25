package com.sixmycat.catchy.feature.feed.command.application.service;

import com.sixmycat.catchy.feature.feed.command.application.dto.request.FeedCommentCreateRequest;

public interface FeedCommentCommandService {
    Long createComment(FeedCommentCreateRequest request, Long memberId);
    void deleteComment(Long commentId, Long memberId);
}