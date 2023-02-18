package com.dqy.englishstudyapi.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dqy.englishstudyapi.entity.frontEntity.EmailVerifyCode;
import com.dqy.englishstudyapi.entity.frontEntity.PhoneVerifyCode;
import com.dqy.englishstudyapi.service.UserService;
import com.dqy.englishstudyapi.tablebean.User;
import com.dqy.englishstudyapi.util.RandomUtil;
import com.dqy.englishstudyapi.util.SessionUtil;
import com.dqy.englishstudyapi.util.SmsUtil;
import com.dqy.englishstudyapi.vo.ReturnVO;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("code")
public class CodeController {
    @Autowired
    RandomUtil randomUtil;
    @Autowired
    SessionUtil sessionUtil;
    @Autowired
    SmsUtil smsUtil;
    ReturnVO returnVO;
    @Autowired
    UserService userService;
    @RequestMapping(value = "/getcode",method = RequestMethod.POST)
    @ApiOperation(value = "发送手机验证码", notes = "测试", httpMethod = "POST")
    public ReturnVO sendCode(@RequestBody PhoneVerifyCode phoneVerifyCode , HttpServletRequest request) {
        returnVO = new ReturnVO();

        String phoneNum = phoneVerifyCode.getPhone();
        if (null == phoneNum || "".equals(phoneNum)) {
           returnVO.setCode(500);
           returnVO.setMessage("请输入手机号");
           return returnVO;
        }

        //随机生成四位的验证码
        phoneVerifyCode.setCode(randomUtil.randomNumber(6));
        //自定义类设置进缓存
        sessionUtil.setCodeSession(phoneVerifyCode,request);

//        returnVO.setCode(200);
//        returnVO.setMessage("发送成功");
//        returnVO.setData(phoneVerifyCode);

        //此处省略业务逻辑，发送手机验证码
        try {
            returnVO =smsUtil.sendCode(phoneVerifyCode);

        } catch (Exception e) {
            returnVO.setCode(500);
            returnVO.setMessage(e.getMessage());
            e.printStackTrace();
        }


        return returnVO;
    }

    @RequestMapping(value = "/getcodeEmail",method = RequestMethod.POST)
    @ApiOperation(value = "邮箱验证码", notes = "测试", httpMethod = "POST")
    public ReturnVO sendCodeEmail(@RequestBody EmailVerifyCode emailVerifyCode, HttpServletRequest request) {
        returnVO = new ReturnVO();

        String email= emailVerifyCode.getEmail();
        if (null == email || "".equals(email)) {
            returnVO.setCode(500);
            returnVO.setMessage("请输入邮箱号");
            return returnVO;
        }

        //随机生成四位的验证码
        emailVerifyCode.setCode(randomUtil.randomNumber(6));
        //自定义类设置进缓存
        sessionUtil.setCodeSessionEmail(emailVerifyCode,request);
//        returnVO.setCode(200);
//        returnVO.setMessage("发送成功");
//        returnVO.setData(emailVerifyCode);

        //此处省略业务逻辑，发送手机验证码
        try {
            returnVO =smsUtil.sendCodeEmail(emailVerifyCode);

        } catch (Exception e) {
            returnVO.setCode(500);
            returnVO.setMessage(e.getMessage());
            e.printStackTrace();
        }


        return returnVO;
    }


    @RequestMapping(value = "/verifyCode",method = RequestMethod.POST)
    @ApiOperation(value = "校验验证码", notes = "手机号、验证码一起传", httpMethod = "POST")
    public ReturnVO verifyCode(@RequestBody PhoneVerifyCode phoneVerifyCode , HttpServletRequest request)  {
        returnVO = new ReturnVO();
        User user =userService.getOne(new QueryWrapper<User>().eq("phone",phoneVerifyCode.getPhone()));
        if (user==null){
            returnVO.setCode(400);
            returnVO.setMessage("该号码暂未注册，请去注册");
            return returnVO;
        }
        Map<String, List<String>> checkCode = (Map<String, List<String>>) request.getSession().getAttribute(SessionUtil.PHONECODE_SESSION);

        if (null == checkCode) {
            returnVO.setCode(500);
            returnVO.setMessage("验证码已超时！null");
            return returnVO;
        }
        if (null != checkCode.get(phoneVerifyCode.getPhone()) && checkCode.get(phoneVerifyCode.getPhone()).size() > 0) {
            List<String> codes = checkCode.get(phoneVerifyCode.getPhone());

            if (codes.contains(phoneVerifyCode.getCode())) {
                //比对成功，移除元素以及清除缓存
                codes.remove(phoneVerifyCode.getCode());
                request.getSession().removeAttribute(SessionUtil.PHONECODE_SESSION);
            } else {
                returnVO.setCode(500);
                returnVO.setMessage("验证码错误！");
                return returnVO;
            }

        } else {
            returnVO.setCode(500);
            returnVO.setMessage("验证码已超时！null2");
            return returnVO;
        }
        returnVO.setCode(200);
        returnVO.setMessage("验证成功！");
        return returnVO;
    }

