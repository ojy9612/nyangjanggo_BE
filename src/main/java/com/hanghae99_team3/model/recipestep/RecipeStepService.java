package com.hanghae99_team3.model.recipestep;

import com.hanghae99_team3.model.board.Board;
import com.hanghae99_team3.model.recipestep.dto.RecipeStepRequestDto;
import com.hanghae99_team3.model.s3.AwsS3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
@Service
public class RecipeStepService {

    private final RecipeStepRepository recipeStepRepository;
    private final AwsS3Service awsS3Service;

    public void createRecipeStep(RecipeStepRequestDto recipeStepRequestDto,
                                 MultipartFile multipartFile,
                                 Board board) {

        RecipeStep recipeStep = RecipeStep.builder()
                .recipeStepRequestDto(recipeStepRequestDto)
                .board(board)
                .imageLink(awsS3Service.uploadFile(multipartFile))
                .build();

        recipeStepRepository.save(recipeStep);

    }

    public void updateStep(Board board,
                           RecipeStepRequestDto recipeStepRequestDto,
                           MultipartFile multipartFile){
        List<RecipeStep> recipeStepList = recipeStepRepository.findAllByBoard(board);

        recipeStepList.forEach(recipeStep -> {
            Integer stepNum = recipeStep.getStepNum();
            if (stepNum.equals(recipeStepRequestDto.getStepNum())){

                if(Objects.equals(multipartFile.getOriginalFilename(), "")){
                    recipeStep.updateRecipeStep(recipeStepRequestDto, recipeStep.getImageLink());
                }else{
                    awsS3Service.deleteFile(recipeStep.getImageLink());
                    recipeStep.updateRecipeStep(recipeStepRequestDto, awsS3Service.uploadFile(multipartFile));
                }

            }

        });
    }

    public void removeAndResortRecipeStep(Board board, Integer step){
        List<RecipeStep> recipeStepList = recipeStepRepository.findAllByBoard(board);

        recipeStepList.forEach(recipeStep -> {
            Integer stepNum = recipeStep.getStepNum();
            if (stepNum.equals(step)){
                awsS3Service.deleteFile(recipeStep.getImageLink());
                recipeStepRepository.delete(recipeStep);
            }
            else if (stepNum > step){
                recipeStep.setStepNum(stepNum - 1);
            }
        });
    }

    public void removeAllRecipeStep(Board board) {
        List<RecipeStep> recipeStepList = recipeStepRepository.findAllByBoard(board);

        recipeStepList.forEach(recipeStep -> awsS3Service.deleteFile(recipeStep.getImageLink()));

        recipeStepRepository.deleteAll(recipeStepList);
    }
}
