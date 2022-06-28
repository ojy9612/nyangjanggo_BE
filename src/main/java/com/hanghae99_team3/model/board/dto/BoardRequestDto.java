package com.hanghae99_team3.model.board.dto;


import com.sun.istack.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@NoArgsConstructor
public class BoardRequestDto {

    private String title;
    private String content;
    private List<MultipartFile> imgFile;


    @Builder
    public BoardRequestDto(@NotNull String title, @NotNull String content, List<MultipartFile> imgFile) {
        this.title = title;
        this.content = content;
        this.imgFile = imgFile;
    }

}
