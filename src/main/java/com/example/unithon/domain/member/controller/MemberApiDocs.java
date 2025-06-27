package com.example.unithon.domain.member.controller;

import com.example.unithon.domain.member.dto.req.*;
import com.example.unithon.domain.member.dto.res.*;
import com.example.unithon.global.exception.ErrorResponseEntity;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Tag(name = "members", description = "회원 API")
@RequestMapping("/api/members")
public interface MemberApiDocs {

    @PostMapping("/signup")
    @Operation(summary = "회원가입",
            security = @SecurityRequirement(name = "")) //인증X
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "회원가입 성공"),
            @ApiResponse(responseCode = "400", description = "회원가입 실패",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = {
                                    @ExampleObject(
                                            name = "유효성 검사 실패",
                                            value = "{\"error\" : \"400\", \"message\" : \"유효성 검사에 실패했습니다\"}"
                                    )
                            },
                            schema = @Schema(implementation = ErrorResponseEntity.class))),
            @ApiResponse(responseCode = "409", description = "회원가입 실패",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = {
                                    @ExampleObject(
                                            name = "ID 중복",
                                            value = "{\"error\" : \"409\", \"message\" : \"이미 존재하는 email입니다\"}"

                                    )
                            },
                            schema = @Schema(implementation = ErrorResponseEntity.class))),
    })
    ResponseEntity<MemberSignupResDto> signup(@Valid @RequestBody MemberSignupReqDto signupRequest);

    @PostMapping("/login")
    @Operation(summary = "로그인",
            security = @SecurityRequirement(name = "")) //인증X
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "로그인 성공"),
            @ApiResponse(responseCode = "401", description = "로그인 실패",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = {
                                    @ExampleObject(
                                            name = "비밀번호 불일치",
                                            value = "{\"error\" : \"401\", \"message\" : \"비밀번호가 일치하지 않습니다\"}"
                                    )
                            },
                            schema = @Schema(implementation = ErrorResponseEntity.class))),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 email",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = {
                                    @ExampleObject(
                                            name = "존재하지 않는 사용자",
                                            value = "{\"error\" : \"404\", \"message\" : \"존재하지 않는 email 입니다.\"}"
                                    )
                            }))
    })
    ResponseEntity<MemberLoginResDto> login(@Valid @RequestBody MemberLoginReqDto loginRequest);

    @PostMapping("/refresh")
    @Operation(summary = "토큰 갱신",
            description = "Refresh Token을 사용하여 새로운 Access Token과 Refresh Token을 발급받습니다.",
            security = @SecurityRequirement(name = ""))
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "토큰 갱신 성공"),
            @ApiResponse(responseCode = "400", description = "토큰 갱신 실패",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = {
                                    @ExampleObject(
                                            name = "유효성 검사 실패",
                                            value = "{\"status\" : 400, \"code\" : \"VALIDATION\", \"message\" : \"refresh token을 입력해주세요\"}"
                                    )
                            },
                            schema = @Schema(implementation = ErrorResponseEntity.class))),
            @ApiResponse(responseCode = "401", description = "토큰 갱신 실패",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = {
                                    @ExampleObject(
                                            name = "유효하지 않은 Refresh Token",
                                            value = "{\"status\" : 401, \"code\" : \"AUTH-001\", \"message\" : \"유효하지 않은 refresh token입니다\"}"
                                    )
                            },
                            schema = @Schema(implementation = ErrorResponseEntity.class))),
            @ApiResponse(responseCode = "404", description = "토큰 갱신 실패",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = {
                                    @ExampleObject(
                                            name = "사용자 정보 없음",
                                            value = "{\"status\" : 404, \"code\" : \"USER-003\", \"message\" : \"존재하지 않는 email입니다\"}"
                                    )
                            },
                            schema = @Schema(implementation = ErrorResponseEntity.class)))
    })
    ResponseEntity<MemberTokenRefreshResDto> refreshToken(@Valid @RequestBody MemberTokenRefreshReqDto refreshRequest);

    @GetMapping("/{memberId}")
    @Operation(summary = "개별 회원 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "회원 조회 성공"),
            @ApiResponse(responseCode = "404", description = "회원 조회 실패",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = {
                                    @ExampleObject(
                                            name = "존재하지 않는 회원",
                                            value = "{\"error\" : \"404\", \"message\" : \"존재하지 않는 회원입니다\"}"
                                    )
                            },
                            schema = @Schema(implementation = ErrorResponseEntity.class)))
    })
    ResponseEntity<MemberGetResDto> getMember(@PathVariable Long memberId);

    @GetMapping
    @Operation(summary = "전체 회원 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "전체 회원 조회 성공")
    })
    ResponseEntity<List<MemberGetResDto>> getMemberList();

    @GetMapping("/me")
    @Operation(summary = "마이페이지 조회",
            description = "본인의 상세 정보를 조회합니다. 로그인이 필요합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "마이페이지 조회 성공"),
            @ApiResponse(responseCode = "401", description = "마이페이지 조회 실패",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = {
                                    @ExampleObject(
                                            name = "인증 실패",
                                            value = "{\"error\" : \"401\", \"message\" : \"인증이 필요합니다\"}"
                                    )
                            },
                            schema = @Schema(implementation = ErrorResponseEntity.class))),
            @ApiResponse(responseCode = "404", description = "마이페이지 조회 실패",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = {
                                    @ExampleObject(
                                            name = "사용자 정보 없음",
                                            value = "{\"error\" : \"404\", \"message\" : \"존재하지 않는 id입니다\"}"
                                    )
                            },
                            schema = @Schema(implementation = ErrorResponseEntity.class)))
    })
    ResponseEntity<MemberMyPageResDto> getMyPage(@AuthenticationPrincipal UserDetails user);

    @PutMapping("/me")
    @Operation(summary = "회원 정보 수정",
            description = "본인의 정보를 수정합니다. 닉네임과 비밀번호 변경 모두 선택사항입니다. 변경하지 않을 항목은 빈 값으로 보내거나 생략할 수 있습니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "회원 정보 수정 성공"),
            @ApiResponse(responseCode = "400", description = "회원 정보 수정 실패",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = {
                                    @ExampleObject(
                                            name = "새 비밀번호 불일치",
                                            value = "{\"error\" : \"400\", \"message\" : \"비밀번호가 일치하지 않습니다\"}"
                                    ),
                                    @ExampleObject(
                                            name = "현재 비밀번호 미입력",
                                            value = "{\"error\" : \"400\", \"message\" : \"비밀번호가 일치하지 않습니다\"}"
                                    )
                            },
                            schema = @Schema(implementation = ErrorResponseEntity.class))),
            @ApiResponse(responseCode = "401", description = "인증 실패",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = {
                                    @ExampleObject(
                                            name = "현재 비밀번호 불일치",
                                            value = "{\"error\" : \"401\", \"message\" : \"email 혹은 비밀번호가 일치하지 않습니다\"}"
                                    )
                            },
                            schema = @Schema(implementation = ErrorResponseEntity.class))),
            @ApiResponse(responseCode = "404", description = "사용자 정보 없음",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = {
                                    @ExampleObject(
                                            name = "사용자 정보 없음",
                                            value = "{\"error\" : \"404\", \"message\" : \"존재하지 않는 id입니다\"}"
                                    )
                            },
                            schema = @Schema(implementation = ErrorResponseEntity.class)))
    })
    ResponseEntity<MemberUpdateResDto> updateMember(@AuthenticationPrincipal UserDetails user,
                                                    @Valid @RequestBody MemberUpdateReqDto updateRequest);

    @PostMapping(value = "/me/profile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "프로필 이미지 업로드",
            description = "프로필 이미지를 업로드하고 닉네임을 함께 수정할 수 있습니다. 새 이미지 업로드 시 기존 이미지는 자동으로 삭제됩니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "프로필 업데이트 성공"),
            @ApiResponse(responseCode = "400", description = "프로필 업데이트 실패",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = {
                                    @ExampleObject(
                                            name = "지원하지 않는 파일 형식",
                                            value = "{\"status\" : 400, \"code\" : \"IMAGE-001\", \"message\" : \"지원하지 않는 파일 형식입니다\"}"
                                    ),
                                    @ExampleObject(
                                            name = "파일 크기 초과",
                                            value = "{\"status\" : 400, \"code\" : \"IMAGE-002\", \"message\" : \"파일 크기는 5MB 이하여야 합니다\"}"
                                    )
                            },
                            schema = @Schema(implementation = ErrorResponseEntity.class))),
            @ApiResponse(responseCode = "401", description = "인증 실패",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = {
                                    @ExampleObject(
                                            name = "인증 실패",
                                            value = "{\"error\" : \"401\", \"message\" : \"인증이 필요합니다\"}"
                                    )
                            },
                            schema = @Schema(implementation = ErrorResponseEntity.class))),
            @ApiResponse(responseCode = "500", description = "서버 오류",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = {
                                    @ExampleObject(
                                            name = "이미지 업로드 실패",
                                            value = "{\"status\" : 500, \"code\" : \"IMAGE-003\", \"message\" : \"이미지 업로드에 실패했습니다\"}"
                                    )
                            },
                            schema = @Schema(implementation = ErrorResponseEntity.class)))
    })
    ResponseEntity<MemberUpdateResDto> updateProfile(
            @AuthenticationPrincipal UserDetails user,
            @Parameter(description = "닉네임 (선택사항)")
            @RequestParam(value = "nickname", required = false) String nickname,
            @Parameter(description = "프로필 이미지 파일 (JPG, PNG, GIF, WEBP 지원, 최대 2MB)")
            @RequestParam(value = "profileImage", required = false) MultipartFile profileImage);

    @DeleteMapping("/me/profile-image")
    @Operation(summary = "프로필 이미지 삭제",
            description = "현재 설정된 프로필 이미지를 삭제합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "프로필 이미지 삭제 성공"),
            @ApiResponse(responseCode = "401", description = "인증 실패",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = {
                                    @ExampleObject(
                                            name = "인증 실패",
                                            value = "{\"error\" : \"401\", \"message\" : \"인증이 필요합니다\"}"
                                    )
                            },
                            schema = @Schema(implementation = ErrorResponseEntity.class))),
            @ApiResponse(responseCode = "404", description = "사용자 정보 없음",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = {
                                    @ExampleObject(
                                            name = "사용자 정보 없음",
                                            value = "{\"error\" : \"404\", \"message\" : \"존재하지 않는 id입니다\"}"
                                    )
                            },
                            schema = @Schema(implementation = ErrorResponseEntity.class)))
    })
    ResponseEntity<MemberUpdateResDto> deleteProfileImage(@AuthenticationPrincipal UserDetails user);
}