package com.example.unithon.domain.post.comment.dto.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class PostCommentUpdateReqDto {

    @NotBlank(message = "댓글 내용을 입력해주세요.")
    @Schema(example = "Updated comment content.")
    private String content;
}