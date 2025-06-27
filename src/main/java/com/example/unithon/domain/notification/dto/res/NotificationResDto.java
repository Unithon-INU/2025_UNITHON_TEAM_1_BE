package com.example.unithon.domain.notification.dto.res;

import com.example.unithon.domain.notification.NotificationType;
import com.example.unithon.domain.notification.entity.Notification;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class NotificationResDto {
    private final Long id;
    private final String senderNickname;
    private final NotificationType type;
    private final String message;
    private final Long postId;
    private final Long commentId;
    private final Boolean isRead;
    private final LocalDateTime createdAt;

    @Builder
    private NotificationResDto(Long id, String senderNickname, NotificationType type,
                               String message, Long postId, Long commentId,
                               Boolean isRead, LocalDateTime createdAt) {
        this.id = id;
        this.senderNickname = senderNickname;
        this.type = type;
        this.message = message;
        this.postId = postId;
        this.commentId = commentId;
        this.isRead = isRead;
        this.createdAt = createdAt;
    }

    public static NotificationResDto from(Notification notification) {
        return NotificationResDto.builder()
                .id(notification.getId())
                .senderNickname(notification.getSender().getNickname())
                .type(notification.getType())
                .message(notification.getMessage())
                .postId(notification.getPostId())
                .commentId(notification.getCommentId())
                .isRead(notification.getIsRead())
                .createdAt(notification.getCreatedAt())
                .build();
    }
}
