package com.sns.gobong.domain.entity;

import com.sns.gobong.domain.status.Role;
import com.sns.gobong.domain.status.VisibilityStatus;
import com.sns.gobong.util.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.Collections;

@Entity
@Table(name = "gobong_user")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //@NotBlank
    //@Size(min = 2, max = 8, message = "2글자 이상, 8글자 이하로 입력해주세요.")
    @Column(length = 8, unique = true, nullable = false)
    private String nickname;

    //@Email(message = "이메일 형식을 지켜주세요.")
    //@NotBlank(message = "필수 입력란입니다.")
    //@Size(max = 30, message = "30자이내로 입력해주세요.")
    @Column(length = 30, unique = true, nullable = false)
    private String email;

    //@NotBlank(message = "필수 입력란입니다.")
    @Column(nullable = false)
    private String pw;

    private String img; // TODO: AWS S3

    //@Pattern(regexp = "^\\d{3}-\\d{4}-\\d{4}$", message = "전화번호 형식은 XXX-XXXX-XXXX이어야 합니다.")
    @Column(nullable = false, unique = true)
    private String tel;

    @Enumerated(EnumType.STRING)
    @Column(name = "visibility_status")
    private VisibilityStatus visibility;

    @Enumerated(EnumType.STRING)
    private Role role = Role.ROLE_USER;

    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority(role.name()));
    }

}
