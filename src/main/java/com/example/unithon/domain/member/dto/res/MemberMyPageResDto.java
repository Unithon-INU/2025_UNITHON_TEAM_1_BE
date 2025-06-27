package com.example.unithon.domain.member.dto.res;

import com.example.unithon.domain.member.entity.Member;
import com.example.unithon.domain.member.enums.Role;
import lombok.Builder;
import lombok.Getter;

@Getter
public class MemberMyPageResDto {
    private final Long id;
    private final String email;
    private final String nickname;
    private final Role role;

    @Builder
    private MemberMyPageResDto(Long id, String email, String nickname, Role role) {
        this.id = id;
        this.email = email;
        this.nickname = nickname;
        this.role = role;
    }

    public static MemberMyPageResDto from(Member member) {
        return MemberMyPageResDto.builder()
                .id(member.getId())
                .email(member.getEmail())
                .nickname(member.getNickname())
                .role(member.getRole())
                .build();
    }
}