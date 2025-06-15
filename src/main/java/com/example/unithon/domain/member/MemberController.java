package com.example.unithon.domain.member;

import com.example.unithon.domain.member.dto.req.MemberLoginReqDto;
import com.example.unithon.domain.member.dto.req.MemberSignupReqDto;
import com.example.unithon.domain.member.dto.res.MemberGetResDto;
import com.example.unithon.domain.member.dto.res.MemberLoginResDto;
import com.example.unithon.domain.member.dto.res.MemberSignupResDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/members")
public class MemberController implements MemberApiDocs{

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

    public ResponseEntity<MemberGetResDto> getMember(@PathVariable Long memberId) {
        return ResponseEntity.ok(memberService.getMember(memberId));
    }

    @Override
    public ResponseEntity<List<MemberGetResDto>> getMemberList() {
        return ResponseEntity.ok(memberService.getMemberList());
    }
}
