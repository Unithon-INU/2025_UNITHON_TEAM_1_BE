package com.example.unithon.domain.member.dto.res;

import lombok.Builder;
import lombok.Getter;

@Getter
public class MemberTokenRefreshResDto {
    private final String accessToken;
    private final String refreshToken;

    @Builder
    private MemberTokenRefreshResDto(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public static MemberTokenRefreshResDto of(String accessToken, String refreshToken) {
        return MemberTokenRefreshResDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }
}
