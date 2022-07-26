package com.hanghae99_team3.scheduler;

import com.hanghae99_team3.model.board.repository.BoardRepository;
import com.hanghae99_team3.model.images.Images;
import com.hanghae99_team3.model.images.ImagesRepository;
import com.hanghae99_team3.model.recipestep.RecipeStepRepository;
import com.hanghae99_team3.model.s3.AwsS3Service;
import com.hanghae99_team3.model.user.domain.User;
import com.hanghae99_team3.model.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class Scheduler {

    private final UserRepository userRepository;
    private final ImagesRepository imagesRepository;
    private final AwsS3Service awsS3Service;
    private final ServletWebServerApplicationContext webServerAppCtxt;
    /**
     * Cron 표현식을 사용한 작업 예약
     * 초(0-59) 분(0-59) 시간(0-23) 일(1-31) 월(1-12) 요일(0-7)
     * 매일 오전 6시 00분에 실행
     */
    @Scheduled(cron = "0 0 6 * * ?", zone = "Asia/Seoul")
    @Transactional(readOnly = true)
    public void deleteDeadImage() {

        Integer port = webServerAppCtxt.getWebServer().getPort();

        try (BufferedReader reader = new BufferedReader(
                new FileReader("/home/ec2-user/service_url.inc")
        )) {

            String str = reader.readLine();
            str = str.substring(str.length() - 5).substring(0, 4);

            Integer processingPort = Integer.valueOf(str);

            if (processingPort.equals(port)) {
                List<String> allObject = awsS3Service.getAllObject().get(0);
                List<String> imageLinkList = new ArrayList<>();

                allObject.forEach(imageLink -> {
                    Optional<Images> optionalImages1 = imagesRepository.findByImageLink(imageLink);
                    Optional<User> optionalImages2 = userRepository.findByUserImg(imageLink);

                    if (optionalImages1.isEmpty() && optionalImages2.isEmpty()) {
                        imageLinkList.add(imageLink);
                    }
                });
                awsS3Service.deleteAllFile(imageLinkList);
                log.info(imageLinkList.size() + "개의 이미지를 삭제했습니다.");
            }

        } catch (IOException | NumberFormatException e) {
            throw new RuntimeException(e);
        }

    }
}

