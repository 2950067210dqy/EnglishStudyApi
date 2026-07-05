package com.dqy.englishstudyapi.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dqy.englishstudyapi.service.PtypeService;
import com.dqy.englishstudyapi.tablebean.Ptype;
import com.dqy.englishstudyapi.util.TimeUtil;
import com.dqy.englishstudyapi.vo.ReturnVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 邓亲优
 * @since 2023-02-21
 */
@RestController
@RequestMapping("ptype")
public class PtypeController {

    @Autowired
    PtypeService ptypeService;
    @Autowired
    TimeUtil timeUtil;
    ReturnVO returnVO;

    @PostMapping("/get")
    public ReturnVO get(){
        returnVO = new ReturnVO();
       ArrayList<Ptype> ptypes = (ArrayList<Ptype>) ptypeService.list();
        if (ptypes!=null&&ptypes.size()!=0){
            returnVO.setCode(200);
            returnVO.setMessage("获取成功");
            returnVO.setData(ptypes);
            return returnVO;
        }else{
            returnVO.setCode(500);
            returnVO.setMessage("获取失败");
            return returnVO;
        }
    }
    @PostMapping("/getRepeat")
    public ReturnVO getRepeat(@RequestParam("type")String type){
        returnVO = new ReturnVO();
        Ptype ptypes =  ptypeService.getOne(new QueryWrapper<Ptype>().eq("dsc",type));
        if (ptypes!=null){
            returnVO.setCode(200);
            returnVO.setMessage("获取成功");
            return returnVO;
        }else{
            returnVO.setCode(500);
            returnVO.setMessage("获取失败");
            return returnVO;
        }
    }

    @PostMapping("/set")
    public ReturnVO set(@RequestParam("type")String type){
        returnVO = new ReturnVO();
        Ptype ptype = new Ptype();
        ptype.setCreatetime(timeUtil.getCurrentTimeLocalDateTime());
        ptype.setDeleted(0);
        ptype.setDsc(type);
        boolean result = ptypeService.save(ptype);
        if (result){
            returnVO.setCode(200);
            returnVO.setMessage("存储成功");
            return returnVO;
        }else{
            returnVO.setCode(500);
            returnVO.setMessage("存储失败");
            return returnVO;
        }
    }
}
