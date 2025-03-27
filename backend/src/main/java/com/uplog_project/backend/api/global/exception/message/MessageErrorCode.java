package com.uplog_project.backend.api.global.exception.message;

import com.uplog_project.backend.api.global.exception.ErrorCode;
import org.springframework.http.HttpStatus;

public enum MessageErrorCode implements ErrorCode {

    NOT_FOUND_MESSAGE(HttpStatus.NOT_FOUND.value(), "존재하지 않는 메세지 유형입니다");

    private final int code;
    private final String message;

    MessageErrorCode(int code, String message){
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
