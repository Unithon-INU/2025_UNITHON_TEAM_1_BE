package com.example.unithon.domain.member.dto.res;

import com.example.unithon.domain.member.Member;
import com.example.unithon.domain.member.Role;
import lombok.Builder;
import lombok.Getter;

@Getter
public class MemberGetResDto {
    private final Long id;
    private final String email;
    private final String nickname;
    private final Role role;

    @Builder
    private MemberGetResDto(Long id, String email, String nickname, Role role) {
        this.id = id;
        this.email = email;
        this.nickname = nickname;
        this.role = role;
    }

    public static MemberGetResDto from(Member member) {
        return MemberGetResDto.builder()
                .id(member.getId())
                .email(member.getEmail())
                .nickname(member.getNickname())
                .role(member.getRole())
                .build();
    }
}