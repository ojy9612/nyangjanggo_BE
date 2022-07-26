package com.hanghae99_team3.scheduler;

import com.hanghae99_team3.model.board.domain.Board;
import com.hanghae99_team3.model.board.repository.BoardRepository;
import com.hanghae99_team3.model.images.Images;
import com.hanghae99_team3.model.images.ImagesRepository;
import com.hanghae99_team3.model.recipestep.RecipeStep;
import com.hanghae99_team3.model.recipestep.RecipeStepRepository;
import com.hanghae99_team3.model.s3.AwsS3Service;
import com.hanghae99_team3.model.user.domain.User;
import com.hanghae99_team3.model.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class SchedulerTest {

    @Autowired BoardRepository boardRepository;
    @Autowired ImagesRepository imagesRepository;
    @Autowired UserRepository userRepository;
    @Autowired AwsS3Service awsS3Service;

    @TestConfiguration
    static class config {

        @Bean
        ServletWebServerApplicationContext webServerAppCtxt(){
            return new ServletWebServerApplicationContext();
        }
    }

    @Test
    void deleteDeadImage() {

        List<String> allObject = awsS3Service.getAllObject().get(0);
        List<String> imageLinkList = new ArrayList<>();

        allObject.forEach(imageLink -> {
            Optional<Images> optionalImages1 = imagesRepository.findByImageLink(imageLink);
            Optional<User> optionalImages2 = userRepository.findByUserImg(imageLink);

            if (optionalImages1.isEmpty() && optionalImages2.isEmpty()) {
                imageLinkList.add(imageLink);
            }
        });
        System.out.println(imageLinkList.size());

        awsS3Service.deleteAllFile(imageLinkList);

    }

}