package com.hanghae99_team3.model.board;


import com.sun.istack.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BoardRequestDto {

    private String title;
    private String content;
    private String imgLink;
    private String imgKey;

    @Builder
    public BoardRequestDto(@NotNull String title, @NotNull String content, String imgLink, String imgKey) {
        this.title = title;
        this.content = content;
        this.imgLink = imgLink;
        this.imgKey = imgKey;
    }

}
