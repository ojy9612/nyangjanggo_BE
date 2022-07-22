package com.hanghae99_team3.model.board.dto.request;

import com.hanghae99_team3.model.recipestep.dto.RecipeStepRequestDto;
import com.hanghae99_team3.model.resource.dto.ResourceRequestDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class BoardRequestDto {

    private String title;
    private String content;
    private String mainImageLink;
    private List<ResourceRequestDto> resourceRequestDtoList;
    private List<RecipeStepRequestDto> recipeStepRequestDtoList;

    @Builder
    public BoardRequestDto(String title,
                           String content,
                           String mainImageLink,
                           List<ResourceRequestDto> resourceRequestDtoList,
                           List<RecipeStepRequestDto> recipeStepRequestDtoList) {
        this.title = title;
        this.content = content;
        this.mainImageLink = mainImageLink;
        this.resourceRequestDtoList = resourceRequestDtoList;
        this.recipeStepRequestDtoList = recipeStepRequestDtoList;
    }
}
