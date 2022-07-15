package com.hanghae99_team3.model.recipestep;


import com.hanghae99_team3.model.Timestamped;
import com.hanghae99_team3.model.board.domain.Board;
import com.hanghae99_team3.model.recipestep.dto.RecipeStepRequestDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Getter
@Entity
@NoArgsConstructor
public class RecipeStep extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @Column
    private Integer stepNum;
    @Column
    private String content;
    @Column
    private String imageLink;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    private Board board;

    @Builder
    public RecipeStep(@NotNull RecipeStepRequestDto recipeStepRequestDto,
                      String imageLink,
                      @NotNull Board board) {
        this.stepNum = recipeStepRequestDto.getStepNum();
        this.content = recipeStepRequestDto.getStepContent();
        this.imageLink = imageLink;
        board.addRecipeStep(this);
    }

    public void updateRecipeStep(@NotNull RecipeStepRequestDto recipeStepRequestDto,
                                 String imageLink){
        this.stepNum = recipeStepRequestDto.getStepNum();
        this.content = recipeStepRequestDto.getStepContent();
        this.imageLink = imageLink;
    }

}
