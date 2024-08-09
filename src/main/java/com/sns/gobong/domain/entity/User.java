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

    @Column(unique = true, nullable = false)
    private String nickname;

    @Column(unique = true, nullable = false)
    private String email;

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
