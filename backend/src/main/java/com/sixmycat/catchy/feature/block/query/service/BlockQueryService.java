package com.sixmycat.catchy.feature.block.query.service;

import com.sixmycat.catchy.common.dto.PageResponse;
import com.sixmycat.catchy.feature.block.query.dto.response.BlockResponse;

import java.util.List;

public interface BlockQueryService {
    PageResponse<BlockResponse> getBlockedUsers(Long blockerId, int page, int size);
}
