package com.hanghae99_team3.login.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hanghae99_team3.login.exception.ErrorCode;
import com.hanghae99_team3.login.exception.ExceptionResDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class TokenAccessDeniedHandler implements AccessDeniedHandler {


    @Override
    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException, ServletException {

        ErrorCode errorCode = ErrorCode.ACCESS_DENIED;
        ExceptionResDto exceptionResDto = new ExceptionResDto(errorCode);

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(errorCode.getStatus());

        // response 설정
        final ObjectMapper objectMapper = new ObjectMapper();
        response.setCharacterEncoding("utf-8");
        response.setStatus(errorCode.getStatus());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write(objectMapper.writeValueAsString(exceptionResDto));

//        final Map<String, Object> body = new HashMap<>();
//        body.put("code", HttpServletResponse.SC_FORBIDDEN);
//        body.put("payload", "You don't have required role to perform this action.");
//
//        final ObjectMapper mapper = new ObjectMapper();
//        mapper.writeValue(response.getOutputStream(), body);
    }

}
