package com.hanghae99_team3.model.board.dto.response;


import com.hanghae99_team3.model.board.domain.Board;
import com.hanghae99_team3.model.comment.dto.CommentResponseDto;
import com.hanghae99_team3.model.good.GoodResponseDto;
import com.hanghae99_team3.model.recipestep.dto.RecipeStepResponseDto;
import com.hanghae99_team3.model.resource.dto.ResourceResponseDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
public class BoardDetailResponseDto {

    private Long boardId;
    private String status;
    private String nickname;
    private String userImg;
    private String title;
    private String content;
    private String mainImg;
    private List<ResourceResponseDto> resourceResponseDtoList;
    private List<RecipeStepResponseDto> recipeStepResponseDtoList;
    private List<GoodResponseDto> goodList;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;


    public BoardDetailResponseDto(Board board) {
        this.boardId = board.getId();
        this.status = board.getStatus();
        this.nickname = board.getUser().getNickname();
        this.userImg = board.getUser().getUserImg();
        this.title = board.getTitle();
        this.content = board.getContent();
        this.mainImg = board.getMainImageLink();
        this.resourceResponseDtoList = board.getResourceList().stream().map(ResourceResponseDto::new).collect(Collectors.toList());
        this.recipeStepResponseDtoList = board.getRecipeStepList().stream().map(RecipeStepResponseDto::new).collect(Collectors.toList());
        this.goodList = board.getGoodList().stream().map(GoodResponseDto::new).collect(Collectors.toList());
        this.createdAt = board.getCreatedAt();
        this.modifiedAt = board.getModifiedAt();
    }

}
