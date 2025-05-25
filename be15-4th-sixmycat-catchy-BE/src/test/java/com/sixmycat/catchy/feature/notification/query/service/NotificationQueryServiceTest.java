package com.sixmycat.catchy.feature.notification.query.service;

import com.sixmycat.catchy.common.dto.PageResponse;
import com.sixmycat.catchy.feature.notification.command.domain.aggregate.NotificationType;
import com.sixmycat.catchy.feature.notification.query.dto.NotificationDTO;
import com.sixmycat.catchy.feature.notification.query.mapper.NotificationQueryMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class NotificationQueryServiceTest {

    @Mock
    private NotificationQueryMapper notificationMapper;

    @InjectMocks
    private NotificationQueryService notificationQueryService;

    @Test
    void getNotifications_정상조회_성공() {
        // given
        Long memberId = 1L;
        int page = 1;
        int size = 10;

        List<NotificationDTO> mockList = List.of(
                createNotification(1L, "알림1"),
                createNotification(2L, "알림2")
        );

        // PageHelper가 startPage(...) 이후 쿼리를 실행하면 count 쿼리를 자동으로 처리하기 때문에,
        // 우리는 단순히 Mapper에서 list를 반환하는 것만 확인하면 됨
        when(notificationMapper.selectNotifications(memberId)).thenReturn(mockList);

        // when
        PageResponse<NotificationDTO> response = notificationQueryService.getNotifications(memberId, page, size);

        // then
        assertThat(response.getContent()).hasSize(2);
        assertThat(response.getContent().get(0).getContent()).isEqualTo("알림1");
        assertThat(response.getContent().get(1).getContent()).isEqualTo("알림2");
    }

    // 헬퍼 메서드
    private NotificationDTO createNotification(Long senderId, String content) {
        NotificationDTO dto = new NotificationDTO();
        dto.setMemberId(1L);
        dto.setSenderId(senderId);
        dto.setSenderNickname("김멘토");
        dto.setProfileImage("image.png");
        dto.setContent(content);
        dto.setType(NotificationType.COMMENT); // enum 값 예시
        dto.setRelatedId(100L);
        dto.setCreatedAt(LocalDateTime.now());
        return dto;
    }
}
