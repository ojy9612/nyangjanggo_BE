package com.hanghae99_team3.model.board.dto;


import com.sun.istack.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class BoardRequestDto {

    private Long userId;
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
