package com.dqy.englishstudyapi.controller;

import com.dqy.englishstudyapi.entity.frontEntity.ImportWordsEntity;
import com.dqy.englishstudyapi.service.CikuService;
import com.dqy.englishstudyapi.util.JsonUtil;
import com.dqy.englishstudyapi.util.ListUtil;
import com.dqy.englishstudyapi.vo.ReturnVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

@RestController
@RequestMapping("ciku")
@Api(tags= "单词操作",description = "操作单词的增删改查等操作")
public class CikuController {


    @Autowired
    CikuService cikuService;
    @Autowired
    JsonUtil jsonUtil;
    ReturnVO returnVO;
    @ApiOperation(value = "导入词库",response = ReturnVO.class,notes = "导入词库")
    @RequestMapping(value = "/import",method = RequestMethod.POST)
    public ReturnVO importWords(@RequestBody  ImportWordsEntity param){
        returnVO = new ReturnVO();
        if (param==null||param.getCikuId()<1||param.getCikuTypeId()<1||param.getWords().size()==0){
            returnVO.setCode(returnVO.PARAM_ERROR);
            returnVO.setMessage(returnVO.PARAM_ERROR_MESSAGE);
            return  returnVO;
        }
        boolean result = cikuService.importWords(param);
        if (result){
            returnVO.setCode(returnVO.OK);
            returnVO.setMessage(returnVO.OK_MESSAGE);
            System.out.println(returnVO.OK_MESSAGE);
        }else{
            returnVO.setCode(returnVO.EXECUTE_ERROR);
            returnVO.setMessage(returnVO.EXECUTE_ERROR_MESSAGE);
        }
        System.out.println(returnVO);
        return  returnVO;
    }
}
