package com.example.unithon.domain.post.comment.dto.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class PostCommentCreateReqDto {

    @NotBlank(message = "댓글 내용을 입력해주세요.")
    @Schema(example = "Great post! Thanks for sharing.")
    private String content;

    @NotNull(message = "부모 댓글 ID를 입력해주세요.")
    @Schema(example = "0", description = "답댓글인 경우 부모 댓글의 ID (0이면 댓글))")
    private Long parentId;
}