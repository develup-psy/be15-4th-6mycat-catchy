package com.sixmycat.catchy.feature.notification.command.domain.aggregate;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Entity
@Table(name = "notification")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "member_id", nullable = false)
    private Long memberId;

    @Column(name = "sender_member_id", nullable = false)
    private Long senderMemberId;

    @Column(name = "related_id")
    private Long relatedId;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private NotificationType type;

    @Column(name = "content")
    private String content;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Builder
    public Notification(Long memberId, Long senderMemberId, Long relatedId, NotificationType type, String content) {
        this.memberId = memberId;
        this.senderMemberId = senderMemberId;
        this.relatedId = relatedId;
        this.type = type;
        this.content = content;
        this.createdAt = LocalDateTime.now();
    }
}
