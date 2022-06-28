package com.hanghae99_team3.model.board.dto;


import com.hanghae99_team3.model.board.Board;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class BoardResponseDto {

    private Long boardId;
    private String userName;
    private String title;
    private String content;
    private String imgLink;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

//    private Long resourseId;
//    private List<commentsList> comments;
//    private List<likesList> likes;


    public BoardResponseDto(Board board) {
        this.boardId = board.getId();
        this.userName = board.getUser().getUsername();
        this.title = board.getTitle();
        this.content = board.getContent();
        this.imgLink = board.getImgLink();
        this.createdAt = board.getCreatedAt();
        this.modifiedAt = board.getModifiedAt();
    }
}
