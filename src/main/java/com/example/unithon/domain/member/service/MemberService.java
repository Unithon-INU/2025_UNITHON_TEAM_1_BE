package com.example.unithon.domain.member.service;

import com.example.unithon.domain.member.dto.req.*;
import com.example.unithon.domain.member.dto.res.*;
import com.example.unithon.domain.member.entity.Member;
import com.example.unithon.domain.member.enums.Role;
import com.example.unithon.domain.member.repository.MemberRepository;
import com.example.unithon.global.exception.CustomException;
import com.example.unithon.global.exception.ErrorCode;
import com.example.unithon.global.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final MemberImageService memberImageService;

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
                .profileImageUrl(null) // 기본값 null
                .build();
        memberRepository.save(member);
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
        String accessToken = jwtTokenProvider.generateAccessToken(member.getEmail());
        String refreshToken = jwtTokenProvider.generateRefreshToken(member.getEmail());

        log.info("[로그인 성공] email: {}", member.getEmail());
        return MemberLoginResDto.from(member, accessToken, refreshToken);
    }

    // 토큰 갱신
    @Transactional
    public MemberTokenRefreshResDto refreshToken(MemberTokenRefreshReqDto refreshRequest) {
        String refreshToken = refreshRequest.getRefreshToken();

        // Refresh Token 유효성 검증
        if (!jwtTokenProvider.validateToken(refreshToken)) {
            throw new CustomException(ErrorCode.INVALID_REFRESH_TOKEN);
        }

        // Refresh Token에서 사용자 정보 추출
        String email = jwtTokenProvider.parseUsername(refreshToken);
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.EMAIL_NOT_FOUND));

        // 새로운 토큰들 생성
        String newAccessToken = jwtTokenProvider.generateAccessToken(member.getEmail());
        String newRefreshToken = jwtTokenProvider.generateRefreshToken(member.getEmail());

        return MemberTokenRefreshResDto.of(newAccessToken, newRefreshToken);
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

    // 회원 정보 수정 (텍스트 정보만)
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

    // 프로필 이미지 업로드/업데이트
    @Transactional
    public MemberUpdateResDto updateProfile(Member currentMember,
                                            MemberImageUploadReqDto updateRequest,
                                            MultipartFile profileImage) {
        Member member = memberRepository.findById(currentMember.getId())
                .orElseThrow(() -> new CustomException(ErrorCode.ID_NOT_FOUND));

        String profileImageUrl = member.getProfileImageUrl();

        // 새 프로필 이미지가 업로드된 경우
        if (profileImage != null && !profileImage.isEmpty()) {
            // 기존 프로필 이미지 삭제
            if (profileImageUrl != null) {
                memberImageService.deleteProfileImage(profileImageUrl);
            }
            // 새 프로필 이미지 업로드
            profileImageUrl = memberImageService.uploadProfileImage(profileImage);
        }

        // 닉네임 업데이트 (값이 있을 때만)
        String nickname = member.getNickname();
        if (StringUtils.hasText(updateRequest.getNickname())) {
            nickname = updateRequest.getNickname();
        }

        // 회원 정보 업데이트
        member.updateMemberInfo(nickname, profileImageUrl);

        log.info("[프로필 업데이트 완료] memberId: {}, nickname: {}, hasImage: {}",
                member.getId(), nickname, profileImageUrl != null);

        return MemberUpdateResDto.from(member);
    }

    // 프로필 이미지 삭제
    @Transactional
    public MemberUpdateResDto deleteProfileImage(Member currentMember) {
        Member member = memberRepository.findById(currentMember.getId())
                .orElseThrow(() -> new CustomException(ErrorCode.ID_NOT_FOUND));

        // S3에서 이미지 삭제
        if (member.getProfileImageUrl() != null) {
            memberImageService.deleteProfileImage(member.getProfileImageUrl());
        }

        // 데이터베이스에서 URL 제거
        member.updateProfileImage(null);

        log.info("[프로필 이미지 삭제 완료] memberId: {}", member.getId());

        return MemberUpdateResDto.from(member);
    }
}