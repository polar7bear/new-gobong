package com.sns.gobong.domain.dto.response.oauth;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sns.gobong.domain.dto.response.user.UserSignUpResponseDto;
import com.sns.gobong.domain.entity.User;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class UserOAuthResponseDto {

    private Long id;
    private String nickname;
    private String email;
    private String accessToken;
    private String refreshToken;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime createdAt;

    public static UserOAuthResponseDto of(User user, String accessToken, String refreshToken) {
        return UserOAuthResponseDto.builder()
                .id(user.getId())
                .nickname(user.getNickname())
                .email(user.getEmail())
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .createdAt(user.getCreatedAt())
                .build();
    }
}
