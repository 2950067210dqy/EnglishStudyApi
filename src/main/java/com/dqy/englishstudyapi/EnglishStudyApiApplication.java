package com.dqy.englishstudyapi;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.dqy.englishstudyapi.mapper")
@SpringBootApplication
public class EnglishStudyApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(EnglishStudyApiApplication.class, args);
    }

}
