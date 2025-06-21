package com.example.unithon.domain.post.dto.req;

import com.example.unithon.domain.post.Category;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class PostUploadReqDto {

    @NotNull(message = "카테고리를 선택해주세요.")
    @Schema(example = "HOUSING/JOBS/STUDY/SOCIAL/HELP")
    private Category category;

    @NotBlank(message = "제목을 입력해주세요.")
    @Schema(example = "How to find a part-time job in Songdo?")
    private String title;

    @NotBlank(message = "내용을 입력해주세요.")
    @Schema(example = "Any advice would be greatly appreciated!")
    private String content;
}