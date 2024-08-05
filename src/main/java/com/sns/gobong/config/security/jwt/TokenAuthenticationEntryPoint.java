package com.sns.gobong.config.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sns.gobong.util.api.ApiError;
import com.sns.gobong.util.api.ErrorType;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class TokenAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        response.setHeader("Content-Type", "application/json");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        response.getWriter().write(objectMapper.writeValueAsString(
                new ApiError(ErrorType.UNAUTHORIZED, "401", "접근할 수 없는 권한입니다.")
        ));
        response.getWriter().flush();
        response.getWriter().close();
    }
}
