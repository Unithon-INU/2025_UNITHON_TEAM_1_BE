package com.example.unithon.domain.member.dto.res;

import com.example.unithon.domain.member.Member;
import lombok.Builder;
import lombok.Getter;

@Getter
public class SignupResDto {
    private final Long id;
    private final String email;

    @Builder
    private SignupResDto(Long id, String email) {
        this.id = id;
        this.email = email;
    }

    public static SignupResDto from(Member member) {
        return SignupResDto.builder()
                .id(member.getId())
                .email(member.getEmail())
                .build();
    }
}
