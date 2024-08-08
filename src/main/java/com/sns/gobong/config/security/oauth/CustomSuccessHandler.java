package com.sns.gobong.config.security.oauth;

import com.sns.gobong.config.security.jwt.TokenProvider;
import com.sns.gobong.domain.dto.request.user.UserSignUpRequestDto;
import com.sns.gobong.domain.dto.response.oauth.CustomOAuth2User;
import com.sns.gobong.domain.dto.response.oauth.UserDto;
import com.sns.gobong.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@RequiredArgsConstructor
@Component
public class CustomSuccessHandler implements AuthenticationSuccessHandler {

    private final UserRepository userRepository;
    private final TokenProvider tokenProvider;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();
        String nickname = oAuth2User.getName();
        boolean exist = userRepository.existsByNickname(nickname);

        if (exist) {
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

            response.sendRedirect("/oauth/login/success");
        } else {
            UserDto userDto = UserDto.builder()
                    .email(oAuth2User.getEmail())
                    .nickname(oAuth2User.getName())
                    .role(oAuth2User.getAuthorities().toString()).build();

            String redirectUrl = "/register?email=" + URLEncoder.encode(userDto.getEmail(), StandardCharsets.UTF_8)
                    + "&nickname=" + URLEncoder.encode(userDto.getNickname(), StandardCharsets.UTF_8)
                    + "&role=" + URLEncoder.encode(userDto.getRole(), StandardCharsets.UTF_8);

            response.sendRedirect("http://localhost:3000" + redirectUrl);
        }


    }
}
