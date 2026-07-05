package com.dqy.englishstudyapi.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dqy.englishstudyapi.service.ReadtypeService;
import com.dqy.englishstudyapi.service.ReadtypesubService;
import com.dqy.englishstudyapi.tablebean.Readtype;
import com.dqy.englishstudyapi.tablebean.Readtypesub;
import com.dqy.englishstudyapi.util.DynamicTableNameUtil;
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
@RequestMapping("readtypesub")
public class ReadtypesubController {
    @Autowired
    ReadtypesubService readtypesubService;
    @Autowired
    TimeUtil timeUtil;
    @Autowired
    JsonUtil jsonUtil;
    @Autowired
    DynamicTableNameUtil dynamicTableNameUtil;
    ReturnVO returnVO;

    @PostMapping("/getExist")
    public ReturnVO getExist(@RequestParam("rid")Integer rid, @RequestParam("dsc") String dsc){
        returnVO = new ReturnVO();
        if (dsc==null||dsc.equals("")||rid==null){
            returnVO.setCode(500);
            returnVO.setMessage("数据为空");
            return returnVO;
        }else{
            readtypesubService.createTable(rid);
            dynamicTableNameUtil.SetTableName("readtypesub","_"+rid);
            Readtypesub readtypesub = readtypesubService.getOne(new QueryWrapper<Readtypesub>().eq("dsc",dsc));

            if (readtypesub==null){
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
    public ReturnVO get(@RequestParam("rid")Integer rid){
        returnVO = new ReturnVO();
        if (rid==null){
            returnVO.setCode(500);
            returnVO.setMessage("数据为空");
            return returnVO;
        }else {
            readtypesubService.createTable(rid);
            dynamicTableNameUtil.SetTableName("readtypesub","_"+rid);
            List<Readtypesub> readtypesubList = readtypesubService.list();
            if (readtypesubList==null||readtypesubList.size()==0){
                returnVO.setCode(500);
                returnVO.setMessage("获取错误或为空");
                return returnVO;
            }else{
                returnVO.setCode(200);
                returnVO.setMessage("成功");
                returnVO.setData(readtypesubList);
                return returnVO;
            }
        }


    }

    @PostMapping("/set")
    public ReturnVO set(@RequestParam("rid")Integer rid,@RequestParam("dsc") String dsc){
        returnVO = new ReturnVO();
        if (dsc==null||dsc.equals("")){
            returnVO.setCode(500);
            returnVO.setMessage("数据为空");
            return returnVO;
        }else{
            Readtypesub readtypesub = new Readtypesub();
            readtypesub.setDsc(dsc);
            readtypesub.setCreatetime(timeUtil.getNowLocalDateTime());
            readtypesub.setDeleted(0);
            readtypesubService.createTable(rid);
            dynamicTableNameUtil.SetTableName("readtypesub","_"+rid);
            boolean result  = readtypesubService.save(readtypesub);
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
