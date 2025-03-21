package com.uplog_project.backend.api.global.exception;

import com.uplog_project.backend.api.global.aop.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Response> handleException(Exception e) {

        Response errorResponse = new Response(false, HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage(), e.getCause());

        // 에러 응답 생성
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<Response> handleCustomException(CustomException e) {
        // 에러 정보를 담은 ErrorResponse 객체 생성
        Response errorResponse = new Response(false, e.getErrorCode().getCode(), e.getMessage(), null);

        // 에러 응답 생성
        return ResponseEntity.status(e.getErrorCode().getCode()).body(errorResponse);
    }
}
