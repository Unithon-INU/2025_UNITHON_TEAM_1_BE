package com.example.unithon.domain.member.dto.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class MemberTokenRefreshReqDto {
    @NotBlank(message = "refresh token을 입력해주세요.")
    @Schema(example = "eyJhbGciOiJIUzI1NiJ9...")
    private String refreshToken;
}
