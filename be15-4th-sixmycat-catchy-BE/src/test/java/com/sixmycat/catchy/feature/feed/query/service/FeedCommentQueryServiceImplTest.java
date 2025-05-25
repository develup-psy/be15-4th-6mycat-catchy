package com.sixmycat.catchy.feature.feed.query.service;

import com.github.pagehelper.PageHelper;
import com.sixmycat.catchy.common.dto.PageResponse;
import com.sixmycat.catchy.exception.BusinessException;
import com.sixmycat.catchy.exception.ErrorCode;
import com.sixmycat.catchy.feature.feed.query.dto.response.FeedCommentResponse;
import com.sixmycat.catchy.feature.feed.query.mapper.FeedQueryMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class FeedCommentQueryServiceImplTest {

    @Mock
    private FeedQueryMapper feedQueryMapper;

    @InjectMocks
    private FeedCommentQueryServiceImpl feedCommentQueryService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getComments_shouldReturnPagedComments() {
        // given
        Long feedId = 1L;
        int page = 1;
        int size = 10;

        List<FeedCommentResponse> mockComments = List.of(
                FeedCommentResponse.builder()
                        .commentId(1L)
                        .memberId(101L)
                        .nickname("댓글러1")
                        .content("첫 댓글")
                        .createdAt(LocalDateTime.now().minusMinutes(10))
                        .parentCommentId(null)
                        .build(),
                FeedCommentResponse.builder()
                        .commentId(2L)
                        .memberId(102L)
                        .nickname("댓글러2")
                        .content("대댓글입니다")
                        .createdAt(LocalDateTime.now().minusMinutes(5))
                        .parentCommentId(1L)
                        .build()
        );

        when(feedQueryMapper.existsByFeedId(feedId)).thenReturn(true);
        when(feedQueryMapper.findCommentsByFeedId(feedId)).thenReturn(mockComments);

        // when
        PageHelper.startPage(page, size);
        PageResponse<FeedCommentResponse> result = feedCommentQueryService.getComments(feedId, page, size);

        // then
        assertThat(result.getContent()).hasSize(2);
        assertThat(result.getContent().get(0).getNickname()).isEqualTo("댓글러1");
        assertThat(result.getContent().get(1).getParentCommentId()).isEqualTo(1L);

        verify(feedQueryMapper, times(1)).existsByFeedId(feedId);
        verify(feedQueryMapper, times(1)).findCommentsByFeedId(feedId);
    }

    @Test
    void getComments_shouldThrowExceptionWhenFeedNotFound() {
        // given
        Long invalidFeedId = 999L;
        when(feedQueryMapper.existsByFeedId(invalidFeedId)).thenReturn(false);

        // when & then
        assertThatThrownBy(() -> feedCommentQueryService.getComments(invalidFeedId, 1, 10))
                .isInstanceOf(BusinessException.class)
                .hasMessage(ErrorCode.FEED_NOT_FOUND.getMessage());

        verify(feedQueryMapper, times(1)).existsByFeedId(invalidFeedId);
        verify(feedQueryMapper, never()).findCommentsByFeedId(any());
    }
}
