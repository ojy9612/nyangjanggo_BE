package com.hanghae99_team3.model.board.dto;


import com.hanghae99_team3.model.board.Board;
import com.hanghae99_team3.model.good.Good;
import com.hanghae99_team3.model.good.GoodResponseDto;
import com.hanghae99_team3.model.images.Images;
import com.hanghae99_team3.model.images.ImagesResponseDto;
import com.hanghae99_team3.model.recipestep.dto.RecipeStepResponseDto;
import com.hanghae99_team3.model.resource.dto.ResourceResponseDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
public class BoardResponseDto {

    private Long boardId;
    private String nickname;
    private String userImg;
    private String title;
    private String subTitle;
    private String content;
    private String mainImg;
    private int commentCount;
    private int goodCount;
    private List<ResourceResponseDto> resourceResponseDtoList;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;


    public BoardResponseDto(Board board) {
        this.boardId = board.getId();
        this.nickname = board.getUser().getNickname();
        this.userImg = board.getUser().getUserImg();
        this.title = board.getTitle();
        this.subTitle = board.getSubTitle();
        this.content = board.getContent();
        this.mainImg = board.getMainImage();
        this.commentCount = board.getCommentList().size();
        this.goodCount = board.getGoodList().size();
        this.resourceResponseDtoList = board.getResourceList().stream().map(ResourceResponseDto::new).collect(Collectors.toList());
        this.createdAt = board.getCreatedAt();
        this.modifiedAt = board.getModifiedAt();
    }

}
