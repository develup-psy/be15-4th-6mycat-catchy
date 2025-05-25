package com.sixmycat.catchy.feature.block.command.application.service;

import com.sixmycat.catchy.exception.BusinessException;
import com.sixmycat.catchy.exception.ErrorCode;
import com.sixmycat.catchy.feature.block.command.domain.aggregate.Block;
import com.sixmycat.catchy.feature.block.command.domain.repository.BlockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BlockServiceImpl implements BlockService {

    private final BlockRepository blockRepository;

    @Override
    @Transactional
    public void blockUser(Long blockerId, Long blockedId) {
        if (blockerId.equals(blockedId)) {
            throw new BusinessException(ErrorCode.CANNOT_BLOCK_SELF);
        }

        if (blockRepository.existsByBlockerIdAndBlockedId(blockerId, blockedId)) {
            throw new BusinessException(ErrorCode.ALREADY_BLOCKED);
        }

        blockRepository.save(Block.create(blockerId, blockedId));
    }

    @Override
    @Transactional
    public void unblockUser(Long blockerId, Long blockedId) {
        Block block = blockRepository.findByBlockerIdAndBlockedId(blockerId, blockedId)
                .orElseThrow(() -> new BusinessException(ErrorCode.BLOCK_NOT_FOUND));

        blockRepository.delete(block);
    }

}
