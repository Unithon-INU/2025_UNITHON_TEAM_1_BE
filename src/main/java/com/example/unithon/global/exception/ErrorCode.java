package com.example.unithon.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    DUPLICATE_EMAIL(CONFLICT, "USER-001", "이미 존재하는 email 입니다."),
    UNAUTHORIZED_LOGIN(UNAUTHORIZED, "USER-002", "email 혹은 비밀번호가 일치하지 않습니다."),
    EMAIL_NOT_FOUND(NOT_FOUND, "USER-003", "존재하지 않는 email입니다."),
    FORBIDDEN_PERMISSION(FORBIDDEN, "USER-004", "권한이 없습니다."),
    PASSWORD_MISMATCH(BAD_REQUEST, "USER-005", "비밀번호가 일치하지 않습니다."),
    ID_NOT_FOUND(NOT_FOUND, "USER-006", "존재하지 않는 id입니다."),

    POST_NOT_FOUND(NOT_FOUND, "POST-001", "존재하지 않는 post입니다."),

    CLUB_NOT_FOUND(NOT_FOUND, "CLUB-001", "존재하지 않는 club입니다."),
    DUPLICATE_CLUB(CONFLICT, "CLUB-002", "이미 존재하는 club 입니다."),

    INVALID_REFRESH_TOKEN(UNAUTHORIZED, "AUTH-001", "유효하지 않은 refresh token입니다."),
    EXPIRED_REFRESH_TOKEN(UNAUTHORIZED, "AUTH-002", "만료된 refresh token입니다.");

    private final HttpStatus httpStatus;
    private final String code;				// USER-001
    private final String message;			// 설명
}