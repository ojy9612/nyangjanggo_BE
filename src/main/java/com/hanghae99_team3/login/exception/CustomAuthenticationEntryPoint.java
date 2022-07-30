package com.hanghae99_team3.login.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException authException
    ) throws IOException, ServletException {
        ErrorCode errorCode = (ErrorCode) request.getAttribute("exception");
        if (errorCode == null) {
            errorCode = ErrorCode.ACCESS_TOKEN_NOT_EXIST;
        }
        sendErrorResponse(response, errorCode);
    }


    private void sendErrorResponse(HttpServletResponse response, ErrorCode errorCode)
            throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        ExceptionCode exceptionCode = new ExceptionCode(errorCode.getCode(), errorCode.getMessage());

        // response 설정
        response.setCharacterEncoding("utf-8");
        response.setStatus(errorCode.getStatus());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write(objectMapper.writeValueAsString(exceptionCode));
    }
}