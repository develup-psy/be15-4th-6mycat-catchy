package com.sixmycat.catchy.feature.block.command.application.service;

import com.sixmycat.catchy.exception.BusinessException;
import com.sixmycat.catchy.exception.ErrorCode;
import com.sixmycat.catchy.feature.block.command.domain.aggregate.Block;
import com.sixmycat.catchy.feature.block.command.domain.repository.BlockRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class BlockServiceImplTest {

    @Mock
    private BlockRepository blockRepository;

    @InjectMocks
    private BlockServiceImpl blockService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void blockUser_shouldSucceed_whenNotBlockedYet() {
        // given
        Long blockerId = 1L;
        Long blockedId = 2L;

        when(blockRepository.existsByBlockerIdAndBlockedId(blockerId, blockedId))
                .thenReturn(false);

        // when
        blockService.blockUser(blockerId, blockedId);

        // then
        verify(blockRepository).save(any(Block.class));
    }

    @Test
    void blockUser_shouldFail_whenBlockingSelf() {
        // given
        Long id = 1L;

        // when & then
        assertThatThrownBy(() -> blockService.blockUser(id, id))
                .isInstanceOf(BusinessException.class)
                .hasMessage(ErrorCode.CANNOT_BLOCK_SELF.getMessage());
    }

    @Test
    void blockUser_shouldFail_whenAlreadyBlocked() {
        // given
        Long blockerId = 1L;
        Long blockedId = 2L;

        when(blockRepository.existsByBlockerIdAndBlockedId(blockerId, blockedId))
                .thenReturn(true);

        // when & then
        assertThatThrownBy(() -> blockService.blockUser(blockerId, blockedId))
                .isInstanceOf(BusinessException.class)
                .hasMessage(ErrorCode.ALREADY_BLOCKED.getMessage());
    }

    @Test
    void unblockUser_shouldSucceed_whenBlockExists() {
        Long blockerId = 1L;
        Long blockedId = 2L;
        Block block = Block.create(blockerId, blockedId);

        when(blockRepository.findByBlockerIdAndBlockedId(blockerId, blockedId))
                .thenReturn(Optional.of(block));

        blockService.unblockUser(blockerId, blockedId);

        verify(blockRepository).delete(block);
    }

    @Test
    void unblockUser_shouldFail_whenBlockDoesNotExist() {
        Long blockerId = 1L;
        Long blockedId = 2L;

        when(blockRepository.findByBlockerIdAndBlockedId(blockerId, blockedId))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> blockService.unblockUser(blockerId, blockedId))
                .isInstanceOf(BusinessException.class)
                .hasMessage(ErrorCode.BLOCK_NOT_FOUND.getMessage());
    }
}
