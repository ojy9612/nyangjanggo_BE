package com.hanghae99_team3.login.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hanghae99_team3.login.jwt.JwtTokenProvider;
import com.hanghae99_team3.login.jwt.PrincipalDetails;
import com.hanghae99_team3.login.jwt.TokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@Component
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final ObjectMapper objectMapper;
    private final JwtTokenProvider jwtTokenProvider;
    private final TokenService tokenService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException{
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();


        // 최초 로그인이라면 회원가입 처리를 한다.
        String accessToken = tokenService.login(principalDetails);

//        TokenDto token = jwtTokenProvider.createToken(principalDetails.getUsername(), UserRole.USER);
        log.info("{}", accessToken);
//        TokenDto testToken = new TokenDto();
//        writeTokenResponse(response, token);

        getRedirectStrategy().sendRedirect(request, response, makeRedirectUrl(accessToken));
    }


    private String makeRedirectUrl(String accessToken) {

        return UriComponentsBuilder.fromUriString("http://localhost:3000/oauth2/redirect/")
                .queryParam("token", accessToken)
                .build().toUriString();
    }
//    private void writeTokenResponse(HttpServletResponse response, String token)
//            throws IOException {
//        response.setContentType("text/html;charset=UTF-8");
//
//        response.addHeader("Access-Token", token);
////        response.addHeader("Refresh", token.getRefreshToken());
//        response.setContentType("application/json;charset=UTF-8");
//
//        var writer = response.getWriter();
//        writer.println(objectMapper.writeValueAsString(token));
//        writer.flush();
//    }
}