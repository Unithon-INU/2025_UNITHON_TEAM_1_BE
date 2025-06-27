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
    private final String profileImageUrl;

    @Builder
    private MemberMyPageResDto(Long id, String email, String nickname, Role role, String profileImageUrl) {
        this.id = id;
        this.email = email;
        this.nickname = nickname;
        this.role = role;
        this.profileImageUrl = profileImageUrl;
    }

    public static MemberMyPageResDto from(Member member) {
        return MemberMyPageResDto.builder()
                .id(member.getId())
                .email(member.getEmail())
                .nickname(member.getNickname())
                .role(member.getRole())
                .profileImageUrl(member.getProfileImageUrl())
                .build();
    }
}