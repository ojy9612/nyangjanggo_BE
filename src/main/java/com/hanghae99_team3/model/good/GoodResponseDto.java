package com.hanghae99_team3.model.good;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

@Getter
@NoArgsConstructor
public class GoodResponseDto {

    private String username;
    private String userImg;

    public GoodResponseDto(@NotNull Good good) {
        this.username = good.getUser().getUsername();
        this.userImg = good.getUser().getUserImg();
    }
}
