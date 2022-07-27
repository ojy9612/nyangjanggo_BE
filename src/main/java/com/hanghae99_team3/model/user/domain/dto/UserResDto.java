package com.hanghae99_team3.model.user.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserResDto implements Serializable {
    private String nickname;
    private String userImg;
    private String userDescription;
}
