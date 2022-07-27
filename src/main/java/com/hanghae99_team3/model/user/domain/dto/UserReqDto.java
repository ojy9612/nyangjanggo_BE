package com.hanghae99_team3.model.user.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserReqDto {
    private String nickname;
    private String userDescription;

    // 새로운 image를 S3에 저장하고 나서 나오는 imgUrl
    private String imgUrl;
}
