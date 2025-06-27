package com.example.unithon.domain.job.dto.res;

import com.example.unithon.domain.job.entity.Job;
import lombok.Builder;
import lombok.Getter;

@Getter
public class JobCreateResDto {
    private final Long id;
    private final String title;
    private final String company;

    @Builder
    private JobCreateResDto(Long id, String title, String company) {
        this.id = id;
        this.title = title;
        this.company = company;
    }

    public static JobCreateResDto from(Job job) {
        return JobCreateResDto.builder()
                .id(job.getId())
                .title(job.getTitle())
                .company(job.getCompany())
                .build();
    }
}
