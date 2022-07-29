package com.hanghae99_team3.login.jwt;


import com.hanghae99_team3.login.cookie.CookieUtil;
import com.hanghae99_team3.login.exception.NotExpiredTokenYetException;
import com.hanghae99_team3.login.exception.RefreshTokenException;
import com.hanghae99_team3.login.jwt.dto.TokenDto;
import com.hanghae99_team3.login.jwt.dto.TokenResponseDto;
import com.hanghae99_team3.login.jwt.entity.RefreshToken;
import com.hanghae99_team3.login.jwt.repository.RefreshTokenRepository;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

@Slf4j
@Service
public class TokenService {

    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserDetailsServiceImpl userDetailsService;

    @Autowired
    public TokenService(JwtTokenProvider jwtTokenProvider, RefreshTokenRepository refreshTokenRepository, UserDetailsServiceImpl userDetailsService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.refreshTokenRepository = refreshTokenRepository;
        this.userDetailsService = userDetailsService;
    }

    @Transactional
    public TokenDto login (PrincipalDetails principalDetails) {
        // AccessToken, RefreshToken 발급
        TokenDto tokenDto = jwtTokenProvider.createToken(principalDetails.getUsername(), principalDetails.getRole());


        // RefreshToken이 DB에 존재하는지 확인
        Optional<RefreshToken> findRefreshToken = refreshTokenRepository.findByUserId(principalDetails.getUserId());
        if (findRefreshToken.isPresent()) {
            // 존재하면 새로운 Token으로 Update
            findRefreshToken.get().updateToken(tokenDto.getRefreshToken());
        } else {
            // New RefreshToken DB에 저장
            RefreshToken refreshToken = RefreshToken.builder()
                    .userId(principalDetails.getUserId())
                    .token(tokenDto.getRefreshToken())
                    .build();
            refreshTokenRepository.save(refreshToken);
        }

        return tokenDto;
    }


    @Transactional
    public TokenResponseDto refresh(HttpServletRequest request, HttpServletResponse response) {

        // Token 추출
        String accessToken = jwtTokenProvider.resolveToken(request);
        String refreshToken = CookieUtil.getCookie(request, "Refresh-Token")
                .map(Cookie::getValue)
                .orElse((null));
        log.info(refreshToken);
        if (refreshToken == null) {
            throw new RefreshTokenException("RefreshToken이 없습니다.");
        }


        // AccessToken not expired yet
        try {
            if (jwtTokenProvider.validateToken(accessToken)) {
                throw new NotExpiredTokenYetException();
            }
        } catch (ExpiredJwtException e) {
            log.info("[AccessToken 만료, 재발급 요청]");
        }


        // accessToken 에서 User 불러오기
        String userPk = jwtTokenProvider.getUserPkFromToken(accessToken);
        PrincipalDetails principalDetails = (PrincipalDetails) userDetailsService.loadUserByUsername(userPk);


        // refreshToken valid 검증
        jwtTokenProvider.validateToken(refreshToken);


        // DB에 저장된 refreshToken과 일치하는지 검증
        RefreshToken userRefreshToken = refreshTokenRepository.findByUserId(principalDetails.getUserId()).orElseThrow(
                () -> new RefreshTokenException("RefreshToken을 찾을 수 없습니다.")
        );
        if (!refreshToken.equals(userRefreshToken.getToken())) {
            throw new RefreshTokenException("RefreshToken이 일치하지 않습니다.");
        }

        // AccessToken & RefreshToken 재발급
        TokenDto tokenDto = login(principalDetails);

        // Cookie 설정
        Long refreshTokenExpiry = tokenDto.getRefreshTokenExpireDate();
        int cookieMaxAge = (int) (refreshTokenExpiry / 60);
        CookieUtil.deleteCookie(request, response, "Refresh-Token");
        CookieUtil.addCookie(response, "Refresh-Token", tokenDto.getRefreshToken(), cookieMaxAge);

        return new TokenResponseDto(tokenDto.getAccessToken(), tokenDto.getAccessTokenExpireDate());

    }
}
