package com.sns.gobong.domain.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class UserSignOutRequestDto {

    @JsonProperty(value = "refresh_token")
    private String refreshToken;
}
