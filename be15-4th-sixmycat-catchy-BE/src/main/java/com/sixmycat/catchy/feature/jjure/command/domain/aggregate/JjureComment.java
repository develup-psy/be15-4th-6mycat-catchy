package com.sixmycat.catchy.feature.jjure.command.domain.aggregate;

import com.sixmycat.catchy.common.domain.TargetType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "comment")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class JjureComment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long commentId;

    @Column(name = "member_id", nullable = false)
    private Long memberId; // 기존 Member → Long

    @Column(name = "target_id", nullable = false)
    private Long targetId;

    @Enumerated(EnumType.STRING)
    @Column(name = "target_type", nullable = false)
    private TargetType targetType;

    @Column(name = "parent_comment_id")
    private Long parentCommentId;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private String content;

    public static JjureComment create(Long memberId, Long targetId, TargetType targetType, String content, Long parentCommentId) {
        JjureComment comment = new JjureComment();
        comment.memberId = memberId;
        comment.targetId = targetId;
        comment.targetType = targetType;
        comment.content = content;
        comment.parentCommentId = parentCommentId;
        return comment;
    }
}
