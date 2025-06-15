package com.example.unithon.domain.member.dto.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class SignupReqDto {

    @NotBlank(message = "이메일을 입력해주세요.")
    @Email(message = "올바른 이메일 형식을 입력해주세요.")
    @Schema(example = "test@naver.com")
    private String email;

    @NotBlank(message = "비밀번호는 필수입니다.")
    @Schema(example = "test123")
    private String password;

    @NotBlank(message = "비밀번호 확인은 필수입니다.")
    @Schema(example = "test123")
    private String confirmPassword;
}