package com.hanghae99_team3.model.recipestep.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
public class RecipeStepRequestDto {
    private Integer stepNum;
    private String stepContent;
    private String imageLink;

    @Builder
    public RecipeStepRequestDto(@NotNull Integer stepNum,
                                @NotNull String stepContent,
                                String imageLink) {
        this.stepNum = stepNum;
        this.stepContent = stepContent;
        this.imageLink = imageLink;
    }
}
