package com.sns.gobong.config.security.oauth;

import com.sns.gobong.config.security.jwt.TokenProvider;
import com.sns.gobong.domain.dto.request.user.UserSignUpRequestDto;
import com.sns.gobong.domain.dto.response.oauth.CustomOAuth2User;
import com.sns.gobong.domain.dto.response.oauth.UserDto;
import com.sns.gobong.domain.entity.User;
import com.sns.gobong.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

@RequiredArgsConstructor
@Component
public class CustomSuccessHandler implements AuthenticationSuccessHandler {

    private final UserRepository userRepository;
    private final TokenProvider tokenProvider;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();
        String email = oAuth2User.getEmail();
        String provider = oAuth2User.getProvider();

        // 이메일을 기반으로 기존 사용자 조회
        Optional<User> existingUserByEmail = userRepository.findByEmail(email);

        if (existingUserByEmail.isPresent()) {
            User existingUser = existingUserByEmail.get();

            // 자체 회원가입 사용자이거나, 다른 제공자로 이미 가입된 경우
            if (!existingUser.getProvider().equals(provider)) {
                throw new OAuth2AuthenticationException("이미 가입 되어있는 회원입니다.");
            }

            // 동일한 제공자로 로그인한 경우 (이메일과 제공자 정보로 사용자를 식별)
            String accessToken = tokenProvider.createAccessToken(authentication);
            String refreshToken = tokenProvider.createRefreshToken(authentication);
            response.addHeader("Authorization", "Bearer " + accessToken);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write("{\"accessToken\":\"" + accessToken + "\", \"refreshToken\":\"" + refreshToken + "\"}");

            // 리프레시토큰을 쿠키에 저장
            Cookie refreshTokenCookie = new Cookie("Refresh-Token", refreshToken);
            refreshTokenCookie.setHttpOnly(true);
            refreshTokenCookie.setSecure(true);
            refreshTokenCookie.setPath("/");
            refreshTokenCookie.setMaxAge(7 * 24 * 60 * 60);
            response.addCookie(refreshTokenCookie);

            response.sendRedirect("/oauth/login/success");
        } else {
            // 새로운 사용자 또는 추가 정보를 입력받아야 하는 경우
            UserDto userDto = UserDto.builder()
                    .email(oAuth2User.getEmail())
                    .nickname(oAuth2User.getName())
                    .role(oAuth2User.getAuthorities().toString())
                    .provider(provider)
                    .build();

            // 추가 정보 입력 페이지로 리다이렉트 (전화번호 등)
            String redirectUrl = "/register?email=" + URLEncoder.encode(userDto.getEmail(), StandardCharsets.UTF_8)
                    + "&nickname=" + URLEncoder.encode(userDto.getNickname(), StandardCharsets.UTF_8)
                    + "&role=" + URLEncoder.encode(userDto.getRole(), StandardCharsets.UTF_8)
                    + "&provider=" + URLEncoder.encode(provider, StandardCharsets.UTF_8);

            response.sendRedirect("http://localhost:3000" + redirectUrl);
        }
    }
}

