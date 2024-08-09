package com.sns.gobong.controller.user;

import com.sns.gobong.domain.dto.request.oauth.UserOAuthRegisterRequestDto;
import com.sns.gobong.domain.dto.response.oauth.UserOAuthResponseDto;
import com.sns.gobong.service.user.UserService;
import com.sns.gobong.util.api.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/oauth/login")
public class OAuthController {

    private final UserService userService;

    @PostMapping("/register")
    public ApiResponse<UserOAuthResponseDto> register(@Valid @RequestBody UserOAuthRegisterRequestDto dto) {
        UserOAuthResponseDto response = userService.oauthUserRegister(dto);
        return new ApiResponse<>("추가정보 등록이 성공적으로 완료되었습니다.", response);
    }

}
