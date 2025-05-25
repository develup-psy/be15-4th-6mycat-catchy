package com.sixmycat.catchy.feature.feed.query.service;

import com.sixmycat.catchy.common.dto.PageResponse;
import com.sixmycat.catchy.feature.feed.query.dto.response.FeedCommentResponse;
import java.util.List;

public interface FeedCommentQueryService {
    PageResponse<FeedCommentResponse> getComments(Long feedId, int page, int size);
}
