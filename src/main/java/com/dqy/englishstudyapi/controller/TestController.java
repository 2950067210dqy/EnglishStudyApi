package com.dqy.englishstudyapi.controller;


import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;


@RestController
@RequestMapping("test")
@Api(tags = "测试接口",description = "测试用")
public class TestController {



    @RequestMapping("/hello")
    public String hello(){

        return "123";
    }
}
