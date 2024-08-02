package com.sns.gobong.config.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sns.gobong.util.api.ApiError;
import com.sns.gobong.util.api.ErrorType;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class TokenAccessDeniedHandler implements AccessDeniedHandler {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setHeader("Content-Type", "application/json");

        response.getWriter().write(objectMapper.writeValueAsString(
                new ApiError(ErrorType.FORBIDDEN, "403", "요청이 거부되었습니다.")
        ));
        response.getWriter().flush();
        response.getWriter().close();
    }
}
