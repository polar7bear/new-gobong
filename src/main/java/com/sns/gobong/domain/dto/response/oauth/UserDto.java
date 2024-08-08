package com.sns.gobong.domain.dto.response.oauth;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserDto {
    private String nickname;
    private String email;
    private String role;
}
