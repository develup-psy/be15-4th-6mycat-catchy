package com.sixmycat.catchy.feature.notification.command.application.service;

import com.sixmycat.catchy.exception.BusinessException;
import com.sixmycat.catchy.exception.ErrorCode;
import com.sixmycat.catchy.feature.follow.command.domain.repository.FollowRepository;
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
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationCommandService {
    private final NotificationRepository notificationRepository;
    private final SseEmitterRepository sseEmitterRepository;
    private final MemberRepository memberRepository;
    private final FollowRepository followRepository;
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

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
        String senderNickname = memberRepository.findNicknameById(senderMemberId).orElse("");
        boolean alreadyFollowed = followRepository.existsByFollowerIdAndFollowingId(receiverMemberId, senderMemberId);

        Map<String, Object> payload = Map.of(
                "memberId", receiverMemberId,
                "senderId", senderMemberId,
                "senderNickname", senderNickname,
                "profileImage", profileImage,
                "content", content,
                "type", type,
                "relatedId", relatedId,
                "createdAt", LocalDateTime.now(),
                "initialFollowing", alreadyFollowed
        );

        send(receiverMemberId, payload);
    }

    public SseEmitter subscribe(Long memberId, String lastEventId) {
        String emitterId = memberId + "_" + UUID.randomUUID() + "_" + System.currentTimeMillis();
        SseEmitter emitter = sseEmitterRepository.save(emitterId, new SseEmitter(DEFAULT_TIMEOUT));


        emitter.onCompletion(() -> {
            sseEmitterRepository.deleteById(emitterId);
            System.out.println("onCompletion - emitter 삭제: " + emitterId);
        });

        emitter.onTimeout(() -> {
            sseEmitterRepository.deleteById(emitterId);
            System.out.println("onTimeout - emitter 삭제: " + emitterId);
        });

        emitter.onError((e) -> {
            sseEmitterRepository.deleteById(emitterId);
            System.out.println("onError - emitter 삭제: " + emitterId);
        });

        sendToClient(emitterId, emitter, "initial-connect", "알림 서버 연결 성공. [memberId = " + memberId + "]");

        // ping 이벤트 30초마다 보내기
        scheduler.scheduleAtFixedRate(() -> {
            try {
                emitter.send(SseEmitter.event()
                        .name("ping")
                        .data("ping"));
            } catch (IOException e) {
                sseEmitterRepository.deleteById(emitterId);
                System.out.println("ping 전송 실패 - emitter 삭제: " + emitterId);
            }
        }, 30, 30, TimeUnit.SECONDS);


        // 기존 lastEventId 복구 로직
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
                            return false;
                        }
                    })
                    .forEach(entry -> sendToClient(entry.getKey(), emitter, "sse", entry.getValue()));
        }

        return emitter;
    }

    @Async
    public void send(Long memberId, Object data) {
        Map<String, SseEmitter> emitters = sseEmitterRepository.findAllEmittersStartWithId(memberId);

        emitters.forEach((emitterId, emitter) -> {
            try {
                // 캐시 저장
                sseEmitterRepository.saveEventCache(emitterId, data);
                sendToClient(emitterId, emitter, "sse", data);
            } catch (Exception e) {
                // 개별 emitter 전송 실패 시 로그만 남기고 다음 emitter로 진행
                log.warn("SSE 전송 실패: emitterId={}, error={}", emitterId, e.getMessage());
                sseEmitterRepository.deleteById(emitterId); // 실패한 emitter는 제거
            }
        });
    }


    private void sendToClient(String emitterId, SseEmitter emitter, String name, Object data) {
        try {
            emitter.send(SseEmitter.event()
                    .id(emitterId)
                    .name(name)
                    .data(data));
        } catch (IOException e) {
            // 연결 끊김은 자주 일어날 수 있으므로 error 대신 warn
            log.warn("SSE 연결 오류: emitterId={}, error={}", emitterId, e.getMessage());
            sseEmitterRepository.deleteById(emitterId);
        } catch (Exception e) {
            // 예기치 못한 예외도 대비
            log.error("SSE 전송 중 예기치 못한 오류 발생: emitterId={}, error={}", emitterId, e.getMessage());
            sseEmitterRepository.deleteById(emitterId);
        }
    }


}
