package com.example.unithon.domain.notification.service;

import com.example.unithon.domain.member.entity.Member;
import com.example.unithon.domain.notification.NotificationRepository;
import com.example.unithon.domain.notification.NotificationType;
import com.example.unithon.domain.notification.dto.res.NotificationResDto;
import com.example.unithon.domain.notification.dto.res.NotificationSummaryResDto;
import com.example.unithon.domain.notification.entity.Notification;
import com.example.unithon.domain.post.comment.entity.PostComment;
import com.example.unithon.domain.post.entity.Post;
import com.example.unithon.global.exception.CustomException;
import com.example.unithon.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;

    // 내 게시글에 댓글이 달렸을 때 알림 생성
    @Transactional
    public void createPostCommentNotification(Post post, PostComment comment) {
        // 자신의 게시글에 자신이 댓글을 단 경우는 알림 생성하지 않음
        if (post.getMember().getId().equals(comment.getMember().getId())) {
            return;
        }

        String message = String.format("%s commented on your post '%s'",
                comment.getMember().getNickname(),
                truncateTitle(post.getTitle()));

        Notification notification = Notification.builder()
                .receiver(post.getMember())
                .sender(comment.getMember())
                .type(NotificationType.POST_COMMENT)
                .message(message)
                .postId(post.getId())
                .commentId(comment.getId())
                .build();

        notificationRepository.save(notification);
        log.info("게시글 댓글 알림 생성: postId={}, commentId={}", post.getId(), comment.getId());
    }

    // 내 댓글에 답글이 달렸을 때 알림 생성
    @Transactional
    public void createCommentReplyNotification(PostComment parentComment, PostComment reply) {
        // 자신의 댓글에 자신이 답글을 단 경우는 알림 생성하지 않음
        if (parentComment.getMember().getId().equals(reply.getMember().getId())) {
            return;
        }

        String message = String.format("%s replied to your comment",
                reply.getMember().getNickname());

        Notification notification = Notification.builder()
                .receiver(parentComment.getMember())
                .sender(reply.getMember())
                .type(NotificationType.COMMENT_REPLY)
                .message(message)
                .postId(parentComment.getPost().getId())
                .commentId(reply.getId())
                .build();

        notificationRepository.save(notification);
        log.info("댓글 답글 알림 생성: parentCommentId={}, replyId={}", parentComment.getId(), reply.getId());
    }

    // 사용자의 알림 목록 조회 (최대 3개)
    @Transactional(readOnly = true)
    public List<NotificationResDto> getNotifications(Member member) {
        Pageable pageable = PageRequest.of(0, 3);
        return notificationRepository.findByReceiverOrderByCreatedAtDesc(member, pageable).stream()
                .map(NotificationResDto::from)
                .toList();
    }

    // 사용자의 읽지 않은 알림 개수 조회
    @Transactional(readOnly = true)
    public NotificationSummaryResDto getNotificationSummary(Member member) {
        int unreadCount = notificationRepository.countByReceiverAndIsReadFalse(member);
        return NotificationSummaryResDto.of(unreadCount);
    }

    // 특정 알림을 읽음으로 표시
    @Transactional
    public void markAsRead(Long notificationId, Member member) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOTIFICATION_NOT_FOUND));

        // 자신의 알림인지 확인
        if (!notification.getReceiver().getId().equals(member.getId())) {
            throw new CustomException(ErrorCode.FORBIDDEN_PERMISSION);
        }

        notification.markAsRead();
    }

    // 모든 알림을 읽음으로 표시
    @Transactional
    public void markAllAsRead(Member member) {
        notificationRepository.markAllAsReadByReceiver(member);
    }

    // 게시글 삭제 시 관련 알림들 삭제
    @Transactional
    public void deleteNotificationsByPost(Long postId) {
        notificationRepository.deleteAllByPostId(postId);
    }

    // 댓글 삭제 시 관련 알림들 삭제
    @Transactional
    public void deleteNotificationsByComment(Long commentId) {
        notificationRepository.deleteAllByCommentId(commentId);
    }

    // 제목이 길면 잘라서 표시
    private String truncateTitle(String title) {
        if (title.length() > 20) {
            return title.substring(0, 20) + "...";
        }
        return title;
    }
}