package com.hanghae99_team3.model.good;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Getter
@NoArgsConstructor
public class GoodResponseDto implements Serializable {

    private String nickname;
    private String userImg;

    public GoodResponseDto(@NotNull Good good) {
        this.nickname = good.getUser().getNickname();
        this.userImg = good.getUser().getUserImg();
    }
}
