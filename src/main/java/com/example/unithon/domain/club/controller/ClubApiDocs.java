package com.example.unithon.domain.club.controller;

import com.example.unithon.domain.club.enums.Division;
import com.example.unithon.domain.club.dto.req.ClubCreateReqDto;
import com.example.unithon.domain.club.dto.req.ClubUpdateReqDto;
import com.example.unithon.domain.club.dto.res.ClubCreateResDto;
import com.example.unithon.domain.club.dto.res.ClubGetResDto;
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

@Tag(name = "clubs", description = "동아리 API")
@RequestMapping("/api/clubs")
public interface ClubApiDocs {

    @PostMapping
    @Operation(summary = "동아리 등록", description = "ADMIN 권한만 동아리를 등록할 수 있습니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "동아리 등록 성공"),
            @ApiResponse(responseCode = "400", description = "동아리 등록 실패",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = {
                                    @ExampleObject(
                                            name = "유효성 검사 실패",
                                            value = "{\"status\" : 400, \"code\" : \"VALIDATION\", \"message\" : \"유효성 검사에 실패했습니다\"}"
                                    )
                            },
                            schema = @Schema(implementation = ErrorResponseEntity.class))),
            @ApiResponse(responseCode = "403", description = "권한 없음",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = {
                                    @ExampleObject(
                                            name = "권한 없음",
                                            value = "{\"status\" : 403, \"code\" : \"USER-004\", \"message\" : \"권한이 없습니다\"}"
                                    )
                            },
                            schema = @Schema(implementation = ErrorResponseEntity.class))),
            @ApiResponse(responseCode = "409", description = "동아리명 중복",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = {
                                    @ExampleObject(
                                            name = "동아리명 중복",
                                            value = "{\"status\" : 409, \"code\" : \"CLUB-001\", \"message\" : \"이미 존재하는 동아리입니다\"}"
                                    )
                            },
                            schema = @Schema(implementation = ErrorResponseEntity.class)))
    })
    ResponseEntity<ClubCreateResDto> createClub(@AuthenticationPrincipal UserDetails user,
                                                @Valid @RequestBody ClubCreateReqDto createRequest);

    @GetMapping("/{clubId}")
    @Operation(summary = "개별 동아리 조회",
            description = "모든 사용자가 동아리 정보를 조회할 수 있습니다.",
            security = @SecurityRequirement(name = ""))
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "동아리 조회 성공"),
            @ApiResponse(responseCode = "404", description = "동아리 조회 실패",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = {
                                    @ExampleObject(
                                            name = "존재하지 않는 동아리",
                                            value = "{\"status\" : 404, \"code\" : \"CLUB-002\", \"message\" : \"존재하지 않는 동아리입니다\"}"
                                    )
                            },
                            schema = @Schema(implementation = ErrorResponseEntity.class)))
    })
    @io.swagger.v3.oas.annotations.Parameter(
            name = "clubId",
            description = "조회할 동아리의 ID",
            required = true
    )
    ResponseEntity<ClubGetResDto> getClub(@PathVariable Long clubId);

    @GetMapping
    @Operation(summary = "동아리 목록 조회",
            description = "분야별 필터링을 지원합니다. 모든 결과는 최신순으로 정렬됩니다.",
            security = @SecurityRequirement(name = ""))
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "동아리 조회 성공")
    })
    @io.swagger.v3.oas.annotations.Parameter(
            name = "division",
            description = "분야 필터 (선택) - CULTURE, SPORTS, ACADEMIC, HOBBY_EXHIBITION, VOLUNTEER, RELIGIOUS",
            example = "CULTURE"
    )
    ResponseEntity<List<ClubGetResDto>> getClubList(@RequestParam(required = false) Division division);

    @PutMapping("/{clubId}")
    @Operation(summary = "동아리 수정", description = "ADMIN 권한만 동아리를 수정할 수 있습니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "동아리 수정 성공"),
            @ApiResponse(responseCode = "400", description = "동아리 수정 실패",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = {
                                    @ExampleObject(
                                            name = "유효성 검사 실패",
                                            value = "{\"status\" : 400, \"code\" : \"VALIDATION\", \"message\" : \"유효성 검사에 실패했습니다\"}"
                                    )
                            },
                            schema = @Schema(implementation = ErrorResponseEntity.class))),
            @ApiResponse(responseCode = "403", description = "권한 없음",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = {
                                    @ExampleObject(
                                            name = "권한 없음",
                                            value = "{\"status\" : 403, \"code\" : \"USER-004\", \"message\" : \"권한이 없습니다\"}"
                                    )
                            },
                            schema = @Schema(implementation = ErrorResponseEntity.class))),
            @ApiResponse(responseCode = "404", description = "동아리 조회 실패",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = {
                                    @ExampleObject(
                                            name = "존재하지 않는 동아리",
                                            value = "{\"status\" : 404, \"code\" : \"CLUB-002\", \"message\" : \"존재하지 않는 동아리입니다\"}"
                                    )
                            },
                            schema = @Schema(implementation = ErrorResponseEntity.class))),
            @ApiResponse(responseCode = "409", description = "동아리명 중복",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = {
                                    @ExampleObject(
                                            name = "동아리명 중복",
                                            value = "{\"status\" : 409, \"code\" : \"CLUB-001\", \"message\" : \"이미 존재하는 동아리입니다\"}"
                                    )
                            },
                            schema = @Schema(implementation = ErrorResponseEntity.class)))
    })
    @io.swagger.v3.oas.annotations.Parameters({
            @io.swagger.v3.oas.annotations.Parameter(
                    name = "clubId",
                    description = "수정할 동아리의 ID",
                    required = true
            )
    })
    ResponseEntity<Void> updateClub(@AuthenticationPrincipal UserDetails user,
                                    @PathVariable Long clubId,
                                    @Valid @RequestBody ClubUpdateReqDto updateRequest);

    @DeleteMapping("/{clubId}")
    @Operation(summary = "동아리 삭제", description = "ADMIN 권한만 동아리를 삭제할 수 있습니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "동아리 삭제 성공"),
            @ApiResponse(responseCode = "403", description = "권한 없음",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = {
                                    @ExampleObject(
                                            name = "권한 없음",
                                            value = "{\"status\" : 403, \"code\" : \"USER-004\", \"message\" : \"권한이 없습니다\"}"
                                    )
                            },
                            schema = @Schema(implementation = ErrorResponseEntity.class))),
            @ApiResponse(responseCode = "404", description = "동아리 조회 실패",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = {
                                    @ExampleObject(
                                            name = "존재하지 않는 동아리",
                                            value = "{\"status\" : 404, \"code\" : \"CLUB-002\", \"message\" : \"존재하지 않는 동아리입니다\"}"
                                    )
                            },
                            schema = @Schema(implementation = ErrorResponseEntity.class)))
    })
    @io.swagger.v3.oas.annotations.Parameter(
            name = "clubId",
            description = "삭제할 동아리의 ID",
            required = true
    )
    ResponseEntity<Void> deleteClub(@AuthenticationPrincipal UserDetails user,
                                    @PathVariable Long clubId);
}