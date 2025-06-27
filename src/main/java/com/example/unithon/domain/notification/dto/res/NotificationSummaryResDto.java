package com.example.unithon.domain.notification.dto.res;

import lombok.Builder;
import lombok.Getter;

@Getter
public class NotificationSummaryResDto {
    private final int unreadCount;
    private final boolean hasUnread;

    @Builder
    private NotificationSummaryResDto(int unreadCount, boolean hasUnread) {
        this.unreadCount = unreadCount;
        this.hasUnread = hasUnread;
    }

    public static NotificationSummaryResDto of(int unreadCount) {
        return NotificationSummaryResDto.builder()
                .unreadCount(unreadCount)
                .hasUnread(unreadCount > 0)
                .build();
    }
}