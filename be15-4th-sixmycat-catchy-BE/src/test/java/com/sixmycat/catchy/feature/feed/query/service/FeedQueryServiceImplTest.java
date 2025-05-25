package com.sixmycat.catchy.feature.feed.query.service;

import com.sixmycat.catchy.common.dto.AuthorInfo;
import com.sixmycat.catchy.common.dto.CommentPreview;
import com.sixmycat.catchy.common.dto.PageResponse;
import com.sixmycat.catchy.exception.BusinessException;
import com.sixmycat.catchy.exception.ErrorCode;
import com.sixmycat.catchy.feature.feed.query.dto.response.*;
import com.sixmycat.catchy.feature.feed.query.mapper.FeedQueryMapper;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

class FeedQueryServiceImplTest {

    @Mock
    private FeedQueryMapper feedQueryMapper;

    @InjectMocks
    private FeedQueryServiceImpl feedQueryService;

    public FeedQueryServiceImplTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getFeedDetail_returnsResponseSuccessfully() {
        // given
        Long feedId = 1L;
        Long userId = 10L;

        FeedBaseInfo baseInfo = new FeedBaseInfo(
                1L,
                10L,
                "nickname",
                "https://profile.img",
                "some content",
                "https://music.url",
                5,
                2,
                LocalDateTime.now()
        );

        List<String> imageUrls = List.of("img1.jpg", "img2.jpg");
        CommentPreview commentPreview = new CommentPreview("commenter", "nice!");

        when(feedQueryMapper.findFeedBaseInfo(feedId)).thenReturn(Optional.of(baseInfo));
        when(feedQueryMapper.findFeedImageUrls(feedId)).thenReturn(imageUrls);
        when(feedQueryMapper.findLatestCommentPreview(feedId)).thenReturn(Optional.of(commentPreview));
        when(feedQueryMapper.isFeedLikedByUser(feedId, userId)).thenReturn(true);

        // when
        FeedDetailResponse result = feedQueryService.getFeedDetail(feedId, userId);

        // then
        assertThat(result.getId()).isEqualTo(feedId);
        assertThat(result.getAuthor().getAuthorId()).isEqualTo(userId);
        assertThat(result.getImageUrls()).containsExactlyElementsOf(imageUrls);
        assertThat(result.getContent()).isEqualTo("some content");
        assertThat(result.getMusicUrl()).isEqualTo("https://music.url");
        assertThat(result.getCommentPreview().getUserNickname()).isEqualTo("commenter");
        assertThat(result.isLiked()).isTrue();
        assertThat(result.isMine()).isTrue();
    }

    @Test
    void getFeedDetail_throwsException_whenFeedNotFound() {
        // given
        Long feedId = 100L;

        when(feedQueryMapper.findFeedBaseInfo(feedId)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> feedQueryService.getFeedDetail(feedId, 10L))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining(ErrorCode.FEED_NOT_FOUND.getMessage());
    }

    @Test
    void shouldReturnMyFeedListSuccessfully() {
        // given
        Long memberId = 1L;
        int page = 0;
        int size = 10;

        List<FeedSummaryResponse> mockList = List.of(
                FeedSummaryResponse.builder()
                        .feedId(1L)
                        .thumbnailUrl("https://example.com/image1.jpg")
                        .build(),
                FeedSummaryResponse.builder()
                        .feedId(2L)
                        .thumbnailUrl("https://example.com/image2.jpg")
                        .build()
        );

        when(feedQueryMapper.findMyFeeds(memberId)).thenReturn(mockList);

        // when
        PageResponse<FeedSummaryResponse> result = feedQueryService.getMyFeeds(memberId, page, size);

        // then
        assertThat(result.getContent()).hasSize(2);
        assertThat(result.getContent().get(0).getFeedId()).isEqualTo(1L);
    }

    @Test
    void shouldReturnFeedListSuccessfully() {
        // given
        Long userId = 1L;
        FeedBaseInfo baseInfo = new FeedBaseInfo(
                10L, // feedId
                userId, // authorId (userId와 일치해야 isMine = true)
                "nickname",
                "profile.jpg",
                "hello world",
                "music.mp3",
                5,
                2,
                LocalDateTime.now()
        );

        List<String> imageUrls = List.of("img1.jpg", "img2.jpg");
        CommentPreview preview = new CommentPreview("댓글쓴이", "댓글 내용");

        // ✅ 수정: userId를 인자로 전달
        when(feedQueryMapper.findFeedList(userId)).thenReturn(List.of(baseInfo));
        when(feedQueryMapper.findFeedImageUrls(10L)).thenReturn(imageUrls);
        when(feedQueryMapper.findLatestCommentPreview(10L)).thenReturn(Optional.of(preview));
        when(feedQueryMapper.isFeedLikedByUser(10L, userId)).thenReturn(true);

        // when
        PageResponse<FeedDetailResponse> result = feedQueryService.getFeedList(userId, 0, 10);

        // then
        assertThat(result.getContent()).hasSize(1);
        FeedDetailResponse response = result.getContent().get(0);
        assertThat(response.getId()).isEqualTo(10L);
        assertThat(response.getAuthor()).isEqualTo(new AuthorInfo(userId, "nickname", "profile.jpg"));
        assertThat(response.getImageUrls()).isEqualTo(imageUrls);
        assertThat(response.getCommentPreview()).isEqualTo(preview);
        assertThat(response.isLiked()).isTrue();
        assertThat(response.isMine()).isTrue();
    }
}
