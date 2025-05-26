package com.sixmycat.catchy.feature.feed.command.application.service;

import com.sixmycat.catchy.common.domain.TargetType;
import com.sixmycat.catchy.exception.BusinessException;
import com.sixmycat.catchy.exception.ErrorCode;
import com.sixmycat.catchy.feature.feed.command.application.dto.request.FeedCommentCreateRequest;
import com.sixmycat.catchy.feature.feed.command.domain.aggregate.FeedComment;
import com.sixmycat.catchy.feature.feed.command.domain.repository.FeedCommentRepository;
import com.sixmycat.catchy.feature.notification.command.application.service.NotificationCommandService;
import com.sixmycat.catchy.feature.notification.command.domain.aggregate.NotificationType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class FeedCommentCommandServiceImplTest {

    private FeedCommentRepository commentRepository;
    private NotificationCommandService notificationService;
    private FeedInternalService feedInternalService;
    private FeedCommentCommandServiceImpl commentService;

    @BeforeEach
    void setUp() {
        commentRepository = mock(FeedCommentRepository.class);
        notificationService = mock(NotificationCommandService.class);
        feedInternalService = mock(FeedInternalService.class);
        commentService = new FeedCommentCommandServiceImpl(commentRepository, notificationService, feedInternalService);
    }

    @Test
    @DisplayName("Successfully creates a comment without parent")
    void shouldCreateCommentSuccessfully() {
        Long memberId = 1L;
        Long targetId = 2L;

        FeedCommentCreateRequest request = mock(FeedCommentCreateRequest.class);
        when(request.getTargetId()).thenReturn(targetId);
        when(request.getTargetType()).thenReturn(TargetType.FEED);
        when(request.getContent()).thenReturn("test content");
        when(request.getParentCommentId()).thenReturn(null);

        FeedComment comment = spy(FeedComment.create(memberId, targetId, TargetType.FEED, "test content", null));
        when(comment.getCommentId()).thenReturn(123L);
        when(commentRepository.save(any(FeedComment.class))).thenReturn(comment);
        when(feedInternalService.findMemberIdByFeedId(targetId)).thenReturn(99L);

        Long result = commentService.createComment(request, memberId);

        assertThat(result).isEqualTo(123L);
        verify(commentRepository).save(any(FeedComment.class));
        verify(notificationService).createAndSendNotification(
                eq(memberId), eq(99L), anyString(), eq(NotificationType.COMMENT), eq(targetId));
    }

    @Test
    @DisplayName("Throws when parent comment not found")
    void shouldThrowWhenParentCommentNotFound() {
        FeedCommentCreateRequest request = mock(FeedCommentCreateRequest.class);
        when(request.getParentCommentId()).thenReturn(999L);
        when(commentRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> commentService.createComment(request, 1L))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining(ErrorCode.COMMENT_NOT_FOUND.getMessage());
    }

    @Test
    @DisplayName("Successfully creates a reply comment when parent exists and is valid")
    void shouldCreateReplyCommentWhenParentExistsAndValid() {
        Long parentId = 10L;
        Long memberId = 1L;
        Long targetId = 2L;

        FeedCommentCreateRequest request = mock(FeedCommentCreateRequest.class);
        when(request.getTargetId()).thenReturn(targetId);
        when(request.getTargetType()).thenReturn(TargetType.FEED);
        when(request.getContent()).thenReturn("reply");
        when(request.getParentCommentId()).thenReturn(parentId);

        FeedComment parent = spy(FeedComment.create(99L, targetId, TargetType.FEED, "parent", null));
        when(parent.getTargetType()).thenReturn(TargetType.FEED);
        when(parent.getMemberId()).thenReturn(99L);

        FeedComment reply = spy(FeedComment.create(memberId, targetId, TargetType.FEED, "reply", parentId));
        when(reply.getCommentId()).thenReturn(124L);

        when(commentRepository.findById(parentId)).thenReturn(Optional.of(parent));
        when(commentRepository.save(any(FeedComment.class))).thenReturn(reply);

        Long result = commentService.createComment(request, memberId);

        assertThat(result).isEqualTo(124L);
        verify(notificationService).createAndSendNotification(
                eq(memberId), eq(99L), anyString(), eq(NotificationType.RECOMMENT), eq(targetId));
    }

    @Test
    @DisplayName("Recursively deletes comment and its children")
    void shouldDeleteCommentRecursively() {
        Long parentId = 1L;
        Long memberId = 1L;

        FeedComment parent = spy(FeedComment.create(memberId, 2L, TargetType.FEED, "parent", null));
        FeedComment child1 = spy(FeedComment.create(memberId, 2L, TargetType.FEED, "child1", parentId));
        FeedComment child2 = spy(FeedComment.create(memberId, 2L, TargetType.FEED, "child2", parentId));

        when(parent.getCommentId()).thenReturn(parentId);
        when(child1.getCommentId()).thenReturn(2L);
        when(child2.getCommentId()).thenReturn(3L);

        when(commentRepository.findById(parentId)).thenReturn(Optional.of(parent));
        when(commentRepository.findAllByParentCommentId(parentId)).thenReturn(List.of(child1, child2));
        when(commentRepository.findAllByParentCommentId(2L)).thenReturn(List.of());
        when(commentRepository.findAllByParentCommentId(3L)).thenReturn(List.of());

        commentService.deleteComment(parentId, memberId);

        verify(commentRepository).deleteById(2L);
        verify(commentRepository).deleteById(3L);
        verify(commentRepository).deleteById(parentId);
    }


    @Test
    @DisplayName("Throws when comment not found on delete")
    void shouldThrowWhenCommentNotFoundOnDelete() {
        when(commentRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> commentService.deleteComment(1L, 1L))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining(ErrorCode.COMMENT_NOT_FOUND.getMessage());
    }

    @Test
    @DisplayName("Throws when user is not the author of the comment")
    void shouldThrowWhenUnauthorizedUserDeletesComment() {
        FeedComment comment = FeedComment.create(1L, 2L, TargetType.FEED, "hi", null);
        when(commentRepository.findById(1L)).thenReturn(Optional.of(comment));

        assertThatThrownBy(() -> commentService.deleteComment(1L, 999L))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining(ErrorCode.UNAUTHORIZED_USER.getMessage());
    }
}
