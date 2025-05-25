package com.sixmycat.catchy.feature.jjure.command.application.service;

import com.sixmycat.catchy.feature.jjure.command.application.dto.request.JjureCommentCreateRequest;

public interface JjureCommentCommandService {
    Long createComment(JjureCommentCreateRequest request, Long memberId);
    void deleteComment(Long commentId, Long memberId);
}