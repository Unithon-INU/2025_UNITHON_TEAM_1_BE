package com.example.unithon.domain.member;


import com.example.unithon.domain.member.dto.req.MemberLoginReqDto;
import com.example.unithon.domain.member.dto.req.MemberSignupReqDto;
import com.example.unithon.domain.member.dto.req.MemberUpdateReqDto;
import com.example.unithon.domain.member.dto.res.*;
import com.example.unithon.global.exception.CustomException;
import com.example.unithon.global.exception.ErrorCode;
import com.example.unithon.global.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

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


    // 마이페이지 조회
    @Transactional(readOnly = true)
    public MemberMyPageResDto getMyPage(Member currentMember) {
        // 최신 정보 조회
        Member member = memberRepository.findById(currentMember.getId())
                .orElseThrow(() -> new CustomException(ErrorCode.ID_NOT_FOUND));

        return MemberMyPageResDto.from(member);
    }

    // 회원 정보 수정
    @Transactional
    public MemberUpdateResDto updateMember(Member currentMember, MemberUpdateReqDto updateRequest) {
        Member member = memberRepository.findById(currentMember.getId())
                .orElseThrow(() -> new CustomException(ErrorCode.ID_NOT_FOUND));

        // 닉네임 업데이트 (값이 있을 때만)
        if (StringUtils.hasText(updateRequest.getNickname())) {
            member.updateMemberInfo(updateRequest.getNickname());
        }

        // 비밀번호 변경 로직 (선택사항)
        if (StringUtils.hasText(updateRequest.getNewPassword())) {
            // 새 비밀번호가 있으면 현재 비밀번호 확인이 필수
            if (!StringUtils.hasText(updateRequest.getCurrentPassword())) {
                throw new CustomException(ErrorCode.PASSWORD_MISMATCH);
            }

            // 현재 비밀번호 검증
            if (!passwordEncoder.matches(updateRequest.getCurrentPassword(), member.getPassword())) {
                throw new CustomException(ErrorCode.UNAUTHORIZED_LOGIN);
            }

            // 새 비밀번호 확인
            if (!updateRequest.getNewPassword().equals(updateRequest.getConfirmNewPassword())) {
                throw new CustomException(ErrorCode.PASSWORD_MISMATCH);
            }

            // 비밀번호 업데이트
            member.updatePassword(passwordEncoder.encode(updateRequest.getNewPassword()));
        }

        return MemberUpdateResDto.from(member);
    }
}