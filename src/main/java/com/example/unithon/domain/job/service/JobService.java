package com.example.unithon.domain.job.service;

import com.example.unithon.domain.job.enums.JobField;
import com.example.unithon.domain.job.dto.req.JobCreateReqDto;
import com.example.unithon.domain.job.dto.req.JobUpdateReqDto;
import com.example.unithon.domain.job.dto.res.JobCreateResDto;
import com.example.unithon.domain.job.dto.res.JobGetResDto;
import com.example.unithon.domain.job.entity.Job;
import com.example.unithon.domain.job.repository.JobRepository;
import com.example.unithon.domain.member.entity.Member;
import com.example.unithon.domain.member.enums.Role;
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
public class JobService {

    private final JobRepository jobRepository;

    // 채용 공고 등록 (ADMIN만 가능)
    @Transactional
    public JobCreateResDto createJob(Member member, JobCreateReqDto createRequest) {
        validateAdminPermission(member);

        Job job = Job.builder()
                .title(createRequest.getTitle())
                .company(createRequest.getCompany())
                .jobField(createRequest.getJobField())
                .type(createRequest.getType())
                .visa(createRequest.getVisa())
                .salary(createRequest.getSalary())
                .location(createRequest.getLocation())
                .schedule(createRequest.getSchedule())
                .experience(createRequest.getExperience())
                .language(createRequest.getLanguage())
                .description(createRequest.getDescription())
                .responsibilities(createRequest.getResponsibilities())
                .requirements(createRequest.getRequirements())
                .contactPerson(createRequest.getContactPerson())
                .contactEmail(createRequest.getContactEmail())
                .contactPhone(createRequest.getContactPhone())
                .build();

        jobRepository.save(job);
        log.info("[채용 공고 등록 완료] title: {}, company: {}", job.getTitle(), job.getCompany());
        return JobCreateResDto.from(job);
    }

    // 채용 공고 수정 (ADMIN만 가능)
    @Transactional
    public void updateJob(Long jobId, Member currentMember, JobUpdateReqDto updateRequest) {
        validateAdminPermission(currentMember);

        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new CustomException(ErrorCode.JOB_NOT_FOUND));

        job.updateJob(
                updateRequest.getTitle(),
                updateRequest.getCompany(),
                updateRequest.getJobField(),
                updateRequest.getType(),
                updateRequest.getVisa(),
                updateRequest.getSalary(),
                updateRequest.getLocation(),
                updateRequest.getSchedule(),
                updateRequest.getExperience(),
                updateRequest.getLanguage(),
                updateRequest.getDescription(),
                updateRequest.getResponsibilities(),
                updateRequest.getRequirements(),
                updateRequest.getContactPerson(),
                updateRequest.getContactEmail(),
                updateRequest.getContactPhone()
        );

        log.info("[채용 공고 수정 완료] jobId: {}, title: {}", jobId, job.getTitle());
    }

    // 채용 공고 삭제 (ADMIN만 가능)
    @Transactional
    public void deleteJob(Long jobId) {
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new CustomException(ErrorCode.JOB_NOT_FOUND));

        jobRepository.delete(job);
        log.info("[채용 공고 삭제 완료] jobId: {}, title: {}", jobId, job.getTitle());
    }

    // 개별 채용 공고 조회 (모든 사용자 가능)
    @Transactional(readOnly = true)
    public JobGetResDto getJob(Long jobId) {
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new CustomException(ErrorCode.JOB_NOT_FOUND));
        return JobGetResDto.from(job);
    }

    // 전체 채용 공고 조회 (최신순, 모든 사용자 가능)
    @Transactional(readOnly = true)
    public List<JobGetResDto> getJobList() {
        return jobRepository.findAllByOrderByCreatedAtDesc().stream()
                .map(JobGetResDto::from)
                .toList();
    }

    // 분야별 채용 공고 조회 (모든 사용자 가능)
    @Transactional(readOnly = true)
    public List<JobGetResDto> getJobsByField(JobField jobField) {
        return jobRepository.findByJobFieldOrderByCreatedAtDesc(jobField).stream()
                .map(JobGetResDto::from)
                .toList();
    }

    // 제목으로 채용 공고 검색 (모든 사용자 가능)
    @Transactional(readOnly = true)
    public List<JobGetResDto> searchJobsByTitle(String keyword) {
        return jobRepository.findByTitleContainingIgnoreCaseOrderByCreatedAtDesc(keyword).stream()
                .map(JobGetResDto::from)
                .toList();
    }

    // 회사명으로 채용 공고 검색 (모든 사용자 가능)
    @Transactional(readOnly = true)
    public List<JobGetResDto> searchJobsByCompany(String keyword) {
        return jobRepository.findByCompanyContainingIgnoreCaseOrderByCreatedAtDesc(keyword).stream()
                .map(JobGetResDto::from)
                .toList();
    }

    // ADMIN 권한 검증
    private void validateAdminPermission(Member member) {
        if (member.getRole() != Role.ADMIN) {
            throw new CustomException(ErrorCode.FORBIDDEN_PERMISSION);
        }
    }
}