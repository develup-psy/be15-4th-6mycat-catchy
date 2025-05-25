package com.sixmycat.catchy.feature.jjure.query.service;

import com.sixmycat.catchy.common.dto.PageResponse;
import com.sixmycat.catchy.feature.jjure.query.dto.response.JjureDetailResponse;
import com.sixmycat.catchy.feature.jjure.query.dto.response.JjureSummaryResponse;

public interface JjureQueryService {
    JjureDetailResponse getJjureDetail(Long jjureId, Long userId);

    PageResponse<JjureDetailResponse> getJjureList(Long userId, int page, int size);

    PageResponse<JjureSummaryResponse> getLikedJjureList(Long memberId, int page, int size);

    PageResponse<JjureSummaryResponse> getMyJjureList(Long memberId, int page, int size);

    PageResponse<JjureSummaryResponse> getJjuresByMemberId(Long memberId, int page, int size);
}