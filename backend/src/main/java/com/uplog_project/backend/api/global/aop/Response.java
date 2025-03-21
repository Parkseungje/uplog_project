package com.uplog_project.backend.api.global.aop;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Response<T> {
    private boolean success;
    private int code;
    private String message;
    private T data;

    // 기본 성공 (data 없음)
    public static Response<Void> success() {
        return new Response<>(true, 200, "요청 성공", null);
    }

    // 기본 성공 (data 포함)
    public static <T> Response<T> success(T data) {
        return new Response<>(true, 200, "요청 성공", data);
    }

    // 커스텀 메시지 (data 포함)
    public static <T> Response<T> success(String message, T data) {
        return new Response<>(true, 200, message, data);
    }

    // 커스텀 메시지만 (data 없음)
    public static Response<Void> success(String message) {
        return new Response<>(true, 200, message, null);
    }

    // 실패
    public static Response<Void> fail(int code, String message) {
        return new Response<>(false, code, message, null);
    }
}
