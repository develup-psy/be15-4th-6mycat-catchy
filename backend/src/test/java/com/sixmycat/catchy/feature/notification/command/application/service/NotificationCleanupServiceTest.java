package com.sixmycat.catchy.feature.notification.command.application.service;

import com.sixmycat.catchy.feature.notification.command.domain.repository.NotificationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class NotificationCleanupServiceTest {

    @Mock
    private NotificationRepository notificationRepository;

    @InjectMocks
    private NotificationSchedulerService notificationSchedulerService;

    @Test
    public void deleteOldNotifications_shouldCallRepositoryWithCorrectDate() {
        // given
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expectedThreshold = now.minusDays(30);

        // when
        notificationSchedulerService.deleteOldNotifications();

        // then
        ArgumentCaptor<LocalDateTime> captor = ArgumentCaptor.forClass(LocalDateTime.class);
        verify(notificationRepository, times(1)).deleteNotificationsOlderThan(captor.capture());

        // 이건 조금 유연하게 비교 (날짜 차이 허용)
        assertTrue(Duration.between(expectedThreshold, captor.getValue()).abs().getSeconds() < 5);
    }
}
