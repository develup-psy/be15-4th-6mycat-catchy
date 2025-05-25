package com.sixmycat.catchy.feature.notification.command.application.service;

import com.sixmycat.catchy.exception.BusinessException;
import com.sixmycat.catchy.exception.ErrorCode;
import com.sixmycat.catchy.feature.member.command.domain.repository.MemberRepository;
import com.sixmycat.catchy.feature.notification.command.domain.aggregate.Notification;
import com.sixmycat.catchy.feature.notification.command.domain.aggregate.NotificationType;
import com.sixmycat.catchy.feature.notification.command.domain.repository.NotificationRepository;
import com.sixmycat.catchy.feature.notification.command.infrastructure.repository.SseEmitterRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationCommandService {
    private final NotificationRepository notificationRepository;
    private final SseEmitterRepository sseEmitterRepository;
    private final MemberRepository memberRepository;

    private static final Long DEFAULT_TIMEOUT = 60L * 1000 * 60; // 1시간

    @Transactional
    public void createAndSendNotification(Long senderMemberId, Long receiverMemberId, String content, NotificationType type, Long relatedId) {


        Notification notification = Notification.builder()
                .memberId(receiverMemberId)
                .senderMemberId(senderMemberId)
                .type(type)
                .relatedId(relatedId)
                .content(content)
                .build();

        notificationRepository.save(notification);

        Optional<String> optionalProfileImage = memberRepository.findProfileImageByIdAndDeletedAtIsNull(senderMemberId);
        String profileImage = optionalProfileImage.orElse("");

        Map<String, Object> payload = Map.of(
                "memberId", receiverMemberId,
                "senderMemberId", senderMemberId,
                "profileImage", profileImage,
                "content", content,
                "type", type,
                "relatedId", relatedId,
                "createdAt", LocalDateTime.now()
        );

        send(receiverMemberId, payload);
    }

    public SseEmitter subscribe(Long memberId, String lastEventId) {
        String emitterId = memberId + "_" + UUID.randomUUID() + "_" + System.currentTimeMillis();
        SseEmitter emitter = sseEmitterRepository.save(emitterId, new SseEmitter(DEFAULT_TIMEOUT));


        emitter.onCompletion(() -> sseEmitterRepository.deleteById(emitterId));
        emitter.onTimeout(() -> sseEmitterRepository.deleteById(emitterId));
        emitter.onError((e) -> sseEmitterRepository.deleteById(emitterId));

        sendToClient(emitterId, emitter, "알림 서버 연결 성공. [memberId = " + memberId + "]");


        if (!lastEventId.isEmpty()) {
            Map<String, Object> events = sseEmitterRepository.findAllEventCacheStartWithId(memberId);
            events.entrySet().stream()
                    .filter(entry -> {
                        String eventKey = entry.getKey(); // "{memberId}_{uuid}_{timestamp}"
                        String[] parts = eventKey.split("_");
                        String[] lastParts = lastEventId.split("_");

                        if (parts.length < 3 || lastParts.length < 3) {
                            return false;
                        }

                        try {
                            long eventTimestamp = Long.parseLong(parts[2]);
                            long lastTimestamp = Long.parseLong(lastParts[2]);

                            return eventTimestamp > lastTimestamp;
                        } catch (NumberFormatException e) {
                            return false; // 파싱 실패한 경우도 무시
                        }
                    })
                    .forEach(entry -> sendToClient(entry.getKey(), emitter, entry.getValue()));
        }

        return emitter;
    }

    @Async
    public void send(Long memberId, Object data) {
        Map<String, SseEmitter> emitters = sseEmitterRepository.findAllEmittersStartWithId(memberId);
        emitters.forEach((emitterId, emitter) -> {
            sseEmitterRepository.saveEventCache(emitterId, data);
            sendToClient(emitterId, emitter, data);
        });
    }

    private void sendToClient(String emitterId, SseEmitter emitter, Object data) {
        try {
            emitter.send(SseEmitter.event()
                    .id(emitterId)
                    .name("sse")
                    .data(data));
        } catch (IOException e) {
            sseEmitterRepository.deleteById(emitterId);
            log.error("SSE 연결 오류: emitterId={}, error={}", emitterId, e.getMessage());
        }
    }


}
