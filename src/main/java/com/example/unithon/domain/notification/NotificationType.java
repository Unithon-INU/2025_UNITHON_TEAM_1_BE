package com.example.unithon.domain.notification;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum NotificationType {
    POST_COMMENT("내 게시글에 댓글이 달렸습니다"),
    COMMENT_REPLY("내 댓글에 답글이 달렸습니다");

    private final String message;
}