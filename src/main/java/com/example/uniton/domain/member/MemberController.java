package com.example.uniton.domain.member;

import com.example.uniton.domain.member.dto.req.LoginReqDto;
import com.example.uniton.domain.member.dto.req.SignupReqDto;
import com.example.uniton.domain.member.dto.res.LoginResDto;
import com.example.uniton.domain.member.dto.res.SignupResDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/members")
public class MemberController implements MemberApiDocs{

    private final MemberService memberService;

    @Override
    public ResponseEntity<SignupResDto> signup(@Valid @RequestBody SignupReqDto signupRequest) {
        return ResponseEntity.ok(memberService.signup(signupRequest));
    }

    @Override
    public ResponseEntity<LoginResDto> login(@Valid @RequestBody LoginReqDto loginRequest) {
        return ResponseEntity.ok(memberService.login(loginRequest));
    }
}
