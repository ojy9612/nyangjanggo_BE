package com.hanghae99_team3.model.user.domain.dto;

import lombok.*;

import java.io.Serializable;

@Getter
@NoArgsConstructor
public class UserResDto implements Serializable {
    private String nickname;
    private String userImg;
    private String userDescription;

    @Builder
    public UserResDto(String nickname, String userImg, String userDescription) {
        this.nickname = nickname;
        this.userImg = userImg;
        this.userDescription = userDescription;
    }
}
