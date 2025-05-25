package com.sixmycat.catchy.feature.notification.query.dto;

import com.sixmycat.catchy.feature.notification.command.domain.aggregate.NotificationType;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class NotificationDTO {
    Long id;
    Long memberId;
    Long senderId;
    String senderNickname;
    String profileImage;
    String content;
    NotificationType type;
    Long relatedId;
    LocalDateTime createdAt;
    Boolean initialFollowing;
}
