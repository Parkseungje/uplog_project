package com.uplog_project.backend.api.global.exception.user;

import com.uplog_project.backend.api.global.exception.ErrorCode;
import org.springframework.http.HttpStatus;

public enum UserErrorCode implements ErrorCode {

    DUPLICATE_USER_ID(HttpStatus.CONFLICT.value(), "중복되는 아이디입니다."),
    NOT_MATCH_PASSWORD_CONFIRM(HttpStatus.BAD_REQUEST.value(), "비밀번호가 일치하지 않습니다"),
    NOT_FOUND_USER(HttpStatus.NOT_FOUND.value(), "존재하지 않는 유저입니다"),
    BAD_REQUEST_PASSWORD(HttpStatus.BAD_REQUEST.value(), "로그인 정보를 다시 확인하세요"),
    HANDLE_ACCESS_DENIED(HttpStatus.FORBIDDEN.value(), "로그인이 필요합니다."),
    CANT_ACCESS(HttpStatus.UNAUTHORIZED.value(), "접근권한이 없습니다"),
    FAIL_KAKAO_LOGIN(HttpStatus.BAD_REQUEST.value(), "카카오 로그인 실패!"),
    NOT_FOUND_ACCESS_TOKEN_RESPONSE(HttpStatus.BAD_REQUEST.value(), "엑세스 토큰 요청에 실패했습니다."),
    ;

    private final int code;
    private final String message;

    UserErrorCode(int code, String message){
        this.code = code;
        this.message=message;
    }

    @Override
    public int getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
