//Test
package com.hanghae99_team3.model.user.dto;

import com.hanghae99_team3.model.user.domain.UserRole;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserInfoDto {
    String username;
    UserRole role;
}

