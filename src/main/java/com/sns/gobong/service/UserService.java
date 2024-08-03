package com.sns.gobong.service;

import com.sns.gobong.domain.dto.request.SignUpUserRequestDto;
import com.sns.gobong.domain.dto.response.UserSignUpResponseDto;
import com.sns.gobong.domain.entity.User;
import com.sns.gobong.exception.user.UserAlreadyExistsException;
import com.sns.gobong.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserSignUpResponseDto signUp(SignUpUserRequestDto userDto) {
        userRepository.findByEmail(userDto.getEmail())
                .ifPresent(existUser -> {
                    throw new UserAlreadyExistsException("이미 존재하는 회원입니다.");
                });
        String encoded = passwordEncoder.encode(userDto.getPw());
        userDto.setPw(encoded);
        User user = SignUpUserRequestDto.from(userDto);
        userRepository.save(user);

        return UserSignUpResponseDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .nickname(user.getNickname())
                .createdAt(user.getCreatedAt())
                .build();
    }
}
