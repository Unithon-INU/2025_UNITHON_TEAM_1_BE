package com.example.unithon.domain.job.entity;

import com.example.unithon.domain.job.enums.JobField;
import com.example.unithon.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "jobs")
public class Job extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(nullable = false, length = 100)
    private String company;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private JobField jobField;

    @Column(nullable = false, length = 50)
    private String type;

    @Column(nullable = false, length = 50)
    private String visa;

    @Column(nullable = false, length = 100)
    private String salary;

    @Column(nullable = false, length = 200)
    private String location;

    @Column(nullable = false, length = 100)
    private String schedule;

    @Column(nullable = false, length = 100)
    private String experience;

    @Column(nullable = false, length = 100)
    private String language;

    @Column(nullable = false, length = 2000)
    private String description;

    @Column(nullable = false, length = 2000)
    private String responsibilities;

    @Column(nullable = false, length = 2000)
    private String requirements;

    @Column(nullable = false, length = 100)
    private String contactPerson;

    @Column(nullable = false, length = 100)
    private String contactEmail;

    @Column(nullable = false, length = 50)
    private String contactPhone;

    @Builder
    private Job(String title, String company, JobField jobField, String type, String visa,
                String salary, String location, String schedule, String experience, String language,
                String description, String responsibilities, String requirements,
                String contactPerson, String contactEmail, String contactPhone) {
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
        this.contactPerson = contactPerson;
        this.contactEmail = contactEmail;
        this.contactPhone = contactPhone;
    }

    public void updateJob(String title, String company, JobField jobField, String type, String visa,
                          String salary, String location, String schedule, String experience, String language,
                          String description, String responsibilities, String requirements,
                          String contactPerson, String contactEmail, String contactPhone) {
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
        this.contactPerson = contactPerson;
        this.contactEmail = contactEmail;
        this.contactPhone = contactPhone;
    }
}