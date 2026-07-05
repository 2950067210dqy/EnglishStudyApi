package com.dqy.englishstudyapi;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@MapperScan("com.dqy.englishstudyapi.mapper")
@EnableSwagger2
//开启事务
@EnableTransactionManagement
@SpringBootApplication
public class EnglishStudyApiApplication  extends SpringBootServletInitializer {
    @Override  //为了打包springboot 项目
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder){
        return builder.sources(this.getClass());
    }
    public static void main(String[] args) {
        SpringApplication.run(EnglishStudyApiApplication.class, args);
    }

}
