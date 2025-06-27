package com.example.unithon.domain.club.dto.req;

import com.example.unithon.domain.club.enums.Division;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class ClubCreateReqDto {

    @NotNull(message = "카테고리를 선택해주세요.")
    @Schema(example = "CULTURE")
    private Division division;

    @NotBlank(message = "동아리명을 입력해주세요.")
    @Schema(example = "INUO")
    private String name;

    @NotBlank(message = "동아리 요약을 입력해주세요.")
    @Schema(example = "Orchestra Club - Experience classical music and orchestral performances with fellow musicians.")
    private String summary;

    @NotBlank(message = "동아리 설명을 입력해주세요.")
    @Schema(example = "Incheon National University Orchestra (INUO) is an orchestra club at Incheon National University founded in 2016...")
    private String description;
}