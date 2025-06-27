package com.example.unithon.domain.job.controller;

import com.example.unithon.domain.job.JobField;
import com.example.unithon.domain.job.dto.req.JobCreateReqDto;
import com.example.unithon.domain.job.dto.req.JobUpdateReqDto;
import com.example.unithon.domain.job.dto.res.JobCreateResDto;
import com.example.unithon.domain.job.dto.res.JobGetResDto;
import com.example.unithon.domain.job.service.JobService;
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
@RequestMapping("/api/jobs")
public class JobController implements JobApiDocs {

    private final JobService jobService;

    @Override
    public ResponseEntity<JobCreateResDto> createJob(@AuthenticationPrincipal UserDetails user,
                                                     @Valid @RequestBody JobCreateReqDto createRequest) {
        Member member = getCurrentMember(user);
        return ResponseEntity.ok(jobService.createJob(member, createRequest));
    }

    @Override
    public ResponseEntity<JobGetResDto> getJob(@PathVariable Long jobId) {
        return ResponseEntity.ok(jobService.getJob(jobId));
    }

    @Override
    public ResponseEntity<List<JobGetResDto>> getJobList(@RequestParam(required = false) JobField jobField,
                                                         @RequestParam(required = false) String searchType,
                                                         @RequestParam(required = false) String keyword) {
        // 검색 기능
        if (searchType != null && keyword != null && !keyword.trim().isEmpty()) {
            if ("title".equalsIgnoreCase(searchType)) {
                return ResponseEntity.ok(jobService.searchJobsByTitle(keyword));
            } else if ("company".equalsIgnoreCase(searchType)) {
                return ResponseEntity.ok(jobService.searchJobsByCompany(keyword));
            }
        }

        // 분야별 필터
        if (jobField != null) {
            return ResponseEntity.ok(jobService.getJobsByField(jobField));
        }

        // 전체 조회
        return ResponseEntity.ok(jobService.getJobList());
    }

    @Override
    public ResponseEntity<Void> updateJob(@AuthenticationPrincipal UserDetails user,
                                          @PathVariable Long jobId,
                                          @Valid @RequestBody JobUpdateReqDto updateRequest) {
        Member member = getCurrentMember(user);
        jobService.updateJob(jobId, member, updateRequest);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Void> deleteJob(@AuthenticationPrincipal UserDetails user,
                                          @PathVariable Long jobId) {
        Member member = getCurrentMember(user);
        jobService.deleteJob(jobId);
        return ResponseEntity.noContent().build();
    }

    private Member getCurrentMember(UserDetails userDetails) {
        if (userDetails instanceof CustomUserDetails) {
            return ((CustomUserDetails) userDetails).getMember();
        }
        throw new CustomException(ErrorCode.UNAUTHORIZED_LOGIN);
    }
}