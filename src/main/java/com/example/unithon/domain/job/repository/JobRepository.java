package com.example.unithon.domain.job.repository;

import com.example.unithon.domain.job.JobField;
import com.example.unithon.domain.job.entity.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobRepository extends JpaRepository<Job, Long> {

    // (전체) 최신순 조회
    List<Job> findAllByOrderByCreatedAtDesc();

    // (분야별) 최신순 조회
    List<Job> findByJobFieldOrderByCreatedAtDesc(JobField jobField);

    // 제목 검색
    List<Job> findByTitleContainingIgnoreCaseOrderByCreatedAtDesc(String keyword);

    // 회사명 검색
    List<Job> findByCompanyContainingIgnoreCaseOrderByCreatedAtDesc(String keyword);
}