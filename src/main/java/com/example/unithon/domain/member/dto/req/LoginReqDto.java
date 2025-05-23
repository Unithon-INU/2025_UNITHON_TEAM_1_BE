package com.example.unithon.domain.member.dto.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class LoginReqDto {

    @NotBlank(message = "ID를 입력해주세요.")
    @Schema(example = "test")
    private String loginId;

    @NotBlank(message = "비밀번호를 입력해주세요.")
    @Schema(example = "test123")
    private String password;
}
