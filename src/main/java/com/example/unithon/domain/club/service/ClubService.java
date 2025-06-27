package com.example.unithon.domain.club.service;

import com.example.unithon.domain.club.Division;
import com.example.unithon.domain.club.dto.req.ClubCreateReqDto;
import com.example.unithon.domain.club.dto.req.ClubUpdateReqDto;
import com.example.unithon.domain.club.dto.res.ClubCreateResDto;
import com.example.unithon.domain.club.dto.res.ClubGetResDto;
import com.example.unithon.domain.club.entity.Club;
import com.example.unithon.domain.club.repository.ClubRepository;
import com.example.unithon.domain.member.Member;
import com.example.unithon.domain.member.MemberRepository;
import com.example.unithon.domain.member.Role;
import com.example.unithon.global.exception.CustomException;
import com.example.unithon.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ClubService {

    private final ClubRepository clubRepository;
    private final MemberRepository memberRepository;

    // 동아리 등록 (ADMIN만 가능)
    @Transactional
    public ClubCreateResDto createClub(Member member, ClubCreateReqDto createRequest) {
        validateAdminPermission(member);

        // 동아리명 중복 확인
        if (clubRepository.existsByName(createRequest.getName())) {
            throw new CustomException(ErrorCode.DUPLICATE_CLUB);
        }

        Club club = Club.builder()
                .division(createRequest.getDivision())
                .name(createRequest.getName())
                .summary(createRequest.getSummary())
                .description(createRequest.getDescription())
                .build();

        clubRepository.save(club);
        return ClubCreateResDto.from(club);
    }

    // 동아리 수정 (ADMIN만 가능)
    @Transactional
    public void updateClub(Long clubId, Member currentMember, ClubUpdateReqDto updateRequest) {
        validateAdminPermission(currentMember);

        Club club = clubRepository.findById(clubId)
                .orElseThrow(() -> new CustomException(ErrorCode.CLUB_NOT_FOUND));

        // 동아리명 중복 확인 (현재 동아리 제외)
        if (!club.getName().equals(updateRequest.getName()) &&
                clubRepository.existsByName(updateRequest.getName())) {
            throw new CustomException(ErrorCode.DUPLICATE_CLUB);
        }

        club.updateClub(
                updateRequest.getDivision(),
                updateRequest.getName(),
                updateRequest.getSummary(),
                updateRequest.getDescription()
        );
    }

    // 동아리 삭제 (ADMIN만 가능)
    @Transactional
    public void deleteClub(Long clubId) {
        Club club = clubRepository.findById(clubId)
                .orElseThrow(() -> new CustomException(ErrorCode.CLUB_NOT_FOUND));

        clubRepository.delete(club);
    }

    // 개별 동아리 조회 (모든 사용자 가능)
    @Transactional(readOnly = true)
    public ClubGetResDto getClub(Long clubId) {
        Club club = clubRepository.findById(clubId)
                .orElseThrow(() -> new CustomException(ErrorCode.CLUB_NOT_FOUND));
        return ClubGetResDto.from(club);
    }

    // 전체 동아리 조회 (최신순, 모든 사용자 가능)
    @Transactional(readOnly = true)
    public List<ClubGetResDto> getClubList() {
        return clubRepository.findAllByOrderByCreatedAtDesc().stream()
                .map(ClubGetResDto::from)
                .toList();
    }

    // 카테고리별 동아리 조회 (모든 사용자 가능)
    @Transactional(readOnly = true)
    public List<ClubGetResDto> getClubsByCategory(Division division) {
        return clubRepository.findByDivisionOrderByCreatedAtDesc(division).stream()
                .map(ClubGetResDto::from)
                .toList();
    }

    // ADMIN 권한 검증
    private void validateAdminPermission(Member member) {
        if (member.getRole() != Role.ADMIN) {
            throw new CustomException(ErrorCode.FORBIDDEN_PERMISSION);
        }
    }
}