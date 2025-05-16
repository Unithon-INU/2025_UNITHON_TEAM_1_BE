package com.example.uniton.domain.member.dto.res;

import com.example.uniton.domain.member.Member;
import lombok.Builder;
import lombok.Getter;

@Getter
public class SignupResDto {
    private final Long id;
    private final String loginId;

    @Builder
    private SignupResDto(Long id, String loginId) {
        this.id = id;
        this.loginId = loginId;
    }

    public static SignupResDto from(Member member) {
        return SignupResDto.builder()
                .id(member.getId())
                .loginId(member.getLoginId())
                .build();
    }
}
