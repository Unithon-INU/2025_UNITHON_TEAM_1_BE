package com.example.uniton.global.jwt;

import com.example.uniton.domain.member.Member;
import com.example.uniton.domain.member.MemberRepository;
import com.example.uniton.global.exception.CustomException;
import com.example.uniton.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String loginId){
        Member member = memberRepository.findByLoginId(loginId)
                .orElseThrow(() -> new CustomException(ErrorCode.ID_NOT_FOUND));
        // CustomUserDetails로 감싸서 반환
        return new CustomUserDetails(member);
    }
}
