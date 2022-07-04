package com.hanghae99_team3.security.jwt;


import com.hanghae99_team3.model.user.domain.RefreshToken;
import com.hanghae99_team3.model.user.domain.User;
import com.hanghae99_team3.model.user.repository.RefreshTokenRepository;
import com.hanghae99_team3.model.user.repository.UserRepository;
import com.hanghae99_team3.security.exception.RefreshTokenException;
import com.hanghae99_team3.security.oauth2.PrincipalDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class TokenService {

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;

    @Transactional
    public String login (PrincipalDetails principalDetails) {
        // AccessToken, RefreshToken 발급
        TokenDto tokenDto = jwtTokenProvider.createToken(principalDetails.getUsername(), principalDetails.getRole());

        // RefreshToken이 DB에 존재하는지 확인
        Optional<RefreshToken> findRefreshToken = refreshTokenRepository.findByUserPk(principalDetails.getUsername());
        if (findRefreshToken.isPresent()) {
            return tokenDto.getAccessToken();
        }

        // RefreshToken DB에 저장
        RefreshToken refreshToken = RefreshToken.builder()
                .userPk(principalDetails.getUsername())
                .token(tokenDto.getRefreshToken())
                .build();
        refreshTokenRepository.save(refreshToken);
        return tokenDto.getAccessToken();
    }

    @Transactional
    public String refresh(TokenRequestDto tokenRequestDto) {

        // accessToken이 만료인지 다시 확인해야함

        String accessToken = tokenRequestDto.getAccessToken();
        Authentication authentication = jwtTokenProvider.getAuthentication(accessToken);
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        RefreshToken refreshToken = refreshTokenRepository.findByUserPk(principalDetails.getUsername()).orElseThrow(
                () -> new RefreshTokenException("RefreshToken을 찾을 수 없습니다.")
        );

        // refreshToken의 유효기간이 끝난 경우
        if (!jwtTokenProvider.validateToken(refreshToken.getToken())) {
            refreshTokenRepository.deleteByUserPk(principalDetails.getUsername());
            throw new RefreshTokenException();
        }

        return jwtTokenProvider.createAccessTokenOnly(principalDetails.getUsername(), principalDetails.getRole());
    }
}
