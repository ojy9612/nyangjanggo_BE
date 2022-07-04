package com.hanghae99_team3.security.jwt;

import lombok.*;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TokenDto {
    private String accessToken = "test1234";
    private String refreshToken = "test12345678";
    private Long accessTokenExpireDate;
}
