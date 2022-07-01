package com.hanghae99_team3.model.board.dto;


import com.hanghae99_team3.model.recipestep.dto.RecipeStepRequestDto;
import com.hanghae99_team3.model.resource.dto.ResourceRequestDto;
import lombok.*;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class BoardRequestDto {

    private String title;
    private String subTitle;
    private MultipartFile frontImageLink;
    private String content;

    private List<ResourceRequestDto> resourceRequestDtoList;
    private List<RecipeStepRequestDto> recipeStepRequestDtoList;
    private List<MultipartFile> imgFileList;

    /*
    이미지는 없어도 전송할 수 있지만 비어있는 값이 와야함.(List size 는 1)
    그러므로 List<MultipartFile>는 null 이 되면 안됨.
    */
    @Builder
    public BoardRequestDto(@NotNull String title,
                           @NotNull String subTitle,
                           @NotNull MultipartFile frontImageLink,
                           @NotNull String content,
                           @NotNull List<ResourceRequestDto> resourceRequestDtoList,
                           @NotNull List<RecipeStepRequestDto> recipeStepRequestDtoList,
                           @NotNull List<MultipartFile> imgFile) {
        this.title = title;
        this.subTitle = subTitle;
        this.frontImageLink = frontImageLink;
        this.content = content;
        this.resourceRequestDtoList = resourceRequestDtoList;
        this.recipeStepRequestDtoList = recipeStepRequestDtoList;
        this.imgFileList = imgFile;
    }

}
