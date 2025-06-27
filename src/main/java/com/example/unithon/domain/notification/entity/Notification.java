package com.example.unithon.domain.notification.entity;

import com.example.unithon.domain.member.entity.Member;
import com.example.unithon.domain.notification.NotificationType;
import com.example.unithon.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "notifications")
public class Notification extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 알림을 받을 사용자
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_id", nullable = false)
    private Member receiver;

    // 알림을 발생시킨 사용자 (댓글 작성자)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id", nullable = false)
    private Member sender;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NotificationType type;

    @Column(nullable = false, length = 200)
    private String message;

    // 관련 게시글 ID
    @Column(nullable = false)
    private Long postId;

    // 관련 댓글 ID (댓글인 경우)
    @Column
    private Long commentId;

    // 읽음 여부
    @Column(nullable = false)
    private Boolean isRead = false;

    @Builder
    private Notification(Member receiver, Member sender, NotificationType type,
                         String message, Long postId, Long commentId) {
        this.receiver = receiver;
        this.sender = sender;
        this.type = type;
        this.message = message;
        this.postId = postId;
        this.commentId = commentId;
        this.isRead = false;
    }

    public void markAsRead() {
        this.isRead = true;
    }
}