package com.sns.gobong.service.user;

import com.sns.gobong.config.security.jwt.TokenProvider;
import com.sns.gobong.domain.dto.request.oauth.UserOAuthRegisterRequestDto;
import com.sns.gobong.domain.dto.request.user.UserSignInRequestDto;
import com.sns.gobong.domain.dto.request.user.UserSignUpRequestDto;
import com.sns.gobong.domain.dto.response.oauth.UserOAuthResponseDto;
import com.sns.gobong.domain.dto.response.user.UserSignInResponseDto;
import com.sns.gobong.domain.dto.response.user.UserSignUpResponseDto;
import com.sns.gobong.domain.entity.User;
import com.sns.gobong.exception.user.UserAlreadyExistsException;
import com.sns.gobong.exception.user.WrongPasswordException;
import com.sns.gobong.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    private final RefreshTokenService refreshTokenService;

    @Transactional
    public UserSignUpResponseDto signUp(UserSignUpRequestDto userDto) {
        userRepository.findByEmail(userDto.getEmail())
                .ifPresent(existUser -> {
                    throw new UserAlreadyExistsException("이미 존재하는 회원입니다.");
                });
        String encoded = passwordEncoder.encode(userDto.getPw());
        userDto.setPw(encoded);
        User user = UserSignUpRequestDto.from(userDto);
        userRepository.save(user);

        return UserSignUpResponseDto.of(user);
    }

    @Transactional
    public UserOAuthResponseDto oauthUserRegister(UserOAuthRegisterRequestDto dto) {
        User user = UserOAuthRegisterRequestDto.from(dto);
        userRepository.save(user); // 회원 유무 존재는 핸들러에서 체크함
        Authentication authentication = new UsernamePasswordAuthenticationToken(user.getEmail(), null, user.getAuthorities());
        String accessToken = tokenProvider.createAccessToken(authentication);
        String refreshToken = tokenProvider.createRefreshToken(authentication);

        refreshTokenService.saveRefreshToken(user, refreshToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return UserOAuthResponseDto.of(user, accessToken, refreshToken);
    }

    @Transactional
    public UserSignInResponseDto signIn(UserSignInRequestDto userDto) {
        User user = userRepository.findByEmail(userDto.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("존재 하지않는 이메일입니다."));

        if (!passwordEncoder.matches(userDto.getPw(), user.getPw())) {
            throw new WrongPasswordException("비밀번호가 일치하지 않습니다.");
        }
        Authentication authentication = new UsernamePasswordAuthenticationToken(user.getEmail(), null, user.getAuthorities());
        String accessToken = tokenProvider.createAccessToken(authentication);
        String refreshToken = tokenProvider.createRefreshToken(authentication);

        SecurityContextHolder.getContext().setAuthentication(authentication);

        refreshTokenService.saveRefreshToken(user, refreshToken);

        return UserSignInResponseDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

}
