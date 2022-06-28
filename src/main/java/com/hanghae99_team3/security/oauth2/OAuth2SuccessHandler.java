package com.hanghae99_team3.security.oauth2;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hanghae99_team3.model.member.domain.User;
import com.hanghae99_team3.model.member.domain.UserRole;
import com.hanghae99_team3.security.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@Component
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {
//    private final TokenService tokenService;
    private final UserRequestMapper userRequestMapper;
    private final ObjectMapper objectMapper;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        User user = principalDetails.getUser();

        // 최초 로그인이라면 회원가입 처리를 한다.

        String token = jwtTokenProvider.createToken(user.getEmail(), UserRole.USER);
        log.info("{}", token);

        writeTokenResponse(response, token);
    }

    private void writeTokenResponse(HttpServletResponse response, String token)
            throws IOException {
        response.setContentType("text/html;charset=UTF-8");

        response.addHeader("Access-Token", token);
//        response.addHeader("Refresh", token.getRefreshToken());
        response.setContentType("application/json;charset=UTF-8");

        var writer = response.getWriter();
        writer.println(objectMapper.writeValueAsString(token));
        writer.flush();
    }
}