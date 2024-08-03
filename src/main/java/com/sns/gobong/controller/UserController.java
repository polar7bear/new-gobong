package com.sns.gobong.controller;

import com.sns.gobong.domain.dto.request.SignUpUserRequestDto;
import com.sns.gobong.domain.dto.response.UserSignUpResponseDto;
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
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @PostMapping
    public ApiResponse<UserSignUpResponseDto> signUp(@Valid @RequestBody SignUpUserRequestDto dto) {
        UserSignUpResponseDto response = userService.signUp(dto);
        return new ApiResponse<>("회원가입이 성공적으로 완료되었습니다.", response);
    }
}
