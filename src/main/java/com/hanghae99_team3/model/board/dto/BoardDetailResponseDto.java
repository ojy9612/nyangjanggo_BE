package com.hanghae99_team3.model.board.dto;


import com.hanghae99_team3.model.board.Board;
import com.hanghae99_team3.model.comment.Comment;
import com.hanghae99_team3.model.comment.dto.CommentResponseDto;
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
public class BoardDetailResponseDto {

    private Long boardId;
    private String username;
    private String userImg;
    private String title;
    private String subTitle;
    private String content;
    private List<CommentResponseDto> commentList;
    private List<GoodResponseDto> goodList;
    private List<ImagesResponseDto> imgList;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    private List<CommentResponseDto> makeCommentList(List<Comment> commentList){

        List<CommentResponseDto> commentResponseDtoList = new ArrayList<>();

        commentList.forEach(comment -> commentResponseDtoList.add(new CommentResponseDto(comment)));

        return commentResponseDtoList;
    }

    private List<GoodResponseDto> makeGoodList(List<Good> goodList){

        List<GoodResponseDto> goodResponseDtoList = new ArrayList<>();

        goodList.forEach(good -> goodResponseDtoList.add(new GoodResponseDto(good)));

        return goodResponseDtoList;
    }

    private List<ImagesResponseDto> makeImagesList(List<Images> imagesList){

        List<ImagesResponseDto> imagesResponseDtoList = new ArrayList<>();

        imagesList.forEach(images -> imagesResponseDtoList.add(new ImagesResponseDto(images)));

        return imagesResponseDtoList;
    }

    public BoardDetailResponseDto(Board board) {
        this.boardId = board.getId();
        this.username = board.getUser().getUsername();
        this.userImg = board.getUser().getUserImg();
        this.title = board.getTitle();
        this.subTitle = board.getSubTitle();
        this.content = board.getContent();
        this.commentList = makeCommentList(board.getCommentList());
        this.goodList = makeGoodList(board.getGoodList());
        this.imgList = makeImagesList(board.getImagesList());
        this.createdAt = board.getCreatedAt();
        this.modifiedAt = board.getModifiedAt();
    }

}
