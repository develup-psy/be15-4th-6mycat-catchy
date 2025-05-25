package com.sixmycat.catchy.feature.block.query.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sixmycat.catchy.common.dto.PageResponse;
import com.sixmycat.catchy.exception.BusinessException;
import com.sixmycat.catchy.exception.ErrorCode;
import com.sixmycat.catchy.feature.block.query.dto.response.BlockResponse;
import com.sixmycat.catchy.feature.block.query.mapper.BlockQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BlockQueryServiceImpl implements BlockQueryService {

    private final BlockQueryRepository blockQueryRepository;

    @Override
    public PageResponse<BlockResponse> getBlockedUsers(Long blockerId, int page, int size) {
        PageHelper.startPage(page, size);
        PageInfo<BlockResponse> pageInfo = new PageInfo<>(blockQueryRepository.findBlockedUsers(blockerId));

        if (pageInfo.getList().isEmpty()) {
            throw new BusinessException(ErrorCode.BLOCK_NOT_FOUND);
        }

        return PageResponse.from(pageInfo);
    }
}
