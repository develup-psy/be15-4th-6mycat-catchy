package com.sixmycat.catchy.feature.jjure.query.service;

import com.sixmycat.catchy.common.dto.PageResponse;
import com.sixmycat.catchy.feature.jjure.query.dto.response.JjureCommentResponse;

public interface JjureCommentQueryService {
    PageResponse<JjureCommentResponse> getComments(Long jjureId, int page, int size);
}
