package com.hanghae99_team3.login.jwt.dto;

import lombok.*;

@Getter
@NoArgsConstructor
@ToString
public class TokenDto {
    private String accessToken;
    private String refreshToken;
    private Long accessTokenExpireDate;
    private Long refreshTokenExpireDate;

    @Builder
    public TokenDto(String accessToken, String refreshToken, Long accessTokenExpireDate, Long refreshTokenExpireDate) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.accessTokenExpireDate = accessTokenExpireDate;
        this.refreshTokenExpireDate = refreshTokenExpireDate;
    }
}
