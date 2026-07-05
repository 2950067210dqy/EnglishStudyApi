package com.dqy.englishstudyapi.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dqy.englishstudyapi.entity.frontEntity.ReadFront.ReadFront;
import com.dqy.englishstudyapi.entity.frontEntity.ReadSimple.ReadSimple;
import com.dqy.englishstudyapi.service.ReadService;
import com.dqy.englishstudyapi.tablebean.Read;
import com.dqy.englishstudyapi.util.*;
import com.dqy.englishstudyapi.vo.ReturnVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 邓亲优
 * @since 2023-03-02
 */
@RestController
@RequestMapping("read")
public class ReadController {
    @Autowired
    ReadService readService;
    @Autowired
    TimeUtil timeUtil;
    @Autowired
    JsonUtil jsonUtil;
    @Autowired
    Base64Util base64Util;
    @Autowired
    WordUtil wordUtil;
    @Autowired
    DynamicTableNameUtil dynamicTableNameUtil;
    ReturnVO returnVO;


    @PostMapping("/getById")
    public ReturnVO getById(@RequestParam("rid")Integer rid, @RequestParam("rsid")Integer rsid,@RequestParam("id")Integer id){
        returnVO = new ReturnVO();
        if (rid==null||rsid==null||id==null){
            returnVO.setCode(500);
            returnVO.setMessage("数据为空");
            return returnVO;
        }else{
            readService.createTable(rid,rsid);
            dynamicTableNameUtil.SetTableName("read","_"+rid+"_"+rsid);
            Read read =   readService.getById(id);
            if (read !=null){

                List<String> sentences = wordUtil.spilitSentence(base64Util.decodeToString(read.getEssay()));

                ReadFront readFront = new ReadFront();
                readFront.setId(read.getId());
                readFront.setBrief(read.getBrief());
                readFront.setAuthor(read.getAuthor());
                readFront.setImage(read.getImage());
                readFront.setDeleted(read.getDeleted());
                readFront.setCreatetime(read.getCreatetime());
                readFront.setName(read.getName());
                readFront.setSentences(sentences);
                readFront.setUpdatetime(read.getUpdatetime());


                returnVO.setCode(200);
                returnVO.setMessage("获取成功");
                returnVO.setData(jsonUtil.parseObjectToJsonStrThenToBase64(readFront));
                return returnVO;
            }else{
                returnVO.setCode(500);
                returnVO.setMessage("获取失败或为空");
                return returnVO;
            }

        }

    }
    @PostMapping("/getSimple")
    public ReturnVO getSimple(@RequestParam("rid")Integer rid, @RequestParam("rsid")Integer rsid){
        returnVO = new ReturnVO();
        if (rid==null||rsid==null){
            returnVO.setCode(500);
            returnVO.setMessage("数据为空");
            return returnVO;
        }else{
            readService.createTable(rid,rsid);

            List<ReadSimple> readSimpleList =   readService.selectSimple(rid,rsid);
            if (readSimpleList!=null&&readSimpleList.size()!=0){
                returnVO.setCode(200);
                returnVO.setMessage("获取成功");
                returnVO.setData(readSimpleList);
                return returnVO;
            }else{
                returnVO.setCode(500);
                returnVO.setMessage("获取失败或为空");
                return returnVO;
            }

        }

    }

}
