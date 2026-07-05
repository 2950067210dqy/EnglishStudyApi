package com.dqy.englishstudyapi.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dqy.englishstudyapi.entity.frontEntity.ImportWordsEntity;
import com.dqy.englishstudyapi.service.CikuService;
import com.dqy.englishstudyapi.tablebean.Ciku;
import com.dqy.englishstudyapi.util.DynamicTableNameUtil;
import com.dqy.englishstudyapi.util.JsonUtil;
import com.dqy.englishstudyapi.util.ListUtil;
import com.dqy.englishstudyapi.util.TimeUtil;
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
    @Autowired
    TimeUtil timeUtil;
    @Autowired
    DynamicTableNameUtil dynamicTableNameUtil;
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


    @RequestMapping(value = "/shoucang",method = RequestMethod.POST)
    public ReturnVO shoucang(@RequestParam("uid")  Integer uid,@RequestParam("cikutypeid")  Integer cikuTypeId,@RequestParam("cikuid") Integer cikuId){
        returnVO = new ReturnVO();
        if (uid==null||cikuTypeId==null||cikuId==null){
            returnVO.setCode(500);
            returnVO.setMessage("数据为空");
            return  returnVO;
        }else{
            Ciku shoucang = new Ciku();
            shoucang.setUid(uid);
            shoucang.setDsc(String.valueOf(cikuTypeId));
            shoucang.setDscabb(String.valueOf(cikuId));
            shoucang.setCreatetime(timeUtil.getCurrentTimeLocalDateTime());
            cikuService.createTable(cikuTypeId);
            boolean result = cikuService.shouCang(shoucang);
            if(result){
                returnVO.setCode(200);
                returnVO.setMessage("收藏成功");
            }else{
                returnVO.setCode(500);
                returnVO.setMessage("收藏失败");
            }
        }
        return  returnVO;
    }

    @RequestMapping(value = "/shoucangCancel",method = RequestMethod.POST)
    public ReturnVO shoucangCancel(@RequestParam("uid")  Integer uid,@RequestParam("cikutypeid")  Integer cikuTypeId,@RequestParam("cikuid") Integer cikuId){
        returnVO = new ReturnVO();
        if (uid==null||cikuTypeId==null||cikuId==null){
            returnVO.setCode(500);
            returnVO.setMessage("数据为空");
            return  returnVO;
        }else{
            Ciku shoucang = new Ciku();
            shoucang.setUid(uid);
            shoucang.setDsc(String.valueOf(cikuTypeId));
            shoucang.setDscabb(String.valueOf(cikuId));
            cikuService.createTable(cikuTypeId);
            boolean result=cikuService.shouCangCancel(shoucang);
            if(result){
                returnVO.setCode(200);
                returnVO.setMessage("取消收藏成功");
            }else{
                returnVO.setCode(500);
                returnVO.setMessage("取消收藏失败");
            }
        }
        return  returnVO;
    }

    @RequestMapping(value = "/selectByDsc",method = RequestMethod.POST)
    public ReturnVO selectByDsc(@RequestParam("cikuTypeId")  Integer cikuTypeId,@RequestParam("dsc") String dsc){
        returnVO = new ReturnVO();
        Ciku ciku =cikuService.selectByDsc(cikuTypeId,dsc);
        if (ciku!=null){
            returnVO.setCode(200);
            returnVO.setMessage("存在该词库");
            returnVO.setData(ciku);
        }else {
            returnVO.setCode(500);
            returnVO.setMessage("不存在该词库");
        }
        return  returnVO;
    }
    @RequestMapping(value = "/selectById",method = RequestMethod.POST)
    public ReturnVO selectById(@RequestParam("cikuTypeId")  Integer cikuTypeId,@RequestParam("cikuId") Integer cikuId){
        returnVO = new ReturnVO();
        if (cikuTypeId==null||cikuId==null){
            returnVO.setCode(500);
            returnVO.setMessage("数据为空");
            return  returnVO;
        }
        cikuService.createTable(cikuTypeId);
        dynamicTableNameUtil.SetTableName("ciku","_"+cikuTypeId.toString());
        Ciku ciku =cikuService.getById(cikuId);
        if (ciku!=null){
            returnVO.setCode(200);
            returnVO.setMessage("存在该词库");
            returnVO.setData(ciku);
        }else {
            returnVO.setCode(500);
            returnVO.setMessage("不存在该词库");
        }
        return  returnVO;
    }
}
