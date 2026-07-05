package com.dqy.englishstudyapi.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dqy.englishstudyapi.service.SigninService;
import com.dqy.englishstudyapi.tablebean.Signin;
import com.dqy.englishstudyapi.util.TimeUtil;
import com.dqy.englishstudyapi.vo.ReturnVO;
import com.dqy.englishstudyapi.vo.SubReturnVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 邓亲优
 * @since 2023-02-20
 */
@RestController
@RequestMapping("signin")
public class SigninController {
    @Autowired
    SigninService signinService;
    @Autowired
    TimeUtil timeUtil;
    ReturnVO returnVO;

    @RequestMapping(value = "/isSignInToday",method = RequestMethod.POST)
    public ReturnVO isSignInToday(@RequestParam("uid")Integer uid){
        returnVO = new ReturnVO();
        if (uid==null){
            returnVO .setCode(500);
            returnVO.setMessage("数据为空");
            return  returnVO;
        }
        Map<String,Object> params = new HashMap<>();
        params.put("uid",uid);
        params.put("createdate",timeUtil.getNowLocalDate());
        Signin signin =signinService.getOne(new QueryWrapper<Signin>().allEq(params));
        if (signin==null){
            returnVO .setCode(200);
            returnVO.setMessage("未签到");

        }else{
            returnVO .setCode(500);
            returnVO.setMessage("已签到");
        }
        return returnVO;
    }

    @RequestMapping(value = "/signIn",method = RequestMethod.POST)
    public ReturnVO signIn(@RequestParam("uid")Integer uid){
        returnVO = new ReturnVO();
        if (uid==null){
            returnVO .setCode(500);
            returnVO.setMessage("数据为空");
            return  returnVO;
        }
        SubReturnVo subReturnVo =  signinService.signIn(uid,timeUtil.getNowLocalDate());
        if (subReturnVo.isResult()){
            returnVO .setCode(200);
            returnVO.setMessage("签到成功");
            return  returnVO;
        }else{
            returnVO .setCode(500);
            returnVO.setMessage(subReturnVo.getMessage());
            return  returnVO;
        }

    }
    //补签
    @RequestMapping(value = "/signInAfter",method = RequestMethod.POST)
    public ReturnVO signInAfter(@RequestParam("uid")Integer uid, @RequestParam("time") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)LocalDate date){
        returnVO = new ReturnVO();
        if (uid==null){
            returnVO .setCode(500);
            returnVO.setMessage("数据为空");
            return  returnVO;
        }

        SubReturnVo subReturnVo =  signinService.signInAfter(uid,date);
        if (subReturnVo.isResult()){
            returnVO .setCode(subReturnVo.getCode());
            returnVO.setMessage("签到成功");
            return  returnVO;
        }else{
            returnVO .setCode(subReturnVo.getCode());
            returnVO.setMessage(subReturnVo.getMessage());
            return  returnVO;
        }
    }

    //补签
    @RequestMapping(value = "/getSeriesDayNum",method = RequestMethod.POST)
    public ReturnVO signInAfter(@RequestParam("uid")Integer uid){
        returnVO = new ReturnVO();
        if (uid==null){
            returnVO .setCode(500);
            returnVO.setMessage("数据为空");
            return  returnVO;
        }
        SubReturnVo subReturnVo = new SubReturnVo();
        subReturnVo=signinService.getSeriesDayNum(uid);
        if (subReturnVo.isResult()){
            returnVO.setCode(200);
            returnVO.setMessage("获取成功");
            returnVO.setData(subReturnVo.getData());
            return returnVO;
        }else{
            returnVO .setCode(500);
            returnVO.setMessage(subReturnVo.getMessage());
            return  returnVO;
        }

    }
    @RequestMapping(value = "/getSignIn",method = RequestMethod.POST)
    public ReturnVO getSignIn(@RequestParam("uid")Integer uid){
        returnVO = new ReturnVO();
        if (uid==null){
            returnVO .setCode(500);
            returnVO.setMessage("数据为空");
            return  returnVO;
        }
        SubReturnVo subReturnVo =  signinService.getSignIn(uid);
        if (subReturnVo.isResult()){
            returnVO .setCode(200);
            returnVO.setMessage("获取签到成功");
            returnVO.setData(subReturnVo.getData());
            return  returnVO;
        }else{
            returnVO .setCode(500);
            returnVO.setMessage(subReturnVo.getMessage());
            return  returnVO;
        }

    }
}
