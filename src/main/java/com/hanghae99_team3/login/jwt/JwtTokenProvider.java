package com.hanghae99_team3.login.jwt;

import com.hanghae99_team3.login.jwt.dto.TokenDto;
import com.hanghae99_team3.model.user.domain.UserRole;
import com.hanghae99_team3.login.exception.TokenValidFailedException;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.Date;

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtTokenProvider {


    @Value("{spring.jwt.secret}")
    private String secretKey;



    // 토큰 유효시간
//    private final Long accessTokenValidTime = 14 * 24 * 60 * 60 * 1000L; // 14days
    private final Long accessTokenValidTime = 300 * 1000L; //10초
    private final Long refreshTokenValidTime = 14 * 24 * 60 * 60 * 1000L; // 14days
//    private final Long refreshTokenValidTime = 600 * 1000L; // 60초
    private final UserDetailsService userDetailsService;

    // 객체 초기화, secretKey를 Base64로 인코딩한다.
    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    // JWT 토큰 생성
    public TokenDto createToken(String userPk, UserRole roles) {
        Claims claims = Jwts.claims().setSubject(userPk); // JWT payload 에 저장되는 정보단위
        claims.put("roles", roles); // 정보는 key / value 쌍으로 저장된다.
        Date now = new Date();

        String accessToken = Jwts.builder()
                .setClaims(claims) // 정보 저장
                .setIssuedAt(now) // 토큰 발행 시간 정보
                .setExpiration(new Date(now.getTime() + accessTokenValidTime)) // set Expire Time
                .signWith(SignatureAlgorithm.HS256, secretKey)  // 사용할 암호화 알고리즘과
                // signature 에 들어갈 secret값 세팅
                .compact();
        String refreshToken = Jwts.builder()
                .setExpiration(new Date(now.getTime() + refreshTokenValidTime)) // set Expire Time
                .signWith(SignatureAlgorithm.HS256, secretKey)  // 사용할 암호화 알고리즘과
                // signature 에 들어갈 secret값 세팅
                .compact();

        return TokenDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .accessTokenExpireDate(accessTokenValidTime)
                .refreshTokenExpireDate(refreshTokenValidTime)
                .build();
    }

//    public String createAccessTokenOnly(String userPk, UserRole roles) {
//        Claims claims = Jwts.claims().setSubject(userPk); // JWT payload 에 저장되는 정보단위
//        claims.put("roles", roles); // 정보는 key / value 쌍으로 저장된다.
//        Date now = new Date();
//        return Jwts.builder()
//                .setClaims(claims) // 정보 저장
//                .setIssuedAt(now) // 토큰 발행 시간 정보
//                .setExpiration(new Date(now.getTime() + accessTokenValidTime)) // set Expire Time
//                .signWith(SignatureAlgorithm.HS256, secretKey)  // 사용할 암호화 알고리즘과
//                // signature 에 들어갈 secret값 세팅
//                .compact();
//    }


    // JWT 토큰에서 인증 정보 조회
    public Authentication getAuthentication(String token) {

        // access token 에서  claims 추출
        Claims claims = parseClaims(token);

        // 권한 정보가 없을 때
        if (claims.get("roles") == null) {
            throw new IllegalArgumentException("권한 정보 없음");
        }

        UserDetails userDetails = userDetailsService.loadUserByUsername(claims.getSubject());
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    // JWT 토큰에서 UserPk 추출
    public String getUserPkFromToken(String token) {

        Authentication authentication = getAuthentication(token);
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        return principalDetails.getUsername();
    }


    // jwt 토큰 복호화
    public Claims parseClaims(String token) {
        try {
            return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

    // Request의 Header에서 token 값을 가져옵니다. "Access-Token" : "TOKEN값'
    public String resolveToken(HttpServletRequest request) {
        String headerValue = request.getHeader("Access-Token");

        if (headerValue == null) {
            return null;
        } else {
            return request.getHeader("Access-Token");
        }
    }

    // 토큰의 유효성 + 만료일자 확인
    public boolean validateToken(String jwtToken) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(jwtToken);
            return !claims.getBody().getExpiration().before(new Date());
        } catch (TokenValidFailedException e) {
            throw new TokenValidFailedException();
        }

    }
}