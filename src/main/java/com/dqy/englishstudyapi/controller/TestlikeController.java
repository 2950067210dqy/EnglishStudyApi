package com.dqy.englishstudyapi.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.dqy.englishstudyapi.service.TestlikeService;
import com.dqy.englishstudyapi.tablebean.Testlike;
import com.dqy.englishstudyapi.util.TimeUtil;
import com.dqy.englishstudyapi.vo.ReturnVO;
import com.dqy.englishstudyapi.vo.SubReturnVo;
import freemarker.ext.beans.BeansWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
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
@RequestMapping("testlike")
public class TestlikeController {
    @Autowired
    TestlikeService testlikeService;
    @Autowired
    TimeUtil timeUtil;
    ReturnVO returnVO;

    @PostMapping("/count")
    public ReturnVO count(@RequestParam("uid") Integer uid
    ){
        returnVO = new ReturnVO();
        if (uid==null){
            returnVO.setCode(500);
            returnVO.setMessage("数据为空");
            return returnVO;
        }

        Long count = testlikeService.count(new QueryWrapper<Testlike>().eq("uid", uid));
        if (count!=null){
            returnVO.setData(count);
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
    public ReturnVO set(@RequestParam("testtype")Integer testtype,@RequestParam("testid")Integer testid,@RequestParam("uid")Integer uid){

        returnVO = new ReturnVO();
        if (testtype==null||testid==null||uid==null){
            returnVO.setCode(500);
            returnVO.setMessage("数据为空");
            return  returnVO;
        }
        Map<String,Object> params = new HashMap<>();
        params.put("uid",uid);
        params.put("testtype",testtype);
        params.put("testid",testid);
        Testlike exist = testlikeService.getOne(new QueryWrapper<Testlike>().allEq(params));
        if (exist==null){
            Testlike testlike = new Testlike();
            testlike.setCreatetime(timeUtil.getNowLocalDateTime());
            testlike.setUid(uid);
            testlike.setDeleted(0);
            testlike.setTestid(testid);
            testlike.setTesttype(testtype);
            boolean result = testlikeService.save(testlike);
            if (result){
                returnVO.setCode(200);
                returnVO.setMessage("收藏成功");
                return  returnVO;
            }else{
                returnVO.setCode(500);
                returnVO.setMessage("收藏失败");
                return  returnVO;
            }
        }else{
            exist.setUpdatetime(timeUtil.getNowLocalDateTime());
            boolean result = testlikeService.updateById(exist);
            if (result){
                returnVO.setCode(200);
                returnVO.setMessage("收藏成功2");
                return  returnVO;
            }else{
                returnVO.setCode(500);
                returnVO.setMessage("收藏失败2");
                return  returnVO;
            }
        }



    }

    @PostMapping("/delete")
    public ReturnVO delete(@RequestParam("testtype")Integer testtype,@RequestParam("testid")Integer testid,@RequestParam("uid")Integer uid){

        returnVO = new ReturnVO();
        if (testtype==null||testid==null||uid==null){
            returnVO.setCode(500);
            returnVO.setMessage("数据为空");
            return  returnVO;
        }
        Map<String,Object> params = new HashMap<>();
        params.put("uid",uid);
        params.put("testid",testid);
        params.put("testtype",testtype);
        boolean result = testlikeService.remove(new QueryWrapper<Testlike>().allEq(params));
        if (result){
            returnVO.setCode(200);
            returnVO.setMessage("取消收藏成功");
            return  returnVO;
        }else{
            returnVO.setCode(500);
            returnVO.setMessage("取消收藏失败");
            return  returnVO;
        }

    }

    @PostMapping("/get")
    public ReturnVO get(@RequestParam("uid")Integer uid){

        returnVO = new ReturnVO();
        if (uid==null){
            returnVO.setCode(500);
            returnVO.setMessage("数据为空");
            return  returnVO;
        }
        SubReturnVo subReturnVo =testlikeService.getFull(uid);
        if ( subReturnVo.isResult()){
            returnVO.setCode(200);
            returnVO.setMessage("获取收藏成功");
            returnVO.setData(subReturnVo.getData());
            return  returnVO;
        }else{
            returnVO.setCode(subReturnVo.getCode());
            returnVO.setMessage(subReturnVo.getMessage());
            return  returnVO;
        }

    }
}
