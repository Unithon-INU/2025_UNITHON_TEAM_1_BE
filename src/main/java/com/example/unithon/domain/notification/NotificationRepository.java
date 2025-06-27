package com.example.unithon.domain.notification;

import com.example.unithon.domain.member.entity.Member;
import com.example.unithon.domain.notification.entity.Notification;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    // 특정 사용자의 알림 목록 조회 (최신순, 페이징)
    List<Notification> findByReceiverOrderByCreatedAtDesc(Member receiver, Pageable pageable);
    // 특정 사용자의 읽지 않은 알림 개수
    int countByReceiverAndIsReadFalse(Member receiver);

    // 특정 사용자의 읽지 않은 알림 목록 조회
    List<Notification> findByReceiverAndIsReadFalseOrderByCreatedAtDesc(Member receiver);

    // 특정 사용자의 모든 알림을 읽음으로 표시
    @Modifying
    @Query("UPDATE Notification n SET n.isRead = true WHERE n.receiver = :receiver AND n.isRead = false")
    void markAllAsReadByReceiver(@Param("receiver") Member receiver);

    // 특정 게시글과 관련된 알림 삭제 (게시글 삭제 시 사용)
    void deleteAllByPostId(Long postId);

    // 특정 댓글과 관련된 알림 삭제 (댓글 삭제 시 사용)
    void deleteAllByCommentId(Long commentId);
}