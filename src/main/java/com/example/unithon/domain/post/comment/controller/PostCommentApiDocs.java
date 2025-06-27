package com.example.unithon.domain.post.comment.controller;

import com.example.unithon.domain.post.comment.dto.req.PostCommentCreateReqDto;
import com.example.unithon.domain.post.comment.dto.req.PostCommentUpdateReqDto;
import com.example.unithon.domain.post.comment.dto.res.PostCommentCreateResDto;
import com.example.unithon.domain.post.comment.dto.res.PostCommentResDto;
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

@Tag(name = "post-comments", description = "게시글 댓글 API")
@RequestMapping("/api/posts/{postId}/comments")
public interface PostCommentApiDocs {

    @PostMapping
    @Operation(summary = "댓글 생성",
            description = "게시글에 댓글을 작성합니다. parentId가 0이면 댓글, 그 이상이면 답댓글로 등록됩니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "댓글 생성 성공"),
            @ApiResponse(responseCode = "400", description = "댓글 생성 실패",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = {
                                    @ExampleObject(
                                            name = "유효성 검사 실패",
                                            value = "{\"status\" : 400, \"code\" : \"VALIDATION\", \"message\" : \"댓글 내용을 입력해주세요\"}"
                                    ),
                                    @ExampleObject(
                                            name = "잘못된 댓글 깊이",
                                            value = "{\"status\" : 400, \"code\" : \"COMMENT-003\", \"message\" : \"답댓글의 답댓글은 작성할 수 없습니다\"}"
                                    ),
                                    @ExampleObject(
                                            name = "잘못된 부모 댓글",
                                            value = "{\"status\" : 400, \"code\" : \"COMMENT-004\", \"message\" : \"유효하지 않은 부모 댓글입니다\"}"
                                    )
                            },
                            schema = @Schema(implementation = ErrorResponseEntity.class))),
            @ApiResponse(responseCode = "401", description = "인증 실패",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = {
                                    @ExampleObject(
                                            name = "인증 실패",
                                            value = "{\"status\" : 401, \"code\" : \"USER-002\", \"message\" : \"email 혹은 비밀번호가 일치하지 않습니다\"}"
                                    )
                            },
                            schema = @Schema(implementation = ErrorResponseEntity.class))),
            @ApiResponse(responseCode = "404", description = "게시글을 찾을 수 없음",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = {
                                    @ExampleObject(
                                            name = "존재하지 않는 게시글",
                                            value = "{\"status\" : 404, \"code\" : \"POST-001\", \"message\" : \"존재하지 않는 post입니다\"}"
                                    ),
                                    @ExampleObject(
                                            name = "존재하지 않는 부모 댓글",
                                            value = "{\"status\" : 404, \"code\" : \"COMMENT-001\", \"message\" : \"존재하지 않는 댓글입니다\"}"
                                    )
                            },
                            schema = @Schema(implementation = ErrorResponseEntity.class)))
    })
    @io.swagger.v3.oas.annotations.Parameter(
            name = "postId",
            description = "댓글을 작성할 게시글의 ID",
            required = true
    )
    ResponseEntity<PostCommentCreateResDto> createComment(@AuthenticationPrincipal UserDetails user,
                                                          @PathVariable Long postId,
                                                          @Valid @RequestBody PostCommentCreateReqDto createRequest);

    @GetMapping
    @Operation(summary = "게시글 댓글 목록 조회",
            description = "게시글의 모든 댓글을 계층구조로 조회합니다. 최상위 댓글과 답댓글이 함께 반환됩니다.",
            security = @SecurityRequirement(name = ""))
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "댓글 목록 조회 성공"),
            @ApiResponse(responseCode = "404", description = "게시글을 찾을 수 없음",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = {
                                    @ExampleObject(
                                            name = "존재하지 않는 게시글",
                                            value = "{\"status\" : 404, \"code\" : \"POST-001\", \"message\" : \"존재하지 않는 post입니다\"}"
                                    )
                            },
                            schema = @Schema(implementation = ErrorResponseEntity.class)))
    })
    @io.swagger.v3.oas.annotations.Parameter(
            name = "postId",
            description = "댓글을 조회할 게시글의 ID",
            required = true
    )
    ResponseEntity<List<PostCommentResDto>> getCommentsByPost(@PathVariable Long postId);

    @GetMapping("/{commentId}")
    @Operation(summary = "개별 댓글 조회",
            description = "특정 댓글의 상세 정보를 조회합니다.",
            security = @SecurityRequirement(name = ""))
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "댓글 조회 성공"),
            @ApiResponse(responseCode = "404", description = "댓글을 찾을 수 없음",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = {
                                    @ExampleObject(
                                            name = "존재하지 않는 댓글",
                                            value = "{\"status\" : 404, \"code\" : \"COMMENT-001\", \"message\" : \"존재하지 않는 댓글입니다\"}"
                                    )
                            },
                            schema = @Schema(implementation = ErrorResponseEntity.class)))
    })
    @io.swagger.v3.oas.annotations.Parameters({
            @io.swagger.v3.oas.annotations.Parameter(
                    name = "postId",
                    description = "게시글의 ID",
                    required = true
            ),
            @io.swagger.v3.oas.annotations.Parameter(
                    name = "commentId",
                    description = "조회할 댓글의 ID",
                    required = true
            )
    })
    ResponseEntity<PostCommentResDto> getComment(@PathVariable Long postId,
                                                 @PathVariable Long commentId);

    @PutMapping("/{commentId}")
    @Operation(summary = "댓글 수정",
            description = "본인이 작성한 댓글을 수정합니다. 로그인이 필요하며, 작성자만 수정 가능합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "댓글 수정 성공"),
            @ApiResponse(responseCode = "400", description = "댓글 수정 실패",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = {
                                    @ExampleObject(
                                            name = "유효성 검사 실패",
                                            value = "{\"status\" : 400, \"code\" : \"VALIDATION\", \"message\" : \"댓글 내용을 입력해주세요\"}"
                                    )
                            },
                            schema = @Schema(implementation = ErrorResponseEntity.class))),
            @ApiResponse(responseCode = "403", description = "댓글 수정 실패",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = {
                                    @ExampleObject(
                                            name = "권한 없음",
                                            value = "{\"status\" : 403, \"code\" : \"USER-004\", \"message\" : \"권한이 없습니다\"}"
                                    )
                            },
                            schema = @Schema(implementation = ErrorResponseEntity.class))),
            @ApiResponse(responseCode = "404", description = "댓글을 찾을 수 없음",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = {
                                    @ExampleObject(
                                            name = "존재하지 않는 댓글",
                                            value = "{\"status\" : 404, \"code\" : \"COMMENT-001\", \"message\" : \"존재하지 않는 댓글입니다\"}"
                                    )
                            },
                            schema = @Schema(implementation = ErrorResponseEntity.class)))
    })
    @io.swagger.v3.oas.annotations.Parameters({
            @io.swagger.v3.oas.annotations.Parameter(
                    name = "postId",
                    description = "게시글의 ID",
                    required = true
            ),
            @io.swagger.v3.oas.annotations.Parameter(
                    name = "commentId",
                    description = "수정할 댓글의 ID",
                    required = true
            )
    })
    ResponseEntity<Void> updateComment(@AuthenticationPrincipal UserDetails user,
                                       @PathVariable Long postId,
                                       @PathVariable Long commentId,
                                       @Valid @RequestBody PostCommentUpdateReqDto updateRequest);

    @DeleteMapping("/{commentId}")
    @Operation(summary = "댓글 삭제",
            description = "본인이 작성한 댓글을 삭제합니다. 최상위 댓글 삭제 시 모든 답댓글도 함께 삭제됩니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "댓글 삭제 성공"),
            @ApiResponse(responseCode = "403", description = "댓글 삭제 실패",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = {
                                    @ExampleObject(
                                            name = "권한 없음",
                                            value = "{\"status\" : 403, \"code\" : \"USER-004\", \"message\" : \"권한이 없습니다\"}"
                                    )
                            },
                            schema = @Schema(implementation = ErrorResponseEntity.class))),
            @ApiResponse(responseCode = "404", description = "댓글을 찾을 수 없음",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = {
                                    @ExampleObject(
                                            name = "존재하지 않는 댓글",
                                            value = "{\"status\" : 404, \"code\" : \"COMMENT-001\", \"message\" : \"존재하지 않는 댓글입니다\"}"
                                    )
                            },
                            schema = @Schema(implementation = ErrorResponseEntity.class)))
    })
    @io.swagger.v3.oas.annotations.Parameters({
            @io.swagger.v3.oas.annotations.Parameter(
                    name = "postId",
                    description = "게시글의 ID",
                    required = true
            ),
            @io.swagger.v3.oas.annotations.Parameter(
                    name = "commentId",
                    description = "삭제할 댓글의 ID",
                    required = true
            )
    })
    ResponseEntity<Void> deleteComment(@AuthenticationPrincipal UserDetails user,
                                       @PathVariable Long postId,
                                       @PathVariable Long commentId);
}