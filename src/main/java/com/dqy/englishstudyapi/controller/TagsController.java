package com.dqy.englishstudyapi.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dqy.englishstudyapi.service.TagsService;
import com.dqy.englishstudyapi.tablebean.Tags;
import com.dqy.englishstudyapi.util.TimeUtil;
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
 * @since 2023-02-24
 */
@RestController
@RequestMapping("tags")
public class TagsController {
    @Autowired
    TagsService tagsService;
    @Autowired
    TimeUtil timeUtil;

    ReturnVO returnVO;

    @PostMapping("/get")
    public  ReturnVO get(){
        returnVO = new ReturnVO();
        List<Tags> tags =  tagsService.list();
        if (tags!=null&&tags.size()!=0){
            returnVO.setCode(200);
            returnVO.setData(tags);
            returnVO.setMessage("获取成功");
            return returnVO;
        }else{
            returnVO.setCode(500);
            returnVO.setMessage("获取失败或为空");
            return returnVO;
        }
    }

    @PostMapping("/getExist")
    public  ReturnVO getExist(@RequestParam("dsc")String dsc){
        returnVO = new ReturnVO();
        if (dsc==null||dsc.equals("")){
            returnVO.setCode(500);
            returnVO.setMessage("数据不能为空");
            return returnVO;
        }else{
            Tags tags =  tagsService.getOne(new QueryWrapper<Tags>().eq("dsc",dsc));
            if (tags!=null){
                returnVO.setCode(200);
                returnVO.setData(tags);
                returnVO.setMessage("存在");
                return returnVO;
            }else{
                returnVO.setCode(500);
                returnVO.setMessage("不存在");
                return returnVO;
            }
        }

    }

    @PostMapping("/insert")
    public  ReturnVO get(@RequestParam("uid")Integer uid,@RequestParam("dsc")String dsc){
        returnVO = new ReturnVO();
        if (uid==null||dsc==null||dsc.equals("")){
            returnVO.setCode(500);
            returnVO.setMessage("数据不能为空");
            return returnVO;
        }else{
           Tags tags = new Tags();
           tags.setCreateuid(uid);
           tags.setDeleted(0);
           tags.setCreatetime(timeUtil.getCurrentTimeLocalDateTime());
           tags.setDsc(dsc);
           boolean result =tagsService.save(tags);
            if (result){
                returnVO.setCode(200);
                returnVO.setData(tags);
                returnVO.setMessage("存储成功");
                return returnVO;
            }else{
                returnVO.setCode(500);
                returnVO.setMessage("存储失败或为空");
                return returnVO;
            }
        }

    }
}
