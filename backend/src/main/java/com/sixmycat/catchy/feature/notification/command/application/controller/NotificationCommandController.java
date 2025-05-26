package com.sixmycat.catchy.feature.notification.command.application.controller;

import com.sixmycat.catchy.feature.notification.command.application.service.NotificationCommandService;
import com.sixmycat.catchy.feature.notification.command.domain.aggregate.NotificationType;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.nio.file.AccessDeniedException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/notifications")
@Tag(name = "알림 API", description = "알림 연결, 발송 API")
public class NotificationCommandController {
    private final NotificationCommandService notificationCommandService;

    @GetMapping(value = "/connect", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public ResponseEntity<SseEmitter> subscribe(
            @AuthenticationPrincipal String memberId,
            @RequestHeader(value = "Last-Event-Id", required = false, defaultValue = "") String lastEventId) throws AccessDeniedException {

        // 또는 SecurityContextHolder로 직접 체크
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            throw new AccessDeniedException("인증되지 않음");
        }

        return ResponseEntity.ok(notificationCommandService.subscribe(Long.parseLong(memberId), lastEventId));
    }

    @GetMapping("/send")
    public ResponseEntity<String> sendTestNotification(
            @AuthenticationPrincipal String memberId) {

        notificationCommandService.createAndSendNotification(Long.parseLong(memberId), 4L, "회원님의 피드를 좋아합니다.", NotificationType.FEED_LIKE, 123L);
        return ResponseEntity.ok("알림 전송 완료");
    }
}
