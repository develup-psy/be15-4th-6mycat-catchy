package com.sixmycat.catchy.feature.jjure.query.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sixmycat.catchy.common.dto.PageResponse;
import com.sixmycat.catchy.exception.BusinessException;
import com.sixmycat.catchy.exception.ErrorCode;
import com.sixmycat.catchy.feature.jjure.query.dto.response.JjureCommentResponse;
import com.sixmycat.catchy.feature.jjure.query.mapper.JjureQueryMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

class JjureCommentQueryServiceImplTest {

    @Mock
    private JjureQueryMapper jjureQueryMapper;

    @InjectMocks
    private JjureCommentQueryServiceImpl jjureCommentQueryService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getComments_returnsPagedCommentListSuccessfully() {
        // given
        Long jjureId = 1L;
        int page = 1;
        int size = 10;

        List<JjureCommentResponse> mockComments = List.of(
                JjureCommentResponse.builder()
                        .commentId(1L)
                        .memberId(101L)
                        .nickname("고양이짱")
                        .content("쭈르 첫 댓글")
                        .createdAt(LocalDateTime.now().minusMinutes(10))
                        .parentCommentId(null)
                        .build(),
                JjureCommentResponse.builder()
                        .commentId(2L)
                        .memberId(102L)
                        .nickname("대댓글러")
                        .content("쭈르 대댓글입니다")
                        .createdAt(LocalDateTime.now().minusMinutes(5))
                        .parentCommentId(1L)
                        .build()
        );

        when(jjureQueryMapper.existsByJjureId(jjureId)).thenReturn(true);
        when(jjureQueryMapper.findCommentsByJjureId(jjureId)).thenReturn(mockComments);

        // when
        PageResponse<JjureCommentResponse> result = jjureCommentQueryService.getComments(jjureId, page, size);

        // then
        assertThat(result.getContent()).hasSize(2);
        assertThat(result.getContent().get(0).getNickname()).isEqualTo("고양이짱");
        assertThat(result.getContent().get(1).getParentCommentId()).isEqualTo(1L);
        assertThat(result.getCurrentPage()).isEqualTo(page - 1); // PageHelper는 1-based, PageResponse는 0-based
    }

    @Test
    void getComments_throwsExceptionWhenJjureNotFound() {
        // given
        Long jjureId = 999L;
        when(jjureQueryMapper.existsByJjureId(jjureId)).thenReturn(false);

        // when & then
        assertThatThrownBy(() -> jjureCommentQueryService.getComments(jjureId, 1, 10))
                .isInstanceOf(BusinessException.class)
                .hasMessage(ErrorCode.JJURE_NOT_FOUND.getMessage());
    }
}
