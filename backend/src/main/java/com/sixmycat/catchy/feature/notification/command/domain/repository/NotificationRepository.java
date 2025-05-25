package com.sixmycat.catchy.feature.notification.command.domain.repository;

import com.sixmycat.catchy.feature.notification.command.domain.aggregate.Notification;
import io.lettuce.core.dynamic.annotation.Param;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    @Modifying
    @Query("DELETE FROM Notification n WHERE n.createdAt < :thresholdDate")
    void deleteNotificationsOlderThan(@Param("thresholdDate") LocalDateTime thresholdDate);

}
