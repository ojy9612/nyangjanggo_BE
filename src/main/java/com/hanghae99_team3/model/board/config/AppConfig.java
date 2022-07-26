package com.hanghae99_team3.model.board.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public SaveGoodCount saveGoodCount(){
        return new SaveGoodCount();
    }

}
