package com.example.unithon.domain.notification.controller;

import com.example.unithon.domain.member.entity.Member;
import com.example.unithon.domain.notification.dto.res.NotificationResDto;
import com.example.unithon.domain.notification.dto.res.NotificationSummaryResDto;
import com.example.unithon.domain.notification.service.NotificationService;
import com.example.unithon.global.exception.CustomException;
import com.example.unithon.global.exception.ErrorCode;
import com.example.unithon.global.jwt.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notifications")
public class NotificationController implements NotificationApiDocs {

    private final NotificationService notificationService;

    @Override
    public ResponseEntity<List<NotificationResDto>> getNotifications(@AuthenticationPrincipal UserDetails user) {
        Member member = getCurrentMember(user);
        return ResponseEntity.ok(notificationService.getNotifications(member));
    }

    @Override
    public ResponseEntity<NotificationSummaryResDto> getNotificationSummary(@AuthenticationPrincipal UserDetails user) {
        Member member = getCurrentMember(user);
        return ResponseEntity.ok(notificationService.getNotificationSummary(member));
    }

    @Override
    public ResponseEntity<Void> markAsRead(@AuthenticationPrincipal UserDetails user,
                                           @PathVariable Long notificationId) {
        Member member = getCurrentMember(user);
        notificationService.markAsRead(notificationId, member);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Void> markAllAsRead(@AuthenticationPrincipal UserDetails user) {
        Member member = getCurrentMember(user);
        notificationService.markAllAsRead(member);
        return ResponseEntity.ok().build();
    }

    // 현재 로그인한 사용자의 Member 객체 추출
    private Member getCurrentMember(UserDetails userDetails) {
        if (userDetails instanceof CustomUserDetails) {
            return ((CustomUserDetails) userDetails).getMember();
        }
        throw new CustomException(ErrorCode.UNAUTHORIZED_LOGIN);
    }
}