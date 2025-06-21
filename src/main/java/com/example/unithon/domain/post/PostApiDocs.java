package com.example.unithon.domain.post;

import com.example.unithon.domain.post.dto.req.PostUpdateReqDto;
import com.example.unithon.domain.post.dto.req.PostUploadReqDto;
import com.example.unithon.domain.post.dto.res.PostGetResDto;
import com.example.unithon.domain.post.dto.res.PostUploadResDto;
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

@Tag(name = "posts", description = "게시글 API")
@RequestMapping("/api/posts")
public interface PostApiDocs {

    @PostMapping
    @Operation(summary = "게시글 업로드")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "게시글 업로드 성공"),
            @ApiResponse(responseCode = "400", description = "게시글 업로드 실패",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = {
                                    @ExampleObject(
                                            name = "유효성 검사 실패",
                                            value = "{\"error\" : \"400\", \"message\" : \"유효성 검사에 실패했습니다\"}"
                                    ),
                                    @ExampleObject(
                                            name = "잘못된 카테고리",
                                            value = "{\"error\" : \"400\", \"message\" : \"유효하지 않은 카테고리입니다. HOUSING, JOBS, STUDY, SOCIAL, HELP 중 하나를 선택해주세요\"}"
                                    )
                            },
                            schema = @Schema(implementation = ErrorResponseEntity.class)))
    })
    ResponseEntity<PostUploadResDto> uploadPost(@AuthenticationPrincipal UserDetails user, @Valid @RequestBody PostUploadReqDto uploadRequest);

    @GetMapping("/{postId}")
    @Operation(summary = "개별 게시글 조회",
            security = @SecurityRequirement(name = ""))
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "게시글 조회 성공"),
            @ApiResponse(responseCode = "404", description = "게시글 조회 실패",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = {
                                    @ExampleObject(
                                            name = "존재하지 않는 게시글",
                                            value = "{\"error\" : \"404\", \"message\" : \"존재하지 않는 게시글입니다\"}"
                                    )
                            },
                            schema = @Schema(implementation = ErrorResponseEntity.class)))
    })
    ResponseEntity<PostGetResDto> getPost(@PathVariable Long postId);

    @GetMapping
    @Operation(summary = "전체 게시글 조회",
            description = "다양한 조건으로 게시글을 조회합니다. 파라미터는 모두 선택사항이며, 우선순위: memberId > keyword > category > sort",
            security = @SecurityRequirement(name = ""))
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "전체 게시글 조회 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 파라미터",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = {
                                    @ExampleObject(
                                            name = "잘못된 카테고리",
                                            value = "{\"error\" : \"400\", \"message\" : \"유효하지 않은 카테고리입니다. HOUSING, JOBS, STUDY, SOCIAL, HELP 중 하나를 선택해주세요\"}"
                                    )
                            },
                            schema = @Schema(implementation = ErrorResponseEntity.class))),
            @ApiResponse(responseCode = "404", description = "회원 게시글 조회 실패",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = {
                                    @ExampleObject(
                                            name = "존재하지 않는 회원",
                                            value = "{\"error\" : \"404\", \"message\" : \"존재하지 않는 id입니다\"}"
                                    )
                            },
                            schema = @Schema(implementation = ErrorResponseEntity.class)))
    })
    @io.swagger.v3.oas.annotations.Parameters({
            @io.swagger.v3.oas.annotations.Parameter(
                    name = "sort",
                    description = "정렬 방식 (선택) - 'likes': 인기순 TOP 3, 'latest' 또는 미입력: 최신순",
                    example = "likes"
            ),
            @io.swagger.v3.oas.annotations.Parameter(
                    name = "category",
                    description = "카테고리 필터 (선택) - HOUSING, JOBS, STUDY, SOCIAL, HELP",
                    example = "HOUSING"
            ),
            @io.swagger.v3.oas.annotations.Parameter(
                    name = "keyword",
                    description = "검색어 (선택) - 제목과 내용에서 검색",
                    example = "part time job"
            ),
            @io.swagger.v3.oas.annotations.Parameter(
                    name = "memberId",
                    description = "특정 회원 ID (선택) - 해당 회원이 작성한 게시글만 조회",
                    example = "1"
            )
    })
    ResponseEntity<List<PostGetResDto>> getPostList(@RequestParam(required = false) String sort,      // 정렬: likes(인기순), latest(최신순)
                                                    @RequestParam(required = false) Category category, // 카테고리 필터
                                                    @RequestParam(required = false) String keyword,    // 검색어
                                                    @RequestParam(required = false) Long memberId);    // 특정 회원 게시글

    @PutMapping("/{postId}")
    @Operation(summary = "게시글 수정",
            description = "본인이 작성한 게시글을 수정합니다. 로그인이 필요하며, 작성자만 수정 가능합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "게시글 수정 성공"),
            @ApiResponse(responseCode = "400", description = "게시글 수정 실패",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = {
                                    @ExampleObject(
                                            name = "유효성 검사 실패",
                                            value = "{\"error\" : \"400\", \"message\" : \"유효성 검사에 실패했습니다\"}"
                                    ),
                                    @ExampleObject(
                                            name = "잘못된 카테고리",
                                            value = "{\"error\" : \"400\", \"message\" : \"유효하지 않은 카테고리입니다. HOUSING, JOBS, STUDY, SOCIAL, HELP 중 하나를 선택해주세요\"}"
                                    )
                            },
                            schema = @Schema(implementation = ErrorResponseEntity.class))),
            @ApiResponse(responseCode = "403", description = "게시글 수정 실패",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = {
                                    @ExampleObject(
                                            name = "권한 없음",
                                            value = "{\"error\" : \"403\", \"message\" : \"권한이 없습니다\"}"
                                    )
                            },
                            schema = @Schema(implementation = ErrorResponseEntity.class))),
            @ApiResponse(responseCode = "404", description = "게시글 수정 실패",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = {
                                    @ExampleObject(
                                            name = "존재하지 않는 게시글",
                                            value = "{\"error\" : \"404\", \"message\" : \"존재하지 않는 게시글입니다\"}"
                                    )
                            },
                            schema = @Schema(implementation = ErrorResponseEntity.class)))
    })
    @io.swagger.v3.oas.annotations.Parameter(
            name = "postId",
            description = "수정할 게시글의 ID",
            example = "1",
            required = true
    )
    ResponseEntity<Void> updatePost(@AuthenticationPrincipal UserDetails user, @PathVariable Long postId, @Valid @RequestBody PostUpdateReqDto updateRequest);

    @DeleteMapping("/{postId}")
    @Operation(summary = "게시글 삭제",
            description = "본인이 작성한 게시글을 삭제합니다. 로그인이 필요하며, 작성자만 삭제 가능합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "게시글 삭제 성공"),
            @ApiResponse(responseCode = "403", description = "게시글 삭제 실패",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = {
                                    @ExampleObject(
                                            name = "권한 없음",
                                            value = "{\"error\" : \"403\", \"message\" : \"권한이 없습니다\"}"
                                    )
                            },
                            schema = @Schema(implementation = ErrorResponseEntity.class))),
            @ApiResponse(responseCode = "404", description = "게시글 삭제 실패",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = {
                                    @ExampleObject(
                                            name = "존재하지 않는 게시글",
                                            value = "{\"error\" : \"404\", \"message\" : \"존재하지 않는 게시글입니다\"}"
                                    )
                            },
                            schema = @Schema(implementation = ErrorResponseEntity.class)))
    })
    @io.swagger.v3.oas.annotations.Parameter(
            name = "postId",
            description = "삭제할 게시글의 ID",
            example = "1",
            required = true
    )
    ResponseEntity<Void> deletePost(@AuthenticationPrincipal UserDetails user, @PathVariable Long postId);
}