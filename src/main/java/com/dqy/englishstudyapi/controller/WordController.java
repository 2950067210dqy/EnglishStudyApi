package com.dqy.englishstudyapi.controller;


import com.dqy.englishstudyapi.entity.frontEntity.ImportWordsEntity;
import com.dqy.englishstudyapi.service.WordService;
import com.dqy.englishstudyapi.vo.ReturnVO;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 邓亲优
 * @since 2023-02-03
 */
@RestController
@RequestMapping("word")
public class WordController {
    @Autowired
    WordService wordService;

}
