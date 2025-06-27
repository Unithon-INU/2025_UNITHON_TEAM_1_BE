package com.example.unithon.domain.post.comment.dto.res;

import com.example.unithon.domain.post.comment.entity.PostComment;
import lombok.Builder;
import lombok.Getter;

@Getter
public class PostCommentCreateResDto {
    private final Long id;
    private final Long memberId;
    private final String content;
    private final boolean isReply;

    @Builder
    private PostCommentCreateResDto(Long id, Long memberId, String content, boolean isReply) {
        this.id = id;
        this.memberId = memberId;
        this.content = content;
        this.isReply = isReply;
    }

    public static PostCommentCreateResDto from(PostComment comment) {
        return PostCommentCreateResDto.builder()
                .id(comment.getId())
                .memberId(comment.getMember().getId())
                .content(comment.getContent())
                .isReply(comment.isReply())
                .build();
    }
}