package com.example.unithon.domain.member.controller;

import com.example.unithon.domain.member.dto.req.*;
import com.example.unithon.domain.member.dto.res.*;
import com.example.unithon.domain.member.entity.Member;
import com.example.unithon.domain.member.service.MemberService;
import com.example.unithon.global.exception.CustomException;
import com.example.unithon.global.exception.ErrorCode;
import com.example.unithon.global.jwt.CustomUserDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/members")
public class MemberController implements MemberApiDocs {

    private final MemberService memberService;

    @Override
    public ResponseEntity<MemberSignupResDto> signup(@Valid @RequestBody MemberSignupReqDto signupRequest) {
        return ResponseEntity.ok(memberService.signup(signupRequest));
    }

    @Override
    public ResponseEntity<MemberLoginResDto> login(@Valid @RequestBody MemberLoginReqDto loginRequest) {
        return ResponseEntity.ok(memberService.login(loginRequest));
    }

    @Override
    public ResponseEntity<MemberTokenRefreshResDto> refreshToken(@Valid @RequestBody MemberTokenRefreshReqDto refreshRequest) {
        return ResponseEntity.ok(memberService.refreshToken(refreshRequest));
    }

    @Override
    public ResponseEntity<MemberGetResDto> getMember(@PathVariable Long memberId) {
        return ResponseEntity.ok(memberService.getMember(memberId));
    }

    @Override
    public ResponseEntity<List<MemberGetResDto>> getMemberList() {
        return ResponseEntity.ok(memberService.getMemberList());
    }

    @Override
    public ResponseEntity<MemberMyPageResDto> getMyPage(@AuthenticationPrincipal UserDetails user) {
        Member member = getCurrentMember(user);
        return ResponseEntity.ok(memberService.getMyPage(member));
    }

    @Override
    public ResponseEntity<MemberUpdateResDto> updateMember(@AuthenticationPrincipal UserDetails user,
                                                           @Valid @RequestBody MemberUpdateReqDto updateRequest) {
        Member member = getCurrentMember(user);
        return ResponseEntity.ok(memberService.updateMember(member, updateRequest));
    }

    @Override
    public ResponseEntity<MemberUpdateResDto> updateProfile(@AuthenticationPrincipal UserDetails user,
                                                            @RequestParam(value = "nickname", required = false) String nickname,
                                                            @RequestParam(value = "profileImage", required = false) MultipartFile profileImage) {
        Member member = getCurrentMember(user);

        MemberImageUploadReqDto imageUploadRequest = new MemberImageUploadReqDto(nickname);

        return ResponseEntity.ok(memberService.updateProfile(member, imageUploadRequest, profileImage));
    }

    @Override
    public ResponseEntity<MemberUpdateResDto> deleteProfileImage(@AuthenticationPrincipal UserDetails user) {
        Member member = getCurrentMember(user);
        return ResponseEntity.ok(memberService.deleteProfileImage(member));
    }

    private Member getCurrentMember(UserDetails userDetails) {
        if (userDetails instanceof CustomUserDetails) {
            return ((CustomUserDetails) userDetails).getMember();
        }
        throw new CustomException(ErrorCode.UNAUTHORIZED_LOGIN);
    }
}