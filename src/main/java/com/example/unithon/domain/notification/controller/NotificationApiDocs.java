package com.example.unithon.domain.notification.controller;

import com.example.unithon.domain.notification.dto.res.NotificationResDto;
import com.example.unithon.domain.notification.dto.res.NotificationSummaryResDto;
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
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "notifications", description = "알림 API")
@RequestMapping("/api/notifications")
public interface NotificationApiDocs {

    @GetMapping
    @Operation(summary = "알림 목록 조회",
            description = "사용자의 모든 알림을 최신순으로 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "알림 조회 성공"),
            @ApiResponse(responseCode = "401", description = "인증 실패",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = {
                                    @ExampleObject(
                                            name = "인증 실패",
                                            value = "{\"status\" : 401, \"code\" : \"USER-002\", \"message\" : \"email 혹은 비밀번호가 일치하지 않습니다\"}"
                                    )
                            },
                            schema = @Schema(implementation = ErrorResponseEntity.class)))
    })
    ResponseEntity<List<NotificationResDto>> getNotifications(@AuthenticationPrincipal UserDetails user);

    @GetMapping("/summary")
    @Operation(summary = "알림 요약 정보 조회",
            description = "읽지 않은 알림 개수와 여부를 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "알림 요약 조회 성공"),
            @ApiResponse(responseCode = "401", description = "인증 실패",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = {
                                    @ExampleObject(
                                            name = "인증 실패",
                                            value = "{\"status\" : 401, \"code\" : \"USER-002\", \"message\" : \"email 혹은 비밀번호가 일치하지 않습니다\"}"
                                    )
                            },
                            schema = @Schema(implementation = ErrorResponseEntity.class)))
    })
    ResponseEntity<NotificationSummaryResDto> getNotificationSummary(@AuthenticationPrincipal UserDetails user);

    @PutMapping("/{notificationId}/read")
    @Operation(summary = "특정 알림 읽음 처리",
            description = "특정 알림을 읽음으로 표시합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "알림 읽음 처리 성공"),
            @ApiResponse(responseCode = "403", description = "권한 없음",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = {
                                    @ExampleObject(
                                            name = "권한 없음",
                                            value = "{\"status\" : 403, \"code\" : \"USER-004\", \"message\" : \"권한이 없습니다\"}"
                                    )
                            },
                            schema = @Schema(implementation = ErrorResponseEntity.class))),
            @ApiResponse(responseCode = "404", description = "알림 조회 실패",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = {
                                    @ExampleObject(
                                            name = "존재하지 않는 알림",
                                            value = "{\"status\" : 404, \"code\" : \"NOTIFICATION-001\", \"message\" : \"존재하지 않는 알림입니다\"}"
                                    )
                            },
                            schema = @Schema(implementation = ErrorResponseEntity.class)))
    })
    @io.swagger.v3.oas.annotations.Parameter(
            name = "notificationId",
            description = "읽음 처리할 알림의 ID",
            required = true
    )
    ResponseEntity<Void> markAsRead(@AuthenticationPrincipal UserDetails user,
                                    @PathVariable Long notificationId);

    @PutMapping("/read-all")
    @Operation(summary = "모든 알림 읽음 처리",
            description = "사용자의 모든 읽지 않은 알림을 읽음으로 표시합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "모든 알림 읽음 처리 성공"),
            @ApiResponse(responseCode = "401", description = "인증 실패",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = {
                                    @ExampleObject(
                                            name = "인증 실패",
                                            value = "{\"status\" : 401, \"code\" : \"USER-002\", \"message\" : \"email 혹은 비밀번호가 일치하지 않습니다\"}"
                                    )
                            },
                            schema = @Schema(implementation = ErrorResponseEntity.class)))
    })
    ResponseEntity<Void> markAllAsRead(@AuthenticationPrincipal UserDetails user);
}