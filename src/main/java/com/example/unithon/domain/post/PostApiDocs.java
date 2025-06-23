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

    @GetMapping("/top")
    @Operation(summary = "인기 게시글 TOP 3 조회",
            description = "좋아요 수가 많은 게시글 상위 3개를 조회합니다. 좋아요 수가 같으면 최신순으로 정렬됩니다.",
            security = @SecurityRequirement(name = ""))
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "인기 게시글 조회 성공")
    })
    ResponseEntity<List<PostGetResDto>> getTopPostsByLikes();

    @GetMapping
    @Operation(summary = "게시글 조회 및 검색",
            description = "키워드 검색과 카테고리 필터링을 지원합니다. 모든 결과는 최신순으로 정렬됩니다.",
            security = @SecurityRequirement(name = ""))
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "게시글 조회 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 파라미터",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = {
                                    @ExampleObject(
                                            name = "잘못된 카테고리",
                                            value = "{\"error\" : \"400\", \"message\" : \"유효하지 않은 카테고리입니다. HOUSING, JOBS, STUDY, SOCIAL, HELP 중 하나를 선택해주세요\"}"
                                    )
                            },
                            schema = @Schema(implementation = ErrorResponseEntity.class)))
    })
    @io.swagger.v3.oas.annotations.Parameters({
            @io.swagger.v3.oas.annotations.Parameter(
                    name = "keyword",
                    description = "검색어 (선택) - 제목과 내용에서 검색"
            ),
            @io.swagger.v3.oas.annotations.Parameter(
                    name = "category",
                    description = "카테고리 필터 (선택) - HOUSING, JOBS, STUDY, SOCIAL, HELP"
            )
    })
    ResponseEntity<List<PostGetResDto>> getPostList(@RequestParam(required = false) String keyword,
                                                    @RequestParam(required = false) Category category);


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
            required = true
    )
    ResponseEntity<Void> deletePost(@AuthenticationPrincipal UserDetails user, @PathVariable Long postId);
}