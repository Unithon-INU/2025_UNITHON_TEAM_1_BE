package com.example.unithon.domain.member.dto.req;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class MemberUpdateReqDto {

    @Schema(example = "new nickname", description = "닉네임 (변경하지 않으려면 기존 값 유지)")
    private String nickname;

    @Schema(example = "currentPassword123", description = "현재 비밀번호 (비밀번호 변경 시 필수)")
    private String currentPassword;

    @Schema(example = "newPassword123", description = "새 비밀번호")
    private String newPassword;

    @Schema(example = "newPassword123", description = "새 비밀번호 확인")
    private String confirmNewPassword;
}