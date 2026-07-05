package com.dqy.englishstudyapi.controller;


import com.dqy.englishstudyapi.entity.frontEntity.TestTypeFront;
import com.dqy.englishstudyapi.helper.RequestDataHelper;
import com.dqy.englishstudyapi.service.TestService;
import com.dqy.englishstudyapi.service.TesttypeService;
import com.dqy.englishstudyapi.tablebean.Test;
import com.dqy.englishstudyapi.tablebean.Testtype;
import com.dqy.englishstudyapi.util.TimeUtil;
import com.dqy.englishstudyapi.vo.ReturnVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 邓亲优
 * @since 2023-02-28
 */
@RestController
@RequestMapping("testtype")
public class TesttypeController {
    @Autowired
    TesttypeService testtypeService;
    @Autowired
    TestService testService;
    @Autowired
    TimeUtil timeUtil;
    ReturnVO returnVO;
    @PostMapping("/get")
    public ReturnVO get(){
        returnVO = new ReturnVO();
        Map<String,Object> params = new HashMap<>();

        List<TestTypeFront> testTypeFronts = new ArrayList<>();

        List<Testtype> testtypeList =testtypeService.list();
        for (Testtype t:testtypeList
        ) {
            TestTypeFront testTypeFront = new TestTypeFront();
            Integer count =testService.count(t.getId());
            testTypeFront.setTesttype(t);
            testTypeFront.setCount(count);
            testTypeFronts.add(testTypeFront);
        }
        if ( testTypeFronts!=null){
            returnVO.setData(testTypeFronts);
            returnVO.setCode(200);
            returnVO.setMessage("获取成功");
            return returnVO;
        }else{
            returnVO.setCode(500);
            returnVO.setMessage("获取失败");
            return returnVO;
        }
    }
}
