package com.dqy.englishstudyapi;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@MapperScan("com.dqy.englishstudyapi.mapper")
@EnableSwagger2
@SpringBootApplication
public class EnglishStudyApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(EnglishStudyApiApplication.class, args);
    }

}
