package com.hanghae99_team3.model.user.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class UserReqDto {
    private String username;
    private List<MultipartFile> userImg;
    private String userDescription;
    private String imgUrl;
}
