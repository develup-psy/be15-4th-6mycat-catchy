package com.sixmycat.catchy.feature.notification.command.application.service;

import com.sixmycat.catchy.feature.member.command.domain.aggregate.Cat;
import com.sixmycat.catchy.feature.member.command.domain.repository.CatRepository;
import com.sixmycat.catchy.feature.member.query.mapper.MemberMapper;
import com.sixmycat.catchy.feature.notification.command.domain.aggregate.Notification;
import com.sixmycat.catchy.feature.notification.command.domain.aggregate.NotificationType;
import com.sixmycat.catchy.feature.notification.command.domain.repository.NotificationRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationSchedulerService {

    private final NotificationRepository notificationRepository;
    private final NotificationCommandService notificationCommandService;
    private final MemberMapper memberMapper;

    // 매일 새벽 2시에 실행
    @Transactional
    @Scheduled(cron = "0 0 2 * * *")
    public void deleteOldNotifications() {
        LocalDateTime thresholdDate = LocalDateTime.now().minusDays(30);
        notificationRepository.deleteNotificationsOlderThan(thresholdDate);
    }

    // 매일 아침 8시에 실행: 생일 알림 생성
    @Transactional
    @Scheduled(cron = "0 0 8 * * *")
    public void createBirthdayNotifications() {
        List<Long> birthdayMember = memberMapper.findByCatBirth();

        for (Long memberId : birthdayMember) {
            notificationCommandService.createAndSendNotification(1L, memberId, "🎂 생일 축하해요!", NotificationType.BIRTHDAY, null);
        }
    }
}
