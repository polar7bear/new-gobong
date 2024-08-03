package com.sns.gobong.util.api;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
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
        this.errorStack = getStackTraceAsString(e);
    }

    private String getStackTraceAsString(Throwable e) {
        StringBuilder sb = new StringBuilder();
        for (StackTraceElement element : e.getStackTrace()) {
            sb.append(element.toString()).append("\n");
        }
        return sb.toString();
    }
}
