package com.sixmycat.catchy.feature.block.command.application.service;

public interface BlockService {
    void blockUser(Long blockerId, Long blockedId);
    void unblockUser(Long blockerId, Long blockedId);
}
