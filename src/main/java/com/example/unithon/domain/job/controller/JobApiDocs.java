package com.example.unithon.domain.job.controller;

import com.example.unithon.domain.job.JobField;
import com.example.unithon.domain.job.dto.req.JobCreateReqDto;
import com.example.unithon.domain.job.dto.req.JobUpdateReqDto;
import com.example.unithon.domain.job.dto.res.JobCreateResDto;
import com.example.unithon.domain.job.dto.res.JobGetResDto;
import com.example.unithon.global.exception.ErrorResponseEntity;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "jobs", description = "채용 공고 API")
@RequestMapping("/api/jobs")
public interface JobApiDocs {

    @PostMapping
    @Operation(summary = "채용 공고 등록", description = "ADMIN 권한만 채용 공고를 등록할 수 있습니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "채용 공고 등록 성공"),
            @ApiResponse(responseCode = "400", description = "유효성 검사 실패",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponseEntity.class))),
            @ApiResponse(responseCode = "403", description = "권한 없음",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(value = "{\"status\":403,\"code\":\"USER-004\",\"message\":\"권한이 없습니다\"}"),
                            schema = @Schema(implementation = ErrorResponseEntity.class)))
    })
    ResponseEntity<JobCreateResDto> createJob(@AuthenticationPrincipal UserDetails user,
                                              @Valid @RequestBody JobCreateReqDto createRequest);

    @GetMapping("/{jobId}")
    @Operation(summary = "개별 채용 공고 조회", security = @SecurityRequirement(name = ""))
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "채용 공고 조회 성공"),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 채용 공고",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(value = "{\"status\":404,\"code\":\"JOB-001\",\"message\":\"존재하지 않는 채용 공고입니다\"}"),
                            schema = @Schema(implementation = ErrorResponseEntity.class)))
    })
    ResponseEntity<JobGetResDto> getJob(@PathVariable Long jobId);

    @GetMapping
    @Operation(summary = "채용 공고 목록 조회",
            description = "분야별 필터링과 검색을 지원합니다. 모든 결과는 최신순으로 정렬됩니다.",
            security = @SecurityRequirement(name = ""))
    @ApiResponse(responseCode = "200", description = "채용 공고 조회 성공")
    ResponseEntity<List<JobGetResDto>> getJobList(@RequestParam(required = false) JobField jobField,
                                                  @RequestParam(required = false) String searchType,
                                                  @RequestParam(required = false) String keyword);

    @PutMapping("/{jobId}")
    @Operation(summary = "채용 공고 수정", description = "ADMIN 권한만 채용 공고를 수정할 수 있습니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "채용 공고 수정 성공"),
            @ApiResponse(responseCode = "400", description = "유효성 검사 실패",
                    content = @Content(schema = @Schema(implementation = ErrorResponseEntity.class))),
            @ApiResponse(responseCode = "403", description = "권한 없음",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(value = "{\"status\":403,\"code\":\"USER-004\",\"message\":\"권한이 없습니다\"}"),
                            schema = @Schema(implementation = ErrorResponseEntity.class))),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 채용 공고",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(value = "{\"status\":404,\"code\":\"JOB-001\",\"message\":\"존재하지 않는 채용 공고입니다\"}"),
                            schema = @Schema(implementation = ErrorResponseEntity.class)))
    })
    ResponseEntity<Void> updateJob(@AuthenticationPrincipal UserDetails user,
                                   @PathVariable Long jobId,
                                   @Valid @RequestBody JobUpdateReqDto updateRequest);

    @DeleteMapping("/{jobId}")
    @Operation(summary = "채용 공고 삭제", description = "ADMIN 권한만 채용 공고를 삭제할 수 있습니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "채용 공고 삭제 성공"),
            @ApiResponse(responseCode = "403", description = "권한 없음",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(value = "{\"status\":403,\"code\":\"USER-004\",\"message\":\"권한이 없습니다\"}"),
                            schema = @Schema(implementation = ErrorResponseEntity.class))),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 채용 공고",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(value = "{\"status\":404,\"code\":\"JOB-001\",\"message\":\"존재하지 않는 채용 공고입니다\"}"),
                            schema = @Schema(implementation = ErrorResponseEntity.class)))
    })
    ResponseEntity<Void> deleteJob(@AuthenticationPrincipal UserDetails user,
                                   @PathVariable Long jobId);
}