package com.example.unithon.domain.post;

import com.example.unithon.domain.post.dto.res.PostGetResDto;
import com.example.unithon.domain.post.dto.res.PostUploadResDto;
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
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Tag(name = "posts", description = "게시글 API")
@RequestMapping("/api/posts")
public interface PostApiDocs {

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "게시글 업로드",
            description = "텍스트 데이터와 이미지를 함께 업로드합니다. 이미지는 선택사항입니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "게시글 업로드 성공"),
            @ApiResponse(responseCode = "400", description = "게시글 업로드 실패",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = {
                                    @ExampleObject(
                                            name = "유효성 검사 실패",
                                            value = "{\"status\" : 400, \"code\" : \"VALIDATION-001\", \"message\" : \"입력값이 올바르지 않습니다\"}"
                                    ),
                                    @ExampleObject(
                                            name = "이미지 업로드 실패",
                                            value = "{\"status\" : 400, \"code\" : \"IMAGE-001\", \"message\" : \"지원하지 않는 파일 형식입니다\"}"
                                    ),
                                    @ExampleObject(
                                            name = "파일 크기 초과",
                                            value = "{\"status\" : 400, \"code\" : \"IMAGE-002\", \"message\" : \"파일 크기는 5MB 이하여야 합니다\"}"
                                    )
                            },
                            schema = @Schema(implementation = ErrorResponseEntity.class)))
    })
    ResponseEntity<PostUploadResDto> uploadPost(
            @AuthenticationPrincipal UserDetails user,
            @Parameter(description = "카테고리 (HOUSING, JOBS, STUDY, SOCIAL, HELP)", required = true)
            @RequestParam("category") Category category,
            @Parameter(description = "게시글 제목", required = true)
            @RequestParam("title") String title,
            @Parameter(description = "게시글 내용", required = true)
            @RequestParam("content") String content,
            @Parameter(description = "첨부 이미지 파일 (선택사항, 최대 5MB)")
            @RequestParam(value = "image", required = false) MultipartFile image);

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
                                            value = "{\"status\" : 404, \"code\" : \"POST-001\", \"message\" : \"존재하지 않는 post입니다\"}"
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
            @ApiResponse(responseCode = "200", description = "게시글 조회 성공")
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

    @PutMapping(value = "/{postId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "게시글 수정",
            description = "본인이 작성한 게시글을 수정합니다. 새로운 이미지를 업로드하면 기존 이미지가 교체됩니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "게시글 수정 성공"),
            @ApiResponse(responseCode = "400", description = "게시글 수정 실패",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = {
                                    @ExampleObject(
                                            name = "유효성 검사 실패",
                                            value = "{\"status\" : 400, \"code\" : \"VALIDATION-001\", \"message\" : \"입력값이 올바르지 않습니다\"}"
                                    )
                            },
                            schema = @Schema(implementation = ErrorResponseEntity.class))),
            @ApiResponse(responseCode = "403", description = "게시글 수정 실패",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = {
                                    @ExampleObject(
                                            name = "권한 없음",
                                            value = "{\"status\" : 403, \"code\" : \"USER-004\", \"message\" : \"권한이 없습니다\"}"
                                    )
                            },
                            schema = @Schema(implementation = ErrorResponseEntity.class))),
            @ApiResponse(responseCode = "404", description = "게시글 수정 실패",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = {
                                    @ExampleObject(
                                            name = "존재하지 않는 게시글",
                                            value = "{\"status\" : 404, \"code\" : \"POST-001\", \"message\" : \"존재하지 않는 post입니다\"}"
                                    )
                            },
                            schema = @Schema(implementation = ErrorResponseEntity.class)))
    })
    ResponseEntity<Void> updatePost(
            @AuthenticationPrincipal UserDetails user,
            @PathVariable Long postId,
            @Parameter(description = "카테고리 (HOUSING, JOBS, STUDY, SOCIAL, HELP)", required = true)
            @RequestParam("category") Category category,
            @Parameter(description = "게시글 제목", required = true)
            @RequestParam("title") String title,
            @Parameter(description = "게시글 내용", required = true)
            @RequestParam("content") String content,
            @Parameter(description = "새로운 이미지 파일 (선택사항)")
            @RequestParam(value = "image", required = false) MultipartFile image);

    @DeleteMapping("/{postId}")
    @Operation(summary = "게시글 삭제",
            description = "본인이 작성한 게시글을 삭제합니다. 첨부된 이미지도 함께 삭제됩니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "게시글 삭제 성공"),
            @ApiResponse(responseCode = "403", description = "게시글 삭제 실패",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = {
                                    @ExampleObject(
                                            name = "권한 없음",
                                            value = "{\"status\" : 403, \"code\" : \"USER-004\", \"message\" : \"권한이 없습니다\"}"
                                    )
                            },
                            schema = @Schema(implementation = ErrorResponseEntity.class))),
            @ApiResponse(responseCode = "404", description = "게시글 삭제 실패",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = {
                                    @ExampleObject(
                                            name = "존재하지 않는 게시글",
                                            value = "{\"status\" : 404, \"code\" : \"POST-001\", \"message\" : \"존재하지 않는 post입니다\"}"
                                    )
                            },
                            schema = @Schema(implementation = ErrorResponseEntity.class)))
    })
    ResponseEntity<Void> deletePost(@AuthenticationPrincipal UserDetails user, @PathVariable Long postId);
}