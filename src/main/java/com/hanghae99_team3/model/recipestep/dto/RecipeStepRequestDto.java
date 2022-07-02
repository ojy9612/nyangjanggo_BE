package com.hanghae99_team3.model.recipestep.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
public class RecipeStepRequestDto {
    private Integer stepNum;
    private String stepContent;

    @Builder
    public RecipeStepRequestDto(@NotNull Integer stepNum, @NotNull String stepContent) {
        this.stepNum = stepNum;
        this.stepContent = stepContent;
    }
}
