package com.sns.gobong.domain.dto.request.user;

import com.sns.gobong.domain.entity.User;
import com.sns.gobong.domain.status.Role;
import com.sns.gobong.domain.status.VisibilityStatus;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserSignUpRequestDto {


    @NotBlank
    @Size(min = 2, max = 8, message = "2글자 이상, 8글자 이하로 입력해주세요.")
    private String nickname;

    @Email(message = "이메일 형식을 지켜주세요.")
    @NotBlank(message = "필수 입력란입니다.")
    @Size(max = 30, message = "30자이내로 입력해주세요.")
    private String email;

    @NotBlank(message = "필수 입력란입니다.")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[~!@#$%^&*()+|=])[A-Za-z\\d~!@#$%^&*()+|=]{8,16}$",
            message = "영소문자와 특수기호가 1개 이상 포함된 최소 8자 이상, 16자 이하의 길이로 입력해주세요.")
    private String pw;

    private String img; // TODO: AWS S3

    @Pattern(regexp = "^\\d{3}-\\d{4}-\\d{4}$", message = "전화번호 형식은 XXX-XXXX-XXXX이어야 합니다.")
    private String tel;

    private VisibilityStatus visibility;

    public static User from(UserSignUpRequestDto dto) {
        return User.builder()
                .nickname(dto.getNickname())
                .email(dto.getEmail())
                .pw(dto.getPw())
                .tel(dto.getTel())
                .img(dto.getImg())
                .visibility(dto.getVisibility())
                .role(Role.ROLE_USER)
                .build();
    }
}
