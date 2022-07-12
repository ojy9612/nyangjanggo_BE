package com.hanghae99_team3.login.jwt;


import com.hanghae99_team3.login.jwt.dto.TokenDto;
import com.hanghae99_team3.login.jwt.dto.TokenRequestDto;
import com.hanghae99_team3.login.jwt.entity.RefreshToken;
import com.hanghae99_team3.login.jwt.repository.RefreshTokenRepository;
import com.hanghae99_team3.model.user.domain.User;
import com.hanghae99_team3.model.user.repository.UserRepository;
import com.hanghae99_team3.exception.newException.RefreshTokenException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
    public TokenDto login (PrincipalDetails principalDetails) {
        // AccessToken, RefreshToken 발급
        TokenDto tokenDto = jwtTokenProvider.createToken(principalDetails.getUsername(), principalDetails.getRole());

        // RefreshToken이 DB에 존재하는지 확인
        Optional<RefreshToken> findRefreshToken = refreshTokenRepository.findByUserId(principalDetails.getUserId());
        if (findRefreshToken.isPresent()) {
            throw new RefreshTokenException("refreshToken 존재");
        }

        // RefreshToken DB에 저장
        RefreshToken refreshToken = RefreshToken.builder()
                .userId(principalDetails.getUserId())
                .token(tokenDto.getRefreshToken())
                .build();
        refreshTokenRepository.save(refreshToken);


        return tokenDto;
    }

    @Transactional
    public String refresh(TokenRequestDto tokenRequestDto) {

        // accessToken 에서 User 불러오기
        String accessToken = tokenRequestDto.getAccessToken();
        String userPk = jwtTokenProvider.getUserPkFromToken(accessToken);
        User user = userRepository.findByEmail(userPk).orElseThrow(
                () -> new UsernameNotFoundException("해당하는 유저를 찾을 수 없습니다."));

        // refreshToken 이 expired 라면
        if (!jwtTokenProvider.validateToken(tokenRequestDto.getRefreshToken())) {
            refreshTokenRepository.deleteByUserId(user.getId());
            throw new RefreshTokenException();
        }
        // DB에 저장된 refreshToken과 일치하는지 검증
        RefreshToken refreshToken = refreshTokenRepository.findByUserId(user.getId()).orElseThrow(
                () -> new RefreshTokenException("RefreshToken을 찾을 수 없습니다.")
        );
        if (!tokenRequestDto.getRefreshToken().equals(refreshToken.getToken())) {
            throw new RefreshTokenException("RefreshToken이 일치하지 않습니다.");
        }

        // AccessToken, RefreshToken을 모두 재발급?

        return jwtTokenProvider.createAccessTokenOnly(user.getEmail(), user.getRole());


    }
}
