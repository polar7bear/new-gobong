package com.sns.gobong.domain.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserSignInResponseDto {

    private Long id;
    private String email;
    private String accessToken;
    private String refreshToken;
}
