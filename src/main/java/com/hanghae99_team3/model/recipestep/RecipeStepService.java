package com.hanghae99_team3.model.recipestep;

import com.hanghae99_team3.model.board.domain.Board;
import com.hanghae99_team3.model.recipestep.dto.RecipeStepRequestDto;
import com.hanghae99_team3.model.s3.AwsS3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
@Service
public class RecipeStepService {

    private final RecipeStepRepository recipeStepRepository;

    public void createRecipeStep(List<RecipeStepRequestDto> recipeStepRequestDtoList,
                                 Board board) {

        recipeStepRequestDtoList.forEach(recipeStepRequestDto -> {
            RecipeStep recipeStep = RecipeStep.builder()
                    .recipeStepRequestDto(recipeStepRequestDto)
                    .board(board)
                    .build();

            recipeStepRepository.save(recipeStep);
        });
    }

    public void updateRecipeStep(List<RecipeStepRequestDto> recipeStepRequestDtoList, Board board){
        this.removeAllRecipeStep(board);
        this.createRecipeStep(recipeStepRequestDtoList,board);
    }


    public void removeAllRecipeStep(Board board) {
        List<RecipeStep> recipeStepList = recipeStepRepository.findAllByBoard(board);
        recipeStepRepository.deleteAll(recipeStepList);
    }
}
