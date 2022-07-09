package com.hanghae99_team3.login.jwt.dto;

import lombok.*;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TokenDto {
    private String accessToken;
    private String refreshToken;
    private Long accessTokenExpireDate;
}
