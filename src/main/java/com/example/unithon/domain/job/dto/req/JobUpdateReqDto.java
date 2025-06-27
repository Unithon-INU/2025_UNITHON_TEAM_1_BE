package com.example.unithon.domain.job.dto.req;

import com.example.unithon.domain.job.enums.JobField;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class JobUpdateReqDto {

    @NotBlank(message = "채용 제목을 입력해주세요.")
    @Schema(example = "Campus Tour Guide")
    private String title;

    @NotBlank(message = "회사명을 입력해주세요.")
    @Schema(example = "Incheon National University")
    private String company;

    @NotNull(message = "직무 분야를 선택해주세요.")
    @Schema(example = "ETC")
    private JobField jobField;

    @NotBlank(message = "근무 형태를 입력해주세요.")
    @Schema(example = "Part-time")
    private String type;

    @NotBlank(message = "비자 정보를 입력해주세요.")
    @Schema(example = "E-7")
    private String visa;

    @NotBlank(message = "급여 정보를 입력해주세요.")
    @Schema(example = "₩12,000/hour")
    private String salary;

    @NotBlank(message = "근무 장소를 입력해주세요.")
    @Schema(example = "INU Campus, Incheon")
    private String location;

    @NotBlank(message = "근무 일정을 입력해주세요.")
    @Schema(example = "Weekends")
    private String schedule;

    @Schema(example = "Entry level")
    private String experience;

    @Schema(example = "English & Korean")
    private String language;

    @NotBlank(message = "채용 설명을 입력해주세요.")
    @Schema(example = "Join our international student services team as a campus tour guide...")
    private String description;

    @NotBlank(message = "담당 업무를 입력해주세요.")
    @Schema(example = "Lead campus tours for prospective students and visitors...")
    private String responsibilities;

    @NotBlank(message = "지원 자격을 입력해주세요.")
    @Schema(example = "Current INU student (undergraduate or graduate)...")
    private String requirements;

    @Schema(example = "International Affairs Office")
    private String contactPerson;

    @Email(message = "올바른 이메일 형식을 입력해주세요.")
    @Schema(example = "international@inu.ac.kr")
    private String contactEmail;

    @Schema(example = "+82-32-835-8114")
    private String contactPhone;
}