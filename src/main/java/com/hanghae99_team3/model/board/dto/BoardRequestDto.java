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
    private String content;
    private List<MultipartFile> imgFile;

    public BoardRequestDto(@NotNull String title,@NotNull String content, List<MultipartFile> imgFile) {
        this.title = title;
        this.content = content;
        this.imgFile = imgFile;
    }

}
