package com.hanghae99_team3.model.board.dto;


import com.hanghae99_team3.model.board.Board;
import com.hanghae99_team3.model.good.Good;
import com.hanghae99_team3.model.good.GoodResponseDto;
import com.hanghae99_team3.model.images.Images;
import com.hanghae99_team3.model.images.ImagesResponseDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class BoardResponseDto {

    private Long boardId;
    private String username;
    private String userImg;
    private String title;
    private String subTitle;
    private String content;
    private List<ImagesResponseDto> imgList;
    private int commentCount;
    private int goodCount;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;


    private List<ImagesResponseDto> makeImagesList(List<Images> imagesList){

        List<ImagesResponseDto> imagesResponseDtoList = new ArrayList<>();

        imagesList.forEach(images -> imagesResponseDtoList.add(new ImagesResponseDto(images)));

        return imagesResponseDtoList;
    }

    public BoardResponseDto(Board board) {
        this.boardId = board.getId();
        this.username = board.getUser().getUsername();
        this.userImg = board.getUser().getUserImg();
        this.title = board.getTitle();
        this.subTitle = board.getSubTitle();
        this.content = board.getContent();
        this.imgList = makeImagesList(board.getImagesList());
        this.commentCount = board.getCommentList().size();
        this.goodCount = board.getGoodList().size();
        this.createdAt = board.getCreatedAt();
        this.modifiedAt = board.getModifiedAt();
    }

}
