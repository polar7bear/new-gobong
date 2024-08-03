package com.sns.gobong.exception;

import com.sns.gobong.exception.user.UserAlreadyExistsException;
import com.sns.gobong.exception.user.WrongPasswordException;
import com.sns.gobong.util.api.ApiError;
import com.sns.gobong.util.api.ApiResponse;
import com.sns.gobong.util.api.ErrorType;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ApiResponse<Void> handleUserAlreadyExistsException(UserAlreadyExistsException e) {
        ApiError apiError = new ApiError(ErrorType.CONFLICT, "409", e.getMessage(), e);
        return new ApiResponse<>("이미 존재하는 회원입니다.", apiError);
    }

    @ExceptionHandler(WrongPasswordException.class)
    public ApiResponse<Void> handleWorngPasswordException(WrongPasswordException e) {
        ApiError apiError = new ApiError(ErrorType.UNAUTHORIZED, "401", e.getMessage(), e);
        return new ApiResponse<>("비밀번호가 일치하지 않습니다.", apiError);
    }
}
