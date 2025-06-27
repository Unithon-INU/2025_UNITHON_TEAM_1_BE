package com.example.unithon.domain.job.dto.res;

import com.example.unithon.domain.job.enums.JobField;
import com.example.unithon.domain.job.entity.Job;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class JobGetResDto {
    private final Long id;
    private final String title;
    private final String company;
    private final JobField jobField;
    private final String type;
    private final String visa;
    private final String salary;
    private final String location;
    private final String schedule;
    private final String experience;
    private final String language;
    private final String description;
    private final String responsibilities;
    private final String requirements;
    private final ContactInfo contact;
    private final LocalDateTime createdAt;

    @Builder
    private JobGetResDto(Long id, String title, String company, JobField jobField, String type,
                         String visa, String salary, String location, String schedule, String experience,
                         String language, String description, String responsibilities, String requirements,
                         ContactInfo contact, LocalDateTime createdAt) {
        this.id = id;
        this.title = title;
        this.company = company;
        this.jobField = jobField;
        this.type = type;
        this.visa = visa;
        this.salary = salary;
        this.location = location;
        this.schedule = schedule;
        this.experience = experience;
        this.language = language;
        this.description = description;
        this.responsibilities = responsibilities;
        this.requirements = requirements;
        this.contact = contact;
        this.createdAt = createdAt;
    }

    public static JobGetResDto from(Job job) {
        ContactInfo contact = ContactInfo.builder()
                .person(job.getContactPerson())
                .email(job.getContactEmail())
                .phone(job.getContactPhone())
                .build();

        return JobGetResDto.builder()
                .id(job.getId())
                .title(job.getTitle())
                .company(job.getCompany())
                .jobField(job.getJobField())
                .type(job.getType())
                .visa(job.getVisa())
                .salary(job.getSalary())
                .location(job.getLocation())
                .schedule(job.getSchedule())
                .experience(job.getExperience())
                .language(job.getLanguage())
                .description(job.getDescription())
                .responsibilities(job.getResponsibilities())
                .requirements(job.getRequirements())
                .contact(contact)
                .createdAt(job.getCreatedAt())
                .build();
    }

    @Getter
    @Builder
    public static class ContactInfo {
        private final String person;
        private final String email;
        private final String phone;
    }
}