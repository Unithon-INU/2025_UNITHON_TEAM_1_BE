package com.example.uniton.domain.member;

import com.example.uniton.domain.member.dto.req.LoginReqDto;
import com.example.uniton.domain.member.dto.req.SignupReqDto;
import com.example.uniton.domain.member.dto.res.LoginResDto;
import com.example.uniton.domain.member.dto.res.SignupResDto;
import com.example.uniton.global.exception.ErrorResponseEntity;
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
import org.springframework.web.bind.annotation.*;

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
                            value = "{\"error\" : \"409\", \"message\" : \"이미 존재하는 ID입니다\"}"

                    )
            },
            schema = @Schema(implementation = ErrorResponseEntity.class))),
    })
    ResponseEntity<SignupResDto> signup(@Valid @RequestBody SignupReqDto signupRequest);

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
            @ApiResponse(responseCode = "404", description = "존재하지 않는 ID",
                content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                examples = {
                        @ExampleObject(
                                name = "존재하지 않는 사용자",
                                value = "{\"error\" : \"404\", \"message\" : \"존재하지 않는 사용자ID 입니다.\"}"
                        )
                }))
    })
    ResponseEntity<LoginResDto> login(@Valid @RequestBody LoginReqDto loginRequest);
}

