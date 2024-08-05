package com.sns.gobong.controller;

import com.sns.gobong.domain.dto.request.UserSignInRequestDto;
import com.sns.gobong.domain.dto.request.UserSignOutRequestDto;
import com.sns.gobong.domain.dto.request.UserSignUpRequestDto;
import com.sns.gobong.domain.dto.response.UserSignInResponseDto;
import com.sns.gobong.domain.dto.response.UserSignUpResponseDto;
import com.sns.gobong.service.RefreshTokenService;
import com.sns.gobong.service.UserService;
import com.sns.gobong.util.api.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final RefreshTokenService refreshTokenService;

    @PostMapping("/sign-up")
    public ApiResponse<UserSignUpResponseDto> signUp(@Valid @RequestBody UserSignUpRequestDto dto) {
        UserSignUpResponseDto response = userService.signUp(dto);
        return new ApiResponse<>("회원가입이 성공적으로 완료되었습니다.", response);
    }

    @PostMapping("/sign-in")
    public ApiResponse<UserSignInResponseDto> signIn(@Valid @RequestBody UserSignInRequestDto dto) {
        UserSignInResponseDto response = userService.signIn(dto);

        return new ApiResponse<>("로그인이 성공적으로 완료되었습니다.", response);
    }

    @PostMapping("/sign-out")
    public ApiResponse<Void> signOut(@RequestBody UserSignOutRequestDto dto) {
        String message = refreshTokenService.deleteRefreshToken(dto);
        return new ApiResponse<>(message);
    }
}
