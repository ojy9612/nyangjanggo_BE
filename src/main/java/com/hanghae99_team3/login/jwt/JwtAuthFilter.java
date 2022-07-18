package com.hanghae99_team3.login.jwt;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.hanghae99_team3.login.exception.ErrorCode;
import com.hanghae99_team3.login.exception.ExceptionCode;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
public class JwtAuthFilter extends GenericFilterBean {

    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public JwtAuthFilter(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }


    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        // 검증
        log.info("[Verifying Token]");

        // 헤더에서 JWT 를 받아옵니다.

        String token = jwtTokenProvider.resolveToken((HttpServletRequest) request);

        // 유효한 토큰인지 확인합니다.
        try {
            if (token != null && jwtTokenProvider.validateToken(token)) {
                // 토큰이 유효하면 토큰으로부터 유저 정보를 받아옵니다.
                Authentication authentication = jwtTokenProvider.getAuthentication(token);
                // SecurityContext 에 Authentication 객체를 저장합니다.
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }

        } catch (ExpiredJwtException e) {
            log.error(e.getMessage());
            request.setAttribute("exception", ErrorCode.EXPIRED_ACCESS_TOKEN);
        } catch (SignatureException e) {
            log.error(e.getMessage());
            request.setAttribute("exception", ErrorCode.ACCESS_SIGNATURE_NOT_MATCH);
        } catch (JwtException e) {
            log.error(e.getMessage());
            request.setAttribute("exception", ErrorCode.INVALID_TOKEN);
        }

        chain.doFilter(request, response);
    }

}