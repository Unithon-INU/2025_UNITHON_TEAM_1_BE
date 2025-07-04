package com.example.unithon.domain.member.dto.res;

import com.example.unithon.domain.member.entity.Member;
import lombok.Builder;
import lombok.Getter;

@Getter
public class MemberLoginResDto {
    private final String email;
    private final String accessToken;
    private final String refreshToken;

    @Builder
    private MemberLoginResDto(String email, String accessToken, String refreshToken) {
        this.email = email;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public static MemberLoginResDto from(Member member, String accessToken, String refreshToken) {
        return MemberLoginResDto.builder()
                .email(member.getEmail())
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }
}
