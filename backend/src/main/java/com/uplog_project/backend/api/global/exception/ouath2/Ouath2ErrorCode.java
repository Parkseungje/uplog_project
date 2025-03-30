package com.uplog_project.backend.api.global.exception.ouath2;

import com.uplog_project.backend.api.global.exception.ErrorCode;
import org.springframework.http.HttpStatus;

public enum Ouath2ErrorCode implements ErrorCode {

    INVALID_GOOGLE_CLIENT_CONFIG(HttpStatus.BAD_REQUEST.value(), "올바르지 않은 구글 정보입니다."),
    ;

    private final int code;
    private final String message;

    Ouath2ErrorCode(int code, String message){
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
