package com.dqy.englishstudyapi.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dqy.englishstudyapi.service.PtypeService;
import com.dqy.englishstudyapi.service.PtypesubService;
import com.dqy.englishstudyapi.tablebean.Ptype;
import com.dqy.englishstudyapi.tablebean.Ptypesub;
import com.dqy.englishstudyapi.util.TimeUtil;
import com.dqy.englishstudyapi.vo.ReturnVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 邓亲优
 * @since 2023-02-21
 */
@RestController
@RequestMapping("ptypesub")
public class PtypesubController {
    @Autowired
    PtypesubService ptypesubService;
    @Autowired
    TimeUtil timeUtil;
    ReturnVO returnVO;

    @PostMapping("/get")
    public ReturnVO get(@RequestParam("ptypeid") Integer ptypeid){
        returnVO = new ReturnVO();
        ArrayList<Ptypesub> ptypessub = (ArrayList<Ptypesub>) ptypesubService.list(new QueryWrapper<Ptypesub>().eq("ptypeid",ptypeid));
        if (ptypessub!=null&&ptypessub.size()!=0){
            returnVO.setCode(200);
            returnVO.setMessage("获取成功");
            returnVO.setData(ptypessub);
            return returnVO;
        }else{
            returnVO.setCode(500);
            returnVO.setMessage("获取失败");
            return returnVO;
        }
    }
    @PostMapping("/getRepeat")
    public ReturnVO getRepeat(@RequestParam("ptypeid") Integer ptypeid,@RequestParam("type")String type){
        returnVO = new ReturnVO();
        Map<String,Object> params = new HashMap<>();
        params.put("ptypeid",ptypeid);
        params.put("dsc",type);
        Ptypesub ptypes =  ptypesubService.getOne(new QueryWrapper<Ptypesub>().allEq(params));
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
    public ReturnVO set(@RequestParam("ptypeid") Integer ptypeid,@RequestParam("type")String type){
        returnVO = new ReturnVO();
        Ptypesub ptypesub = new Ptypesub();
        ptypesub.setCreatetime(timeUtil.getCurrentTimeLocalDateTime());
        ptypesub.setDeleted(0);
        ptypesub.setDsc(type);
        ptypesub.setPtypeid(ptypeid);
        boolean result = ptypesubService.save(ptypesub);
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
