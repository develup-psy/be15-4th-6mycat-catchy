package com.sixmycat.catchy.feature.like.command.application.service;

import com.sixmycat.catchy.common.domain.TargetType;
import com.sixmycat.catchy.exception.BusinessException;
import com.sixmycat.catchy.exception.ErrorCode;
import com.sixmycat.catchy.feature.feed.command.application.service.FeedInternalService;
import com.sixmycat.catchy.feature.jjure.command.application.service.JjureInternalService;
import com.sixmycat.catchy.feature.like.command.domain.aggregate.Like;
import com.sixmycat.catchy.feature.like.command.domain.repository.LikeRepository;
import com.sixmycat.catchy.feature.notification.command.application.service.NotificationCommandService;
import com.sixmycat.catchy.feature.notification.command.domain.aggregate.NotificationType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LikeCommandServiceImpl implements LikeCommandService {

    private final LikeRepository likeRepository;
    private final NotificationCommandService notificationCommandService;
    private final FeedInternalService feedInternalService;
    private final JjureInternalService jjureInternalService;

    @Override
    @Transactional
    public void like(Long memberId, Long targetId, TargetType targetType) {
        boolean alreadyLiked = likeRepository.existsByMemberIdAndTargetIdAndTargetType(memberId, targetId, targetType);
        if (alreadyLiked) {
            throw new BusinessException(ErrorCode.ALREADY_LIKED);
        }

        Like like = Like.of(memberId, targetId, targetType);
        likeRepository.save(like);

        Long receiverMemberId = resolveReceiverId(targetId, targetType);
        if(!receiverMemberId.equals(memberId)) {
            if (targetType == TargetType.FEED){
                notificationCommandService.createAndSendNotification(memberId, receiverMemberId, "피드 좋아요 추가되었습니다", NotificationType.FEED_LIKE, targetId);
            } else {
                notificationCommandService.createAndSendNotification(memberId, receiverMemberId, "쭈르 좋아요 추가되었습니다", NotificationType.JJURE_LIKE, targetId);
            }
        }

    }

    @Override
    @Transactional
    public void unlike(Long memberId, Long targetId, TargetType targetType) {
        Like like = likeRepository.findByMemberIdAndTargetIdAndTargetType(memberId, targetId, targetType)
                .orElseThrow(() -> new BusinessException(ErrorCode.LIKE_NOT_FOUND));

        likeRepository.delete(like);
    }

    @Override
    public Long resolveReceiverId(Long targetId, TargetType targetType) {
        return switch (targetType) {
            case FEED -> feedInternalService.findMemberIdByFeedId(targetId);
            case JJURE -> jjureInternalService.findMemberIdByJjureId(targetId);
        };
    }
}