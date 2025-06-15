package com.example.unithon.domain.member.dto.res;

import com.example.unithon.domain.member.Member;
import lombok.Builder;
import lombok.Getter;

@Getter
public class MemberSignupResDto {
    private final Long id;
    private final String email;

    @Builder
    private MemberSignupResDto(Long id, String email) {
        this.id = id;
        this.email = email;
    }

    public static MemberSignupResDto from(Member member) {
        return MemberSignupResDto.builder()
                .id(member.getId())
                .email(member.getEmail())
                .build();
    }
}
