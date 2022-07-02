package com.hanghae99_team3.model.board.dto;


import com.hanghae99_team3.model.recipestep.dto.RecipeStepRequestDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
public class BoardRequestDtoStepRecipe {

    private Long boardId;

    private RecipeStepRequestDto recipeStepRequestDto;

    @Builder
    public BoardRequestDtoStepRecipe(@NotNull Long boardId,
                                     @NotNull RecipeStepRequestDto recipeStepRequestDto) {
        this.boardId = boardId;
        this.recipeStepRequestDto = recipeStepRequestDto;
    }

}
