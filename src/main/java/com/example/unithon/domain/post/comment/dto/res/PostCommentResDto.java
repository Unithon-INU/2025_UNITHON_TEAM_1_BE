package com.example.unithon.domain.post.comment.dto.res;

import com.example.unithon.domain.post.comment.entity.PostComment;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class PostCommentResDto {
    private final Long id;
    private final Long memberId; // 댓글 작성자 ID
    private final String nickname;
    private final String content;
    private final LocalDateTime createdAt;
    private final boolean isReply;
    private final Long parentId; // 0이면 일반 댓글, 1이상이면 답댓글
    private final List<PostCommentResDto> replies;

    @Builder
    private PostCommentResDto(Long id, Long memberId, String nickname, String content,
                              LocalDateTime createdAt, boolean isReply,
                              Long parentId, List<PostCommentResDto> replies) {
        this.id = id;
        this.memberId = memberId;
        this.nickname = nickname;
        this.content = content;
        this.createdAt = createdAt;
        this.isReply = isReply;
        this.parentId = parentId;
        this.replies = replies;
    }

    public static PostCommentResDto from(PostComment comment) {
        return PostCommentResDto.builder()
                .id(comment.getId())
                .memberId(comment.getMember().getId())
                .nickname(comment.getMember().getNickname())
                .content(comment.getContent())
                .createdAt(comment.getCreatedAt())
                .isReply(comment.isReply())
                .parentId(comment.getParent() != null ? comment.getParent().getId() : 0L) // null이면 0 반환
                .replies(comment.getChildren().stream()
                        .map(PostCommentResDto::from)
                        .collect(Collectors.toList()))
                .build();
    }

    // 답댓글 정보 없이 단순 변환 (목록 조회 시 사용)
    public static PostCommentResDto fromSimple(PostComment comment) {
        return PostCommentResDto.builder()
                .id(comment.getId())
                .memberId(comment.getMember().getId()) // 댓글 작성자 ID 추가
                .nickname(comment.getMember().getNickname())
                .content(comment.getContent())
                .createdAt(comment.getCreatedAt())
                .isReply(comment.isReply())
                .parentId(comment.getParent() != null ? comment.getParent().getId() : 0L) // null이면 0 반환
                .replies(List.of()) // 빈 리스트로 초기화
                .build();
    }
}