package com.dqy.englishstudyapi.configure;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;

/**
 * @Configuration  标记配置类
 * @EnableSwagger2 开启在线接口文档
 */
@Configuration
@EnableSwagger2
public class Swagger2Config {

    /**
     * 全配置好了访问：http://localhost:8085/swagger-ui.html
     * 文档信息设置：名称、描述、联系人、网站、邮箱、版权等
     * 接口过滤设置：根据包名、或请求路径
     */
    @Bean
    public Docket controllerApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(new ApiInfoBuilder()
                        .title("英语学习平台接口文档")
                        .description("测试英语学习后台的所有api")
//                        .termsOfServiceUrl("http:127.0.0.1/")
                        // 此处填自己信息即可
                        .contact(new Contact("dqy","https://www.dengqinyou.cn","2950067210@qq.com"))
                        .version("版本号:1.0")
                        .build())
                .select()
                // apis()通过指定包名的方式，Swagger扫描指定包下面的接口。
                .apis(RequestHandlerSelectors.basePackage("com.dqy.englishstudyapi.controller"))
                // paths()通过指定API的url来进行过滤
                .paths(PathSelectors.any())
                //.paths(Predicates.or(PathSelectors.ant("/account/**"),
                //       PathSelectors.ant("/api/edifice/*")))
                .build();
    }




}