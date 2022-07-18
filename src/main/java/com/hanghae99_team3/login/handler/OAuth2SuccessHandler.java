package com.hanghae99_team3.login.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hanghae99_team3.login.cookie.CookieUtil;
import com.hanghae99_team3.login.jwt.JwtTokenProvider;
import com.hanghae99_team3.login.jwt.PrincipalDetails;
import com.hanghae99_team3.login.jwt.TokenService;
import com.hanghae99_team3.login.jwt.dto.TokenDto;
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

        // 새로운 회원인지 판별
        boolean isNew = principalDetails.isNew();

        // Token 발행
        TokenDto tokenDto = tokenService.login(principalDetails);

        log.info("{}", tokenDto.toString());

        // Cookie 설정
        Long refreshTokenExpiry = tokenDto.getRefreshTokenExpireDate();
        int cookieMaxAge = (int) (refreshTokenExpiry / 60);
        CookieUtil.deleteCookie(request, response, "Refresh-Token");
        CookieUtil.addCookie(response, "Refresh-Token", tokenDto.getRefreshToken(), cookieMaxAge);

        // Redirect
        getRedirectStrategy().sendRedirect(request, response, makeRedirectUrl(tokenDto, isNew));
    }



    private String makeRedirectUrl(TokenDto tokenDto, Boolean isNew) {

        return UriComponentsBuilder.fromUriString("https://localhost:3000/oauth2/redirect/")
                .queryParam("Access-Token", tokenDto.getAccessToken())
                .queryParam("expireDate", tokenDto.getAccessTokenExpireDate())
                .queryParam("isNew", isNew.toString())
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