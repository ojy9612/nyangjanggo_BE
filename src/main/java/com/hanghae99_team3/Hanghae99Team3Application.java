package com.hanghae99_team3;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.annotation.PostConstruct;
import java.util.TimeZone;

@EnableJpaAuditing
@EnableScheduling
@EnableBatchProcessing
@EnableCaching
@SpringBootApplication
public class Hanghae99Team3Application {

    public static void main(String[] args) {
        SpringApplication.run(Hanghae99Team3Application.class, args);
    }

    @PostConstruct
    public void started() { // db 시간 맞추기
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"));
    }

}
