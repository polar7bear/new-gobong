package com.sns.gobong.domain.dto.response.oauth;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserDto {
    private String name;
    private String userName;
    private String role;
}
