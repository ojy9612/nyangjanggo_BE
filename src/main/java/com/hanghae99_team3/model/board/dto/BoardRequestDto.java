package com.hanghae99_team3.model.board.dto;


import lombok.*;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class BoardRequestDto {

    private String title;
    private String subTitle;
    private String content;
    private List<MultipartFile> imgFileList;

    // 이미지는 없어도 전송할 수 있지만 비어있는 값이 와야하므로(List size 는 1)
    // List<MultipartFile>는 null 이 되면 안됨.
    public BoardRequestDto(@NotNull String title,
                           @NotNull String subTitle,
                           @NotNull String content,
                           @NotNull List<MultipartFile> imgFile) {
        this.title = title;
        this.subTitle = subTitle;
        this.content = content;
        this.imgFileList = imgFile;
    }

}
