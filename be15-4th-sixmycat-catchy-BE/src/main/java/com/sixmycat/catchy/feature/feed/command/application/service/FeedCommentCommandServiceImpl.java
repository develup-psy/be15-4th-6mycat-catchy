package com.sixmycat.catchy.feature.feed.command.application.service;

import com.sixmycat.catchy.exception.BusinessException;
import com.sixmycat.catchy.exception.ErrorCode;
import com.sixmycat.catchy.feature.feed.command.application.dto.request.FeedCommentCreateRequest;
import com.sixmycat.catchy.feature.feed.command.domain.aggregate.FeedComment;
import com.sixmycat.catchy.feature.feed.command.domain.repository.FeedCommentRepository;
import com.sixmycat.catchy.feature.notification.command.application.service.NotificationCommandService;
import com.sixmycat.catchy.feature.notification.command.domain.aggregate.NotificationType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FeedCommentCommandServiceImpl implements FeedCommentCommandService {

    private final FeedCommentRepository commentRepository;
    private final NotificationCommandService notificationCommandService;
    private final FeedInternalService feedInternalService;

    @Override
    @Transactional
    public Long createComment(FeedCommentCreateRequest request, Long memberId) {
        if (request.getParentCommentId() != null) {
            FeedComment parent = commentRepository.findById(request.getParentCommentId())
                    .orElseThrow(() -> new BusinessException(ErrorCode.COMMENT_NOT_FOUND));

            if (!parent.getTargetType().equals(request.getTargetType())) {
                throw new BusinessException(ErrorCode.INVALID_PARENT_COMMENT);
            }
        }

        FeedComment comment = FeedComment.create(
                memberId,
                request.getTargetId(),
                request.getTargetType(),
                request.getContent(),
                request.getParentCommentId()
        );

        Long savedId = commentRepository.save(comment).getCommentId();

        // 알림 전송
        Long receiverId;
        NotificationType notificationType;

        if (request.getParentCommentId() != null) {
            // 대댓글 → 부모 댓글 작성자에게 알림
            FeedComment parent = commentRepository.findById(request.getParentCommentId())
                    .orElseThrow(() -> new BusinessException(ErrorCode.COMMENT_NOT_FOUND));

            receiverId = parent.getMemberId();
            notificationType = NotificationType.RECOMMENT;
        } else {
            // 댓글 → 피드 작성자에게 알림
            receiverId = feedInternalService.findMemberIdByFeedId(request.getTargetId());
            notificationType = NotificationType.COMMENT;
        }

        if (!receiverId.equals(memberId)) {
            notificationCommandService.createAndSendNotification(
                    memberId,
                    receiverId,
                    "댓글 추가",
                    notificationType,
                    request.getTargetId()
            );
        }

        return savedId;
    }

    @Override
    @Transactional
    public void deleteComment(Long commentId, Long memberId) {
        FeedComment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new BusinessException(ErrorCode.COMMENT_NOT_FOUND));

        if (!comment.getMemberId().equals(memberId)) {
            throw new BusinessException(ErrorCode.UNAUTHORIZED_USER);
        }

        // 재귀적으로 자식 댓글 포함 삭제
        deleteRecursively(commentId);
    }

    private void deleteRecursively(Long commentId) {
        List<FeedComment> children = commentRepository.findAllByParentCommentId(commentId);
        for (FeedComment child : children) {
            deleteRecursively(child.getCommentId());
        }
        commentRepository.deleteById(commentId);
    }
}
