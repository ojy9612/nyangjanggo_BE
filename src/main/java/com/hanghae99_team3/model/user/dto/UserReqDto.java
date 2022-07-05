package com.hanghae99_team3.model.user.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class UserReqDto {
    private String nickname;
    private List<MultipartFile> userImg;
    private String userDescription;

    // 새로운 image를 S3에 저장하고 나서 나오는 imgUrl
    private String imgUrl;
}
