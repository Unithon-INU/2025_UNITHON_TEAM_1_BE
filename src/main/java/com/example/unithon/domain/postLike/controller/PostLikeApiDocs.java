package com.example.unithon.domain.postLike.controller;

import com.example.unithon.domain.postLike.dto.PostLikeResDto;
import com.example.unithon.global.exception.ErrorResponseEntity;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Tag(name = "post-likes", description = "게시글 좋아요 API")
@RequestMapping("/api/posts/{postId}/likes")
public interface PostLikeApiDocs {

    @PostMapping
    @Operation(summary = "게시글 좋아요/좋아요 취소",
            description = "게시글에 좋아요를 추가하거나 취소합니다. 토글 방식으로 동작합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "좋아요 처리 성공"),
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
                                    )
                            },
                            schema = @Schema(implementation = ErrorResponseEntity.class)))
    })
    @io.swagger.v3.oas.annotations.Parameter(
            name = "postId",
            description = "좋아요를 처리할 게시글의 ID",
            required = true
    )
    ResponseEntity<PostLikeResDto> toggleLike(@AuthenticationPrincipal UserDetails user,
                                              @PathVariable Long postId);

    @GetMapping
    @Operation(summary = "게시글 좋아요 상태 조회",
            description = "현재 사용자의 해당 게시글 좋아요 상태와 총 좋아요 수를 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "좋아요 상태 조회 성공"),
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
                                    )
                            },
                            schema = @Schema(implementation = ErrorResponseEntity.class)))
    })
    @io.swagger.v3.oas.annotations.Parameter(
            name = "postId",
            description = "좋아요 상태를 조회할 게시글의 ID",
            required = true
    )
    ResponseEntity<PostLikeResDto> getLikeStatus(@AuthenticationPrincipal UserDetails user,
                                                 @PathVariable Long postId);
}