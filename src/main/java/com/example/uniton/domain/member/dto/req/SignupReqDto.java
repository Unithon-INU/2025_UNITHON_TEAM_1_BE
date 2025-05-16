package com.example.uniton.domain.member.dto.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class SignupReqDto {

    @NotBlank(message = "ID를 입력해주세요.")
    @Schema(example = "test")
    private String loginId;

    @NotBlank(message = "이메일을 입력해주세요.")
    @Email(message = "올바른 이메일 형식을 입력해주세요.")
    @Schema(example = "test@naver.com")
    private String email;

    @NotBlank(message = "비밀번호를 입력해주세요.")
    @Schema(example = "test123")
    private String password;
}