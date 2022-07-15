package com.hanghae99_team3.scheduler;

import com.hanghae99_team3.model.board.repository.BoardRepository;
import com.hanghae99_team3.model.images.ImagesRepository;
import com.hanghae99_team3.model.recipestep.RecipeStepRepository;
import com.hanghae99_team3.model.s3.AwsS3Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class Scheduler {

    private final BoardRepository boardRepository;
    private final ImagesRepository imagesRepository;
    private final RecipeStepRepository recipeStepRepository;
    private final AwsS3Service awsS3Service;

    /**
     * Cron 표현식을 사용한 작업 예약
     * 초(0-59) 분(0-59) 시간(0-23) 일(1-31) 월(1-12) 요일(0-7)
     * 매일 오전 6시 00분에 실행
     */
    @Scheduled(cron = "0 0 6 * * ?", zone = "Asia/Seoul")
    @Transactional(readOnly = true)
    public void scheduleTaskUsingCronExpression() {
//        List<String> allObject = awsS3Service.getAllObject().get(0);
//
//        allObject.forEach(imageLink -> {
//            Optional<Images> optionalImages1 = imagesRepository.findByImageLink(imageLink);
//            Optional<Board> optionalImages2 = boardRepository.findByMainImage(imageLink);
//            Optional<RecipeStep> optionalImages3 = recipeStepRepository.findByImageLink(imageLink);
//
//            if (optionalImages1.isEmpty() && optionalImages2.isEmpty() && optionalImages3.isEmpty()) {
//                awsS3Service.deleteFile(imageLink);
//            }
//        });
    }
}

