package com.sns.gobong.util.api;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ApiError {

    private ErrorType errorType;
    private String code;
    private String message;
    private String errorStack;

    public ApiError(ErrorType errorType, String code, String message) {
        this.errorType = errorType;
        this.code = code;
        this.message = message;
    }

    public ApiError(ErrorType errorType, String code, String message, Throwable e) {
        this.errorType = errorType;
        this.code = code;
        this.message = message;
        this.errorStack = e.getMessage();
    }
}
