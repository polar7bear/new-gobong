package com.sns.gobong.config.security.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Component
public class JwtFilter extends OncePerRequestFilter {

    private final TokenProvider tokenProvider;

    public JwtFilter(TokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    private final static List<String> EXCLUDE_URLS = Arrays.asList("/users/sign-up", "/users/sign-in", "/oauth/login/register", "/message");

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String path = request.getRequestURI();
        if (EXCLUDE_URLS.contains(path)) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = tokenProvider.resolveToken(request);

        if (StringUtils.hasText(token) && tokenProvider.validationAccessToken(token)) {
            Authentication authentication = tokenProvider.getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            log.info("[JwtFilter]: Security Context에 \"{}\"님의 인증 정보를 저장하였습니다. URI: {}", authentication.getName(), request.getRequestURI());
        } else {
            log.warn("[JwtFilter]: JWT 토큰이 유효하지 않습니다. URI: {}", request.getRequestURI());
        }
        filterChain.doFilter(request, response);
    }
}
