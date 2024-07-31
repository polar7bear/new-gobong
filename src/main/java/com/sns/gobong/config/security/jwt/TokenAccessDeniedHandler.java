package com.sns.gobong.config.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class TokenAccessDeniedHandler implements AccessDeniedHandler {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setHeader("Content-Type", "application/json");

        Map<String, Object> errorDetails = new HashMap<>();
        errorDetails.put("message", "Forbidden");
        errorDetails.put("status", HttpStatus.FORBIDDEN.value());       // TODO: API 유틸 클래스 만들어서 리팩토링하기

        response.getWriter().write(objectMapper.writeValueAsString(errorDetails));
        response.getWriter().flush();
        response.getWriter().close();
    }
}
