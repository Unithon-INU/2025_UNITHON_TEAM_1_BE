package com.example.unithon.domain.member.dto.res;

import com.example.unithon.domain.member.Member;
import lombok.Builder;
import lombok.Getter;

@Getter
public class MemberUpdateResDto {
    private final Long id;
    private final String email;
    private final String nickname;

    @Builder
    private MemberUpdateResDto(Long id, String email, String nickname) {
        this.id = id;
        this.email = email;
        this.nickname = nickname;
    }

    public static MemberUpdateResDto from(Member member) {
        return MemberUpdateResDto.builder()
                .id(member.getId())
                .email(member.getEmail())
                .nickname(member.getNickname())
                .build();
    }
}