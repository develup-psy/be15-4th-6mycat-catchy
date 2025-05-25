package com.sixmycat.catchy.feature.follow.query.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sixmycat.catchy.common.dto.PageResponse;
import com.sixmycat.catchy.feature.follow.query.dto.FollowingMemberResponse;
import com.sixmycat.catchy.feature.follow.query.dto.ReceivedFollowResponse;
import com.sixmycat.catchy.feature.follow.query.mapper.FollowMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FollowQueryServiceImpl implements FollowQueryService {

    private final FollowMapper followMapper;

    @Override
    public PageResponse<FollowingMemberResponse> getFollowingList(Long memberId, Pageable pageable) {
        PageHelper.startPage(pageable.getPageNumber() + 1, pageable.getPageSize());
        List<FollowingMemberResponse> list = followMapper.findFollowingMembers(memberId);
        PageInfo<FollowingMemberResponse> pageInfo = new PageInfo<>(list);
        return PageResponse.from(pageInfo);
    }

    @Override
    public PageResponse<ReceivedFollowResponse> getReceivedFollows(Long memberId, Pageable pageable) {
        PageHelper.startPage(pageable.getPageNumber() + 1, pageable.getPageSize());
        List<ReceivedFollowResponse> list = followMapper.findReceivedFollows(memberId);
        PageInfo<ReceivedFollowResponse> pageInfo = new PageInfo<>(list);
        return PageResponse.from(pageInfo);
    }

}

