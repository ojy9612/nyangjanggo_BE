package com.hanghae99_team3.model.recipestep;

import com.hanghae99_team3.model.recipestep.dto.RecipeStepRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class RecipeStepService {

    private final RecipeStepRepository recipeStepRepository;

    public void createRecipeStep(List<RecipeStepRequestDto> recipeStepRequestDtoList){

    }

}
