package com.sixmycat.catchy.feature.notification.command.application.service;

import com.sixmycat.catchy.feature.member.command.domain.repository.MemberRepository;
import com.sixmycat.catchy.feature.notification.command.domain.aggregate.Notification;
import com.sixmycat.catchy.feature.notification.command.domain.aggregate.NotificationType;
import com.sixmycat.catchy.feature.notification.command.domain.repository.NotificationRepository;
import com.sixmycat.catchy.feature.notification.command.infrastructure.repository.SseEmitterRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
class NotificationCommandServiceTest {

    @Mock
    private NotificationRepository notificationRepository;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private SseEmitterRepository sseEmitterRepository;

    @InjectMocks
    private NotificationCommandService notificationCommandService;

    private final Long memberId = 1L;
    private final Long relatedId = 99L;
    private final String content = "새로운 댓글이 달렸습니다.";

    @Test
    void createAndSendNotification_정상동작() {
        // given
        NotificationType type = NotificationType.COMMENT;
        Map<String, SseEmitter> emitters = Map.of();

        when(memberRepository.findProfileImageByIdAndDeletedAtIsNull(memberId)).thenReturn(Optional.of(""));
        when(sseEmitterRepository.findAllEmittersStartWithId(memberId)).thenReturn(emitters);

        // when
        notificationCommandService.createAndSendNotification(memberId, 1L, content, type, relatedId);

        // then
        verify(notificationRepository, times(1)).save(any(Notification.class));
    }

    @Test
    void subscribe_정상동작_및_기존_이벤트_전송() {
        // given
        String lastEventId = memberId + "_old_1000";
        String newEmitterId = memberId + "_uuid_" + System.currentTimeMillis();
        SseEmitter emitter = new SseEmitter();

        when(sseEmitterRepository.save(anyString(), any(SseEmitter.class))).thenReturn(emitter);
        when(sseEmitterRepository.findAllEventCacheStartWithId(memberId)).thenReturn(Map.of(
                memberId + "_uuid_2000", "캐시된 데이터"
        ));

        // when
        SseEmitter result = notificationCommandService.subscribe(memberId, lastEventId);

        // then
        assertNotNull(result);
        verify(sseEmitterRepository, atLeastOnce()).save(anyString(), any());
    }

    @Test
    void send_정상적으로_모든_Emitter에게_데이터전송() {
        // given
        Object data = Map.of("message", "테스트 알림");

        SseEmitter mockEmitter = mock(SseEmitter.class);
        String emitterId = memberId + "_uuid_" + System.currentTimeMillis();
        Map<String, SseEmitter> emitters = Map.of(emitterId, mockEmitter);

        when(sseEmitterRepository.findAllEmittersStartWithId(memberId)).thenReturn(emitters);

        // when
        notificationCommandService.send(memberId, data);

        // then
        verify(sseEmitterRepository).saveEventCache(eq(emitterId), eq(data));
    }

    @Test
    void send_IOException_발생시_emitter_삭제됨() throws IOException {
        // given
        SseEmitter mockEmitter = mock(SseEmitter.class);
        String emitterId = memberId + "_uuid_" + System.currentTimeMillis();

        doThrow(new IOException("연결 오류")).when(mockEmitter).send(any(SseEmitter.SseEventBuilder.class));

        Map<String, SseEmitter> emitters = Map.of(emitterId, mockEmitter);

        when(sseEmitterRepository.findAllEmittersStartWithId(memberId)).thenReturn(emitters);

        // when
        notificationCommandService.send(memberId, Map.of("msg", "테스트"));

        // then
        verify(sseEmitterRepository).deleteById(eq(emitterId));
    }

}
