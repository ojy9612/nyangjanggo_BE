package com.hanghae99_team3.model.user.domain.dto;

import lombok.*;

@Getter
@NoArgsConstructor
public class UserReqDto {
    private String nickname;
    private String userDescription;

    @Builder
    public UserReqDto(String nickname, String userDescription) {
        this.nickname = nickname;
        this.userDescription = userDescription;
    }
}
