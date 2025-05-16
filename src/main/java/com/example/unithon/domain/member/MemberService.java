package com.example.unithon.domain.member;


import com.example.unithon.domain.member.dto.req.LoginReqDto;
import com.example.unithon.domain.member.dto.req.SignupReqDto;
import com.example.unithon.domain.member.dto.res.LoginResDto;
import com.example.unithon.domain.member.dto.res.SignupResDto;
import com.example.unithon.global.exception.CustomException;
import com.example.unithon.global.exception.ErrorCode;
import com.example.unithon.global.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    //회원가입
    @Transactional
    public SignupResDto signup(SignupReqDto signupRequest) {
        if (memberRepository.existsByLoginId(signupRequest.getLoginId())) {
            throw new CustomException(ErrorCode.DUPLICATE_LOGIN_ID);
        }

        Member member = Member.builder()
                .loginId(signupRequest.getLoginId())
                .email(signupRequest.getEmail())
                .password(passwordEncoder.encode(signupRequest.getPassword()))
                .role(Role.USER) // USER 권한 부여
                .build();
        memberRepository.save(member);
        log.info("[회원가입 성공] loginId: {}", member.getLoginId());
        return SignupResDto.from(member);
    }

    //로그인
    @Transactional
    public LoginResDto login(LoginReqDto loginRequest) {
        log.info("[로그인 시도] loginId: {}", loginRequest.getLoginId());
        Member member = memberRepository.findByLoginId(loginRequest.getLoginId())
                .orElseThrow(() -> {
                    log.warn("[로그인 실패] 존재하지 않는 ID: {}", loginRequest.getLoginId());
                    return new CustomException(ErrorCode.ID_NOT_FOUND);
                });

        if (!passwordEncoder.matches(loginRequest.getPassword(), member.getPassword())) {
            log.warn("[로그인 실패] 비밀번호 불일치 - loginId: {}", loginRequest.getLoginId());
            throw new CustomException(ErrorCode.UNAUTHORIZED_LOGIN);
        }
        // 토큰 생성
        String token = jwtTokenProvider.generateAccessToken(member.getLoginId());
        log.info("[로그인 성공] loginId: {}", member.getLoginId());
        return LoginResDto.from(member, token);
    }
}