    @RequestMapping(value = "/verifyCodeEmail",method = RequestMethod.POST)
    @ApiOperation(value = "校验验证码", notes = "邮箱号、验证码一起传", httpMethod = "POST")
    public ReturnVO verifyCodeEmail(@RequestBody EmailVerifyCode emailVerifyCode , HttpServletRequest request)  {
        returnVO = new ReturnVO();
        User user =userService.getOne(new QueryWrapper<User>().eq("email",emailVerifyCode.getEmail()));
        if (user==null){
            returnVO.setCode(400);
            returnVO.setMessage("该邮箱暂未注册，请去注册");
            return returnVO;
        }
        Map<String, List<String>> checkCode = (Map<String, List<String>>) request.getSession().getAttribute(SessionUtil.EMAILCODE_SESSION);

        if (null == checkCode) {
            returnVO.setCode(500);
            returnVO.setMessage("验证码已超时！null");
            return returnVO;
        }
        if (null != checkCode.get(emailVerifyCode.getEmail()) && checkCode.get(emailVerifyCode.getEmail()).size() > 0) {
            List<String> codes = checkCode.get(emailVerifyCode.getEmail());

            if (codes.contains(emailVerifyCode.getCode())) {
                //比对成功，移除元素以及清除缓存
                codes.remove(emailVerifyCode.getCode());
                request.getSession().removeAttribute(SessionUtil.EMAILCODE_SESSION);
            } else {
                returnVO.setCode(500);
                returnVO.setMessage("验证码错误！");
                return returnVO;
            }

        } else {
            returnVO.setCode(500);
            returnVO.setMessage("验证码已超时！null2");
            return returnVO;
        }
        returnVO.setCode(200);
        returnVO.setMessage("验证成功！");
        return returnVO;
    }


    @RequestMapping(value = "/verifyCodeRegist",method = RequestMethod.POST)
    @ApiOperation(value = "校验验证码", notes = "手机号、验证码一起传", httpMethod = "POST")
    public ReturnVO verifyCodeRegist(@RequestBody PhoneVerifyCode phoneVerifyCode , HttpServletRequest request)  {
        returnVO = new ReturnVO();
        Map<String, List<String>> checkCode = (Map<String, List<String>>) request.getSession().getAttribute(SessionUtil.PHONECODE_SESSION);

        if (null == checkCode) {
            returnVO.setCode(500);
            returnVO.setMessage("验证码已超时！null");
            return returnVO;
        }
        if (null != checkCode.get(phoneVerifyCode.getPhone()) && checkCode.get(phoneVerifyCode.getPhone()).size() > 0) {
            List<String> codes = checkCode.get(phoneVerifyCode.getPhone());

            if (codes.contains(phoneVerifyCode.getCode())) {
                //比对成功，移除元素以及清除缓存
                codes.remove(phoneVerifyCode.getCode());
                request.getSession().removeAttribute(SessionUtil.PHONECODE_SESSION);
            } else {
                returnVO.setCode(500);
                returnVO.setMessage("验证码错误！");
                return returnVO;
            }

        } else {
            returnVO.setCode(500);
            returnVO.setMessage("验证码已超时！null2");
            return returnVO;
        }
        returnVO.setCode(200);
        returnVO.setMessage("验证成功！");
        return returnVO;
    }

    @RequestMapping(value = "/verifyCodeEmailRegist",method = RequestMethod.POST)
    @ApiOperation(value = "校验验证码", notes = "邮箱号、验证码一起传", httpMethod = "POST")
    public ReturnVO verifyCodeEmailRegist(@RequestBody EmailVerifyCode emailVerifyCode , HttpServletRequest request)  {
        returnVO = new ReturnVO();
        Map<String, List<String>> checkCode = (Map<String, List<String>>) request.getSession().getAttribute(SessionUtil.EMAILCODE_SESSION);

        if (null == checkCode) {
            returnVO.setCode(500);
            returnVO.setMessage("验证码已超时！null");
            return returnVO;
        }
        if (null != checkCode.get(emailVerifyCode.getEmail()) && checkCode.get(emailVerifyCode.getEmail()).size() > 0) {
            List<String> codes = checkCode.get(emailVerifyCode.getEmail());

            if (codes.contains(emailVerifyCode.getCode())) {
                //比对成功，移除元素以及清除缓存
                codes.remove(emailVerifyCode.getCode());
                request.getSession().removeAttribute(SessionUtil.EMAILCODE_SESSION);
            } else {
                returnVO.setCode(500);
                returnVO.setMessage("验证码错误！");
                return returnVO;
            }

        } else {
            returnVO.setCode(500);
            returnVO.setMessage("验证码已超时！null2");
            return returnVO;
        }
        returnVO.setCode(200);
        returnVO.setMessage("验证成功！");
        return returnVO;
    }
}
