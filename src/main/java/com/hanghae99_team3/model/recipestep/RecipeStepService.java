package com.hanghae99_team3.model.recipestep;

import com.hanghae99_team3.model.board.domain.Board;
import com.hanghae99_team3.model.recipestep.dto.RecipeStepRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class RecipeStepService {

    private final RecipeStepRepository recipeStepRepository;

    public void createRecipeStep(List<RecipeStepRequestDto> recipeStepRequestDtoList,
                                 Board board) {

        List<RecipeStep> recipeStepList = new ArrayList<>();
        recipeStepRequestDtoList.forEach(recipeStepRequestDto -> {
            RecipeStep recipeStep = RecipeStep.builder()
                    .recipeStepRequestDto(recipeStepRequestDto)
                    .board(board)
                    .build();

            recipeStepList.add(recipeStep);
        });
        recipeStepRepository.saveAll(recipeStepList);
    }

    public void createRecipeStepTest(List<RecipeStepRequestDto> recipeStepRequestDtoList,
                                     Board board) {

        recipeStepRequestDtoList.forEach(recipeStepRequestDto -> {
            RecipeStep recipeStep = RecipeStep.builder()
                    .recipeStepRequestDto(recipeStepRequestDto)
                    .board(board)
                    .build();

            recipeStepRepository.save(recipeStep);
        });
    }

    // 사용자가 내용을 한번에 수정할 가능성이 높으므로 항상 데이터를 List 로 받게 함
    public void updateRecipeStep(List<RecipeStepRequestDto> recipeStepRequestDtoList, Board board) {
        this.removeAllRecipeStep(board);
        this.createRecipeStep(recipeStepRequestDtoList, board);
    }


    public void removeAllRecipeStep(Board board) {
        List<RecipeStep> recipeStepList = recipeStepRepository.findAllByBoard(board);
        recipeStepRepository.deleteAll(recipeStepList);
    }
}
