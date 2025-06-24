package com.example.unithon.domain.club.controller;

import com.example.unithon.domain.club.Division;
import com.example.unithon.domain.club.dto.req.ClubCreateReqDto;
import com.example.unithon.domain.club.dto.req.ClubUpdateReqDto;
import com.example.unithon.domain.club.dto.res.ClubCreateResDto;
import com.example.unithon.domain.club.dto.res.ClubGetResDto;
import com.example.unithon.domain.club.service.ClubService;
import com.example.unithon.domain.member.Member;
import com.example.unithon.global.exception.CustomException;
import com.example.unithon.global.exception.ErrorCode;
import com.example.unithon.global.jwt.CustomUserDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/clubs")
public class ClubController implements ClubApiDocs {

    private final ClubService clubService;

    @Override
    public ResponseEntity<ClubCreateResDto> createClub(@AuthenticationPrincipal UserDetails user,
                                                       @Valid @RequestBody ClubCreateReqDto createRequest) {
        Member member = getCurrentMember(user);
        return ResponseEntity.ok(clubService.createClub(member, createRequest));
    }

    @Override
    public ResponseEntity<ClubGetResDto> getClub(@PathVariable Long clubId) {
        return ResponseEntity.ok(clubService.getClub(clubId));
    }

    @Override
    public ResponseEntity<List<ClubGetResDto>> getClubList(@RequestParam(required = false) Division division) {
        // 분야별 필터
        if (division != null) {
            return ResponseEntity.ok(clubService.getClubsByCategory(division));
        }

        // 기본(전체 조회, 최신순)
        return ResponseEntity.ok(clubService.getClubList());
    }

    @Override
    public ResponseEntity<Void> updateClub(@AuthenticationPrincipal UserDetails user,
                                           @PathVariable Long clubId,
                                           @Valid @RequestBody ClubUpdateReqDto updateRequest) {
        Member member = getCurrentMember(user);
        clubService.updateClub(clubId, member, updateRequest);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Void> deleteClub(@AuthenticationPrincipal UserDetails user,
                                           @PathVariable Long clubId) {
        clubService.deleteClub(clubId);
        return ResponseEntity.noContent().build();
    }

    // 현재 로그인한 사용자의 Member 객체 추출
    private Member getCurrentMember(UserDetails userDetails) {
        if (userDetails instanceof CustomUserDetails) {
            return ((CustomUserDetails) userDetails).getMember();
        }
        throw new CustomException(ErrorCode.UNAUTHORIZED_LOGIN);
    }
}