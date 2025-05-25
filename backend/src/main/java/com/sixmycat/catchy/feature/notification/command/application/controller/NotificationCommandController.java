package com.sixmycat.catchy.feature.notification.command.application.controller;

import com.sixmycat.catchy.feature.notification.command.application.service.NotificationCommandService;
import com.sixmycat.catchy.feature.notification.command.domain.aggregate.NotificationType;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/notifications")
@Tag(name = "알림 API", description = "알림 연결, 발송 API")
public class NotificationCommandController {
    private final NotificationCommandService notificationCommandService;

    @GetMapping(value = "/connect", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public ResponseEntity<SseEmitter> subscribe(
            @AuthenticationPrincipal String memberId,
            @RequestHeader(value = "Last-Event-Id", required = false, defaultValue = "") String lastEventId) {
        return ResponseEntity.ok(notificationCommandService.subscribe(Long.parseLong(memberId), lastEventId));
    }

    @GetMapping("/send")
    public ResponseEntity<String> sendTestNotification(
            @AuthenticationPrincipal String memberId) {

        notificationCommandService.createAndSendNotification(Long.parseLong(memberId), 1L, "좋아요 추가되었습니다", NotificationType.FEED_LIKE, 123L);
        return ResponseEntity.ok("알림 전송 완료");
    }
}
