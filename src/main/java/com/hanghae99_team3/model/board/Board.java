package com.hanghae99_team3.model.board;


import com.hanghae99_team3.model.Timestamped;
import com.sun.istack.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;


@Getter
@Entity
@Table(name = "boards")
@NoArgsConstructor
public class Board extends Timestamped {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String content;

    private String imgLink;
    private String imgKey;

    @Builder
    public Board(@NotNull String title, @NotNull String content, String imgLink, String imgKey) {
        this.title = title;
        this.content = content;
        this.imgLink = imgLink;
        this.imgKey = imgKey;
    }
    public void update(BoardRequestDto boardRequestDto){
        this.title = boardRequestDto.getTitle();
        this.content = boardRequestDto.getContent();
        this.imgLink = boardRequestDto.getImgLink();
        this.imgKey = boardRequestDto.getImgKey();

    }
}

