package com.dqy.englishstudyapi.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dqy.englishstudyapi.service.ScoreService;
import com.dqy.englishstudyapi.service.ScoresourceService;
import com.dqy.englishstudyapi.service.ScoresourcetypeService;
import com.dqy.englishstudyapi.service.UserService;
import com.dqy.englishstudyapi.tablebean.Score;
import com.dqy.englishstudyapi.tablebean.User;
import com.dqy.englishstudyapi.util.JsonUtil;
import com.dqy.englishstudyapi.util.TimeUtil;
import com.dqy.englishstudyapi.vo.ReturnVO;
import com.dqy.englishstudyapi.vo.SubReturnVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 邓亲优
 * @since 2023-02-20
 */
@RestController
@RequestMapping("score")
public class ScoreController {
    @Autowired
    ScoreService scoreService;

    ReturnVO returnVO;

    @RequestMapping(value = "/get",method = RequestMethod.POST)
    public ReturnVO get(@RequestParam("uid")Integer uid){
        returnVO = new ReturnVO();
        if (uid==null){
            returnVO.setCode(500);
            returnVO.setMessage("数据为空");
            return returnVO;
        }
        Score score= scoreService.getOne(new QueryWrapper<Score>().eq("uid",uid));
        if (score!=null){
            returnVO.setCode(200);
            returnVO.setMessage("获取成功");
            returnVO.setData(score.getScore());
            return returnVO;
        }else{
            returnVO.setCode(500);
            returnVO.setMessage("获取失败");
            return returnVO;
        }
    }

    @RequestMapping(value = "/set",method = RequestMethod.POST)
    public ReturnVO set(@RequestParam("uid")Integer uid,@RequestParam("type")Integer type,@RequestParam(value = "num",required = false)Long num){
        returnVO = new ReturnVO();
        if (uid==null||type==null){
            returnVO.setCode(500);
            returnVO.setMessage("数据为空");
            return returnVO;
        }
        SubReturnVo subReturnVo = scoreService.setScore(uid,type,num);
        if (subReturnVo.isResult()){
            returnVO.setCode(200);
            returnVO.setMessage("存储成功");
            return returnVO;
        }else{
            returnVO.setCode(500);
            returnVO.setMessage(subReturnVo.getMessage());
            return returnVO;
        }
    }
}
