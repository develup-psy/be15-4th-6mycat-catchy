package com.sixmycat.catchy.feature.jjure.query.service;

import com.sixmycat.catchy.common.dto.AuthorInfo;
import com.sixmycat.catchy.common.dto.CommentPreview;
import com.sixmycat.catchy.common.dto.PageResponse;
import com.sixmycat.catchy.exception.BusinessException;
import com.sixmycat.catchy.exception.ErrorCode;
import com.sixmycat.catchy.feature.jjure.query.dto.response.JjureBaseInfo;
import com.sixmycat.catchy.feature.jjure.query.dto.response.JjureDetailResponse;
import com.sixmycat.catchy.feature.jjure.query.mapper.JjureQueryMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class JjureQueryServiceImplTest {

    private JjureQueryMapper jjureQueryMapper;
    private JjureQueryServiceImpl jjureQueryService;

    @BeforeEach
    void setUp() {
        jjureQueryMapper = mock(JjureQueryMapper.class);
        jjureQueryService = new JjureQueryServiceImpl(jjureQueryMapper);
    }

    @Test
    void shouldReturnJjureDetailSuccessfully_whenDataExists() {
        // given
        Long jjureId = 1L;
        Long userId = 10L;

        JjureBaseInfo baseInfo = new JjureBaseInfo(
                jjureId,
                userId,
                "testUser",
                "profile.jpg",
                "fileKey123",
                "caption",
                "music.mp3",
                5,
                3,
                LocalDateTime.now()
        );

        CommentPreview commentPreview = new CommentPreview("nick", "nice post!");

        when(jjureQueryMapper.findJjureBaseInfo(jjureId)).thenReturn(Optional.of(baseInfo));
        when(jjureQueryMapper.findLatestCommentPreview(jjureId)).thenReturn(Optional.of(commentPreview));
        when(jjureQueryMapper.isJjureLikedByUser(jjureId, userId)).thenReturn(true);

        // when
        JjureDetailResponse result = jjureQueryService.getJjureDetail(jjureId, userId);

        // then
        assertThat(result.getId()).isEqualTo(jjureId);
        assertThat(result.getAuthor()).isEqualTo(new AuthorInfo(userId, "testUser", "profile.jpg"));
        assertThat(result.getFileKey()).isEqualTo("fileKey123");
        assertThat(result.getCaption()).isEqualTo("caption");
        assertThat(result.getMusicUrl()).isEqualTo("music.mp3");
        assertThat(result.getLikeCount()).isEqualTo(5);
        assertThat(result.getCommentCount()).isEqualTo(3);
        assertThat(result.getCommentPreview()).isEqualTo(commentPreview);
        assertThat(result.isLiked()).isTrue();
        assertThat(result.isMine()).isTrue();
    }

    @Test
    void shouldThrowException_whenJjureNotFound() {
        // given
        Long jjureId = 99L;
        when(jjureQueryMapper.findJjureBaseInfo(jjureId)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> jjureQueryService.getJjureDetail(jjureId, 1L))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining(ErrorCode.JJURE_NOT_FOUND.getMessage());
    }

    @Test
    void shouldReturnJjureListSuccessfully_whenUserIsAuthenticated() {
        // given
        Long userId = 10L;
        Long jjureId = 1L;
        JjureBaseInfo base = new JjureBaseInfo(
                jjureId, userId, "닉네임", "profile.jpg",
                "file-key", "캡션", "음악", 5, 3,
                LocalDateTime.now()
        );
        CommentPreview comment = new CommentPreview("유저", "코멘트");

        // ✅ 명확하게 userId를 넘김
        when(jjureQueryMapper.findJjureList(userId)).thenReturn(List.of(base));
        when(jjureQueryMapper.findLatestCommentPreview(jjureId)).thenReturn(Optional.of(comment));
        when(jjureQueryMapper.isJjureLikedByUser(jjureId, userId)).thenReturn(true);

        // when
        PageResponse<JjureDetailResponse> response = jjureQueryService.getJjureList(userId, 0, 10);

        // then
        assertThat(response.getContent()).hasSize(1);
        JjureDetailResponse result = response.getContent().get(0);
        assertThat(result.getId()).isEqualTo(jjureId);
        assertThat(result.getAuthor()).isEqualTo(new AuthorInfo(userId, "닉네임", "profile.jpg"));
        assertThat(result.getFileKey()).isEqualTo("file-key");
        assertThat(result.getCaption()).isEqualTo("캡션");
        assertThat(result.getMusicUrl()).isEqualTo("음악");
        assertThat(result.getLikeCount()).isEqualTo(5);
        assertThat(result.getCommentCount()).isEqualTo(3);
        assertThat(result.getCommentPreview()).isEqualTo(comment);
        assertThat(result.isLiked()).isTrue();
        assertThat(result.isMine()).isTrue();
    }


    @Test
    void shouldHandleEmptyListGracefully() {
        // given
        when(jjureQueryMapper.findJjureList(null)).thenReturn(List.of());

        // when
        PageResponse<JjureDetailResponse> response = jjureQueryService.getJjureList(null, 0, 10);

        // then
        assertThat(response.getContent()).isEmpty();
    }

}