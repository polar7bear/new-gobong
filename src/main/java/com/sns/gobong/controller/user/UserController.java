package com.sns.gobong.controller.user;

import com.sns.gobong.domain.dto.request.user.UserSignInRequestDto;
import com.sns.gobong.domain.dto.request.user.UserSignOutRequestDto;
import com.sns.gobong.domain.dto.request.user.UserSignUpRequestDto;
import com.sns.gobong.domain.dto.response.user.UserSignInResponseDto;
import com.sns.gobong.domain.dto.response.user.UserSignUpResponseDto;
import com.sns.gobong.service.user.RefreshTokenService;
import com.sns.gobong.service.user.UserService;
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
