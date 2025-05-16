package com.example.unithon.domain.member.dto.res;

import com.example.unithon.domain.member.Member;
import lombok.Builder;
import lombok.Getter;

@Getter
public class LoginResDto {
    private final Long loginId;
    private final String accessToken;
    private final String refreshToken;

    @Builder
    private LoginResDto(Long loginId, String name, String accessToken, String refreshToken) {
        this.loginId = loginId;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public static LoginResDto from(Member member, String token) {
        return LoginResDto.builder()
                .loginId(member.getId())
                .accessToken(token)
                .refreshToken(token)
                .build();
    }
}
