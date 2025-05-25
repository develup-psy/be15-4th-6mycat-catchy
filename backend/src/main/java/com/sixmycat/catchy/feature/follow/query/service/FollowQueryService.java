package com.sixmycat.catchy.feature.follow.query.service;

import com.sixmycat.catchy.common.dto.PageResponse;
import com.sixmycat.catchy.feature.follow.query.dto.FollowingMemberResponse;
import com.sixmycat.catchy.feature.follow.query.dto.ReceivedFollowResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface FollowQueryService {

    PageResponse<FollowingMemberResponse> getFollowingList(Long memberId, Pageable pageable);

    PageResponse<ReceivedFollowResponse> getReceivedFollows(Long memberId, Pageable pageable);
}
