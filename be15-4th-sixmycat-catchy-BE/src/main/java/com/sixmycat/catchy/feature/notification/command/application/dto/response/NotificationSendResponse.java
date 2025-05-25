package com.sixmycat.catchy.feature.notification.command.application.dto.response;

import com.sixmycat.catchy.feature.notification.command.domain.aggregate.NotificationType;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class NotificationSendResponse {

    private Long id;
    private String memberId;
    private String relatedId;
    private NotificationType type;
    private String content;
    private LocalDateTime createdAt;

}
