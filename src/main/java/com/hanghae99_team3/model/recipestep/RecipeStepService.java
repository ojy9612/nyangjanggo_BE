package com.hanghae99_team3.model.recipestep;

import com.hanghae99_team3.model.board.Board;
import com.hanghae99_team3.model.recipestep.dto.RecipeStepRequestDto;
import com.hanghae99_team3.model.s3.AwsS3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class RecipeStepService {

    private final RecipeStepRepository recipeStepRepository;
    private final AwsS3Service awsS3Service;

    public void createRecipeStep(RecipeStepRequestDto recipeStepRequestDto,
                                 String imageLink,
                                 Board board) {

        RecipeStep recipeStep = RecipeStep.builder()
                .recipeStepRequestDto(recipeStepRequestDto)
                .board(board)
                .imageLink(imageLink)
                .build();

        recipeStepRepository.save(recipeStep);

    }

    public void removeRecipeStep(Board board) {
        List<RecipeStep> recipeStepList = recipeStepRepository.findAllByBoard(board);

        recipeStepList.forEach(recipeStep -> awsS3Service.deleteFile(recipeStep.getImageLink()));

        recipeStepRepository.deleteAll(recipeStepList);
    }
}
