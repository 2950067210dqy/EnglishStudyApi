package com.dqy.englishstudyapi.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dqy.englishstudyapi.service.ReadtypeService;
import com.dqy.englishstudyapi.tablebean.Readtype;
import com.dqy.englishstudyapi.util.JsonUtil;
import com.dqy.englishstudyapi.util.TimeUtil;
import com.dqy.englishstudyapi.vo.ReturnVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
@RequestMapping("readtype")
public class ReadtypeController {
    @Autowired
    ReadtypeService readtypeService;
    @Autowired
    TimeUtil timeUtil;
    @Autowired
    JsonUtil jsonUtil;

    ReturnVO returnVO;

    @PostMapping("/getExist")
    public ReturnVO getExist(@RequestParam("dsc") String dsc){
        returnVO = new ReturnVO();
        if (dsc==null||dsc.equals("")){
            returnVO.setCode(500);
            returnVO.setMessage("数据为空");
            return returnVO;
        }else{
            Readtype readtype = readtypeService.getOne(new QueryWrapper<Readtype>().eq("dsc",dsc));
            if (readtype==null){
                returnVO.setCode(200);
                returnVO.setMessage("没重复的");
                return returnVO;
            }else{
                returnVO.setCode(500);
                returnVO.setMessage("有重复的");
                return returnVO;
            }
        }

    }

    @PostMapping("/get")
    public ReturnVO get(){
        returnVO = new ReturnVO();
        List<Readtype> readtypeList = readtypeService.list();
        if (readtypeList==null||readtypeList.size()==0){
            returnVO.setCode(500);
            returnVO.setMessage("获取错误或为空");
            return returnVO;
        }else{
            returnVO.setCode(200);
            returnVO.setMessage("成功");
            returnVO.setData(readtypeList);
            return returnVO;
        }
    }

    @PostMapping("/set")
    public ReturnVO set(@RequestParam("dsc") String dsc){
        returnVO = new ReturnVO();
        if (dsc==null||dsc.equals("")){
            returnVO.setCode(500);
            returnVO.setMessage("数据为空");
            return returnVO;
        }else{
            Readtype readtype = new Readtype();
            readtype.setDsc(dsc);
            readtype.setCreatetime(timeUtil.getNowLocalDateTime());
            readtype.setDeleted(0);
            boolean result  = readtypeService.save( readtype);
            if (result){
                returnVO.setCode(200);
                returnVO.setMessage("保存成功");
                return returnVO;

            }else{
                returnVO.setCode(500);
                returnVO.setMessage("保存失败");
                return returnVO;
            }
        }
    }
}
