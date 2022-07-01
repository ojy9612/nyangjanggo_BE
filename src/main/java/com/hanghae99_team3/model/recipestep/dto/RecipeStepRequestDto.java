package com.hanghae99_team3.model.recipestep.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
public class RecipeStepRequestDto {
    private Integer stepNum;
    private String stepContent;
    private MultipartFile image;

    @Builder
    public RecipeStepRequestDto(@NotNull Integer stepNum, @NotNull String stepContent, MultipartFile image) {
        this.stepNum = stepNum;
        this.stepContent = stepContent;
        this.image = image;
    }
}
