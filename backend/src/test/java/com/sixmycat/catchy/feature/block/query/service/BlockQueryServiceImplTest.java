package com.sixmycat.catchy.feature.block.query.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sixmycat.catchy.common.dto.PageResponse;
import com.sixmycat.catchy.feature.block.query.dto.response.BlockResponse;
import com.sixmycat.catchy.feature.block.query.mapper.BlockQueryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class BlockQueryServiceImplTest {

    @Mock
    private BlockQueryRepository blockQueryRepository;

    @InjectMocks
    private BlockQueryServiceImpl blockQueryService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getBlockedUsers_shouldReturnPagedBlockedUsers() {
        // given
        Long blockerId = 1L;
        int page = 1;
        int size = 10;

        List<BlockResponse> mockBlockedList = List.of(
                BlockResponse.builder()
                        .blockedId(2L)
                        .blockedNickname("UserTwo")
                        .blockedAt(LocalDateTime.now().minusDays(1))
                        .build(),
                BlockResponse.builder()
                        .blockedId(3L)
                        .blockedNickname("UserThree")
                        .blockedAt(LocalDateTime.now().minusDays(2))
                        .build()
        );

        when(blockQueryRepository.findBlockedUsers(blockerId)).thenReturn(mockBlockedList);

        // when
        PageHelper.startPage(page, size); // PageHelper context 설정
        PageResponse<BlockResponse> result = blockQueryService.getBlockedUsers(blockerId, page, size);

        // then
        assertThat(result.getContent()).hasSize(2);
        assertThat(result.getContent().get(0).getBlockedId()).isEqualTo(2L);
        assertThat(result.getContent().get(1).getBlockedNickname()).isEqualTo("UserThree");

        verify(blockQueryRepository, times(1)).findBlockedUsers(blockerId);
    }
}
