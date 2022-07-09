package com.hanghae99_team3.login.jwt.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
public class TokenRequestDto {
    private String accessToken;

    @Builder
    public TokenRequestDto(String accessToken) {
        this.accessToken = accessToken;

    }
}
