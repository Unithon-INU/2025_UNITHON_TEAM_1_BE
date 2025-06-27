package com.example.unithon.domain.post.comment.dto.res;

import com.example.unithon.domain.post.comment.entity.PostComment;
import lombok.Builder;
import lombok.Getter;

@Getter
public class PostCommentCreateResDto {
    private final Long id;
    private final String content;
    private final boolean isReply;

    @Builder
    private PostCommentCreateResDto(Long id, String content, boolean isReply) {
        this.id = id;
        this.content = content;
        this.isReply = isReply;
    }

    public static PostCommentCreateResDto from(PostComment comment) {
        return PostCommentCreateResDto.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .isReply(comment.isReply())
                .build();
    }
}