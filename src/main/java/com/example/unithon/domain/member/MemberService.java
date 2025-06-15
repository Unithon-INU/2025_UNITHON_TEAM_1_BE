package com.example.unithon.domain.member;


import com.example.unithon.domain.member.dto.req.MemberLoginReqDto;
import com.example.unithon.domain.member.dto.req.MemberSignupReqDto;
import com.example.unithon.domain.member.dto.res.MemberGetResDto;
import com.example.unithon.domain.member.dto.res.MemberLoginResDto;
import com.example.unithon.domain.member.dto.res.MemberSignupResDto;
import com.example.unithon.global.exception.CustomException;
import com.example.unithon.global.exception.ErrorCode;
import com.example.unithon.global.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    //회원가입
    @Transactional
    public MemberSignupResDto signup(MemberSignupReqDto signupRequest) {
        if (memberRepository.existsByEmail(signupRequest.getEmail())) {
            throw new CustomException(ErrorCode.DUPLICATE_EMAIL);
        }
        // 비밀번호 일치 확인
        if (!signupRequest.getPassword().equals(signupRequest.getConfirmPassword())) {
            throw new CustomException(ErrorCode.PASSWORD_MISMATCH);
        }

        Member member = Member.builder()
                .email(signupRequest.getEmail())
                .password(passwordEncoder.encode(signupRequest.getPassword()))
                .nickname(signupRequest.getNickname())
                .role(Role.USER) // USER 권한 부여
                .build();
        memberRepository.save(member);
        log.info("[회원가입 성공] email: {}", member.getEmail());
        return MemberSignupResDto.from(member);
    }

    //로그인
    @Transactional
    public MemberLoginResDto login(MemberLoginReqDto loginRequest) {
        log.info("[로그인 시도] email: {}", loginRequest.getEmail());
        Member member = memberRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> {
                    log.warn("[로그인 실패] 존재하지 않는 email: {}", loginRequest.getEmail());
                    return new CustomException(ErrorCode.EMAIL_NOT_FOUND);
                });

        if (!passwordEncoder.matches(loginRequest.getPassword(), member.getPassword())) {
            log.warn("[로그인 실패] 비밀번호 불일치 - email: {}", loginRequest.getEmail());
            throw new CustomException(ErrorCode.UNAUTHORIZED_LOGIN);
        }
        // 토큰 생성
        String token = jwtTokenProvider.generateAccessToken(member.getEmail());
        log.info("[로그인 성공] email: {}", member.getEmail());
        return MemberLoginResDto.from(member, token);
    }

    // 개별 회원 조회
    @Transactional(readOnly = true)
    public MemberGetResDto getMember(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> {
                    return new CustomException(ErrorCode.ID_NOT_FOUND);
                });
        return MemberGetResDto.from(member);
    }

    //전체 회원 조회
    @Transactional(readOnly = true)
    public List<MemberGetResDto> getMemberList() {
        return memberRepository.findAll().stream()
                .map(MemberGetResDto::from)
                .toList();
    }
}