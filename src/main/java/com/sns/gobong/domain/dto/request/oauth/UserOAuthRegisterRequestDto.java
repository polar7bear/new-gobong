package com.sns.gobong.domain.dto.request.oauth;

import com.sns.gobong.domain.dto.request.user.UserSignUpRequestDto;
import com.sns.gobong.domain.entity.User;
import com.sns.gobong.domain.status.Role;
import com.sns.gobong.domain.status.VisibilityStatus;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class UserOAuthRegisterRequestDto {

    @NotBlank
    private String nickname;

    @NotBlank(message = "필수 입력란입니다.")
    private String email;

    private String img; // TODO: AWS S3

    @Pattern(regexp = "^\\d{3}-\\d{4}-\\d{4}$", message = "전화번호 형식은 XXX-XXXX-XXXX이어야 합니다.")
    private String tel;

    private VisibilityStatus visibility;

    public static User from(UserOAuthRegisterRequestDto dto) {
        return User.builder()
                .nickname(dto.getNickname())
                .email(dto.getEmail())
                .tel(dto.getTel())
                .img(dto.getImg())
                .visibility(dto.getVisibility())
                .role(Role.ROLE_USER)
                .build();
    }
}
