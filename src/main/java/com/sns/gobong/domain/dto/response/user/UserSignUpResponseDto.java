package com.sns.gobong.domain.dto.response.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sns.gobong.domain.entity.User;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@Getter
public class UserSignUpResponseDto {

    private Long id;
    private String nickname;
    private String email;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime createdAt;

    public static UserSignUpResponseDto of(User user) {
        return UserSignUpResponseDto.builder()
                .id(user.getId())
                .nickname(user.getNickname())
                .email(user.getEmail())
                .createdAt(user.getCreatedAt())
                .build();
    }

}
