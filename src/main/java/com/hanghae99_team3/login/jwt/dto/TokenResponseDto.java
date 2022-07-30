package com.hanghae99_team3.login.jwt.dto;

import lombok.*;

@Getter
@NoArgsConstructor
public class TokenResponseDto {

    private String accessToken;
    private Long accessTokenExpireDate;


    @Builder
    public TokenResponseDto(String accessToken, Long accessTokenExpireDate) {
        this.accessToken = accessToken;
        this.accessTokenExpireDate = accessTokenExpireDate;
    }
}
