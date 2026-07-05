package com.dqy.englishstudyapi.controller;


import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.dqy.englishstudyapi.entity.frontEntity.FrontUser;
import com.dqy.englishstudyapi.entity.frontEntity.WxReturnUser;
import com.dqy.englishstudyapi.service.ScoreService;
import com.dqy.englishstudyapi.service.UserService;
import com.dqy.englishstudyapi.tablebean.Score;
import com.dqy.englishstudyapi.tablebean.User;
import com.dqy.englishstudyapi.util.HttpClientUtil;
import com.dqy.englishstudyapi.util.RandomUtil;
import com.dqy.englishstudyapi.util.TimeUtil;
import com.dqy.englishstudyapi.util.TokenUtils;
import com.dqy.englishstudyapi.vo.ReturnVO;
import com.dqy.englishstudyapi.vo.SubReturnVo;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 邓亲优
 * @since 2023-02-11
 */
@RestController
@RequestMapping("user")
public class UserController {
    @Value("${dqy.wx-appid}")
    String wxappid;
    @Value("${dqy.wx-secretid}")
    String wxsecretid;
    @Value("${dqy.qq-appid}")
    String qqappid;
    @Value("${dqy.qq-secretid}")
    String qqsecretid;
    @Autowired
    UserService userService;
    @Autowired
    ScoreService scoreService;
    @Autowired
    HttpClientUtil httpClientUtil;
    @Autowired
    RandomUtil randomUtil;
    @Autowired
    TimeUtil timeUtil;
    ReturnVO returnVO;
    @RequestMapping(value = "/update",method = RequestMethod.POST)
    public ReturnVO update(@RequestBody User user){
        returnVO = new ReturnVO();
        if (user==null){
            returnVO.setCode(500);
            returnVO.setMessage("数据为空");
            return  returnVO;
        }else{
            boolean result =  userService.updateById(user);
            if (result){
                returnVO.setCode(200);
                returnVO.setMessage("修改成功");
                returnVO.setData(user);
            }else{
                returnVO.setCode(500);
                returnVO.setMessage("修改失败");
            }
        }


        return  returnVO;
    }

    @RequestMapping(value = "/save",method = RequestMethod.POST)
    public ReturnVO save(@RequestBody User user){
        returnVO = new ReturnVO();
        if (user==null){
            returnVO.setCode(500);
            returnVO.setMessage("数据为空");
            return  returnVO;
        }else{
            user.setCreatetime(timeUtil.getNowLocalDateTime());
            user.setDeleted(0);
            boolean result =  userService.save(user);
            if (result){
                SubReturnVo subReturnVo =  scoreService.setScore(user.getId(),5,null);
                if (subReturnVo.isResult()){
                    returnVO.setCode(200);
                    returnVO.setMessage("注册成功");
                }else{
                    returnVO.setCode(500);
                    returnVO.setMessage(subReturnVo.getMessage());
                }
            }else{
                returnVO.setCode(500);
                returnVO.setMessage("添加失败");
            }
        }


        return  returnVO;
    }
    @RequestMapping(value = "/getFullUser",method = RequestMethod.POST)
    public ReturnVO getFullUser(@RequestParam("id")Integer id){
        returnVO = new ReturnVO();
        User user = userService.getById(id);
        if (user!=null){
            returnVO.setCode(200);
            returnVO.setMessage("获取成功");
            returnVO.setData(user);
        }else{
            returnVO.setCode(500);
            returnVO.setMessage("获取失败");
        }

        return returnVO;
    }
    @RequestMapping(value = "/getUser",method = RequestMethod.POST)
    public ReturnVO getUser(@RequestParam("id")Integer id){
        returnVO = new ReturnVO();
        User user = userService.getById(id);
        if (user!=null){
            returnVO.setCode(200);
            returnVO.setMessage("获取成功");
            returnVO.setData(user);
        }else{
            returnVO.setCode(500);
            returnVO.setMessage("获取失败");
        }

        return returnVO;
    }


    @RequestMapping(value = "/checkToken",method = RequestMethod.POST)
    public ReturnVO checkToken(){
        returnVO = new ReturnVO();
        returnVO.setCode(200);
        returnVO.setMessage("校验成功");
        return returnVO;
    }


    @RequestMapping(value = "/updatePwd",method = RequestMethod.POST)
    public ReturnVO updatePwd(@RequestParam("phone") String phone,@RequestParam("password") String password){
        returnVO = new ReturnVO();
        if (phone==null||password==null||"".equals(phone.trim())||"".equals(password.trim())){
            returnVO.setCode(500);
            returnVO.setMessage("数据为空");
            return  returnVO;
        }else{
            boolean result =  userService.update(new UpdateWrapper<User>().set("password",password).eq("phone",phone));
            if (result){
                returnVO.setCode(200);
                returnVO.setMessage("修改成功");
            }else{
                returnVO.setCode(500);
                returnVO.setMessage("修改失败");
            }
        }


        return  returnVO;
    }



    @RequestMapping(value = "/register",method = RequestMethod.POST)
    public ReturnVO selectByPhone(@RequestBody User user){
        returnVO = new ReturnVO();
        if (user==null){
            returnVO.setCode(500);
            returnVO.setMessage("数据为空");
            return  returnVO;
        }else{
            user.setCreatetime(timeUtil.getCurrentTimeLocalDateTime());
            user.setDeleted(0);
            user.setType(0);
           boolean result =  userService.save(user);
           if (result){
               returnVO.setCode(200);
               returnVO.setMessage("注册成功");
           }else{
               returnVO.setCode(500);
               returnVO.setMessage("注册失败");
           }
        }


        return  returnVO;
    }


    @RequestMapping(value = "/selectByPhone",method = RequestMethod.POST)
    public ReturnVO selectByPhone(@RequestParam("phone")String phone){
        returnVO = new ReturnVO();

        User user = userService.getOne(new QueryWrapper<User>().eq("phone",phone));
        if (user!=null){
            returnVO.setCode(200);
            returnVO.setMessage("存在");
        }else{
            returnVO.setCode(500);
            returnVO.setMessage("不存在");
        }

        return  returnVO;
    }



    @RequestMapping(value = "/selectByEmail",method = RequestMethod.POST)
    public ReturnVO selectByEmail(@RequestParam("email")String email){
        returnVO = new ReturnVO();

        User user = userService.getOne(new QueryWrapper<User>().eq("email",email));
        if (user!=null){
            returnVO.setCode(200);
            returnVO.setMessage("存在");
        }else{
            returnVO.setCode(500);
            returnVO.setMessage("不存在");
        }

        return  returnVO;
    }

    @RequestMapping(value = "/selectByUsername",method = RequestMethod.POST)
    public ReturnVO selectByUsername(@RequestParam("username")String username){
        returnVO = new ReturnVO();

        User user = userService.getOne(new QueryWrapper<User>().eq("username",username));
        if (user!=null){
            returnVO.setCode(200);
            returnVO.setMessage("存在");
        }else{
            returnVO.setCode(500);
            returnVO.setMessage("不存在");
        }

        return  returnVO;
    }





    @ApiOperation(value = "登录")
    @RequestMapping(value = "/loginByUserName",method = RequestMethod.POST)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userName", value = "用户名", paramType = "String"),
            @ApiImplicitParam(name = "passWord", value = "密码", paramType = "String")
    })
    public ReturnVO loginByUserName(@RequestParam(value = "username") String username, @RequestParam(value = "password") String password ) throws JsonProcessingException {
        returnVO = new ReturnVO();

        HashMap<String,String> param = new HashMap<>();
        param.put("username",username);
        param.put("password",password);
        User user = userService.getOne(new QueryWrapper<User>().allEq(param));
        if (user!=null){
            FrontUser frontUser = new FrontUser();
            frontUser.setUser(user);
            frontUser.setToken(getToken(user));
            returnVO.setCode(200);
            returnVO.setData(frontUser);
            returnVO.setMessage("登录成功");
        }else{
            returnVO.setCode(500);
            returnVO.setMessage("账号密码错误");
        }
       return returnVO;

    }

    @ApiOperation(value = "登录")
    @RequestMapping(value = "/loginByPhone",method = RequestMethod.POST)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "phone", value = "电话号码", paramType = "String")

    })
    public ReturnVO loginByPhone(@RequestParam(value = "phone") String phone) throws JsonProcessingException {
        returnVO = new ReturnVO();
        if (null==phone||phone.trim().equals("")){
            returnVO.setCode(500);
            returnVO.setMessage("电话号码不为空");
            return  returnVO;
        }
        User user = userService.getOne(new QueryWrapper<User>().eq("phone",phone));

        if (user!=null){
            Score score = scoreService.getOne(new QueryWrapper<Score>().eq("uid",user.getId()));
            SubReturnVo subReturnVo =null;
            if (score==null){
                subReturnVo=scoreService.setScore(user.getId(),5,null);
            }
            if (subReturnVo!=null){
                if (subReturnVo.getCode()!=200){
                    returnVO.setCode(subReturnVo.getCode());
                    returnVO.setMessage(subReturnVo.getMessage());
                    return  returnVO;
                }
            }

            FrontUser frontUser = new FrontUser();
            frontUser.setUser(user);
            frontUser.setToken(getToken(user));
            returnVO.setCode(200);
            returnVO.setData(frontUser);
            returnVO.setMessage("登录成功");
        }else{
            returnVO.setCode(500);
            returnVO.setMessage("电话号码错误");
        }
        return returnVO;

    }

    @RequestMapping(value = "/wxlogin",method = RequestMethod.POST)
    public ReturnVO wxlogin(@RequestParam("code") String code){
        returnVO =new ReturnVO();
        MultiValueMap<String,String> param = new LinkedMultiValueMap<>();
        param.add("appid",wxappid);
        param.add("secret",wxsecretid);
        param.add("js_code",code);
        param.add("grant_type","authorization_code");
        try {
            JSONObject object=  httpClientUtil.client(
                    "https://api.weixin.qq.com/sns/jscode2session",
                    HttpMethod.GET,
                    param
            );
            WxReturnUser wxReturnUser = new WxReturnUser();
            wxReturnUser.setUnionid((String) object.get("unionid"));
            wxReturnUser.setOpenid((String) object.get("openid"));
            wxReturnUser.setSession_key((String) object.get("session_key"));
            User temptUser = new User();
            temptUser.setUsername(wxReturnUser.getOpenid());
            User loginUser =userService.getOne(new QueryWrapper<User>().eq("username",temptUser.getUsername()));
            if (loginUser!=null){
                FrontUser frontUser = new FrontUser();
                frontUser.setUser(loginUser);
                frontUser.setToken(getToken(loginUser));
                returnVO.setCode(200);
                returnVO.setData(frontUser);
                List<Integer> type = new ArrayList<>();
                type.add(0);
                returnVO.setDatas(Collections.singletonList(type));
                returnVO.setMessage("登录成功");
            }else{
                User storeUser = new User();
                storeUser.setUsername(wxReturnUser.getOpenid());
                storeUser.setHeadimage("bg1.jpg");
                storeUser.setName("默认用户"+randomUtil.randomAll(4));
                storeUser.setPassword("123456");
                storeUser.setPhone("");
                storeUser.setSex(0);
                storeUser.setAge(18);
                storeUser.setEmail("");
                storeUser.setDeleted(0);
                storeUser.setType(0);
                storeUser.setBirthday(timeUtil.getCurrentTimeLocalDate());
                storeUser.setCreatetime(timeUtil.getCurrentTimeLocalDateTime());
                boolean result =userService.save(storeUser);
                if(result){
                    SubReturnVo subReturnVo =  scoreService.setScore(storeUser.getId(),5,null);
                    if (subReturnVo.isResult()){
                        FrontUser frontUser = new FrontUser();
                        frontUser.setUser(storeUser);
                        frontUser.setToken(getToken(storeUser));
                        returnVO.setCode(250);
                        returnVO.setData(frontUser);
                        List<Integer> type = new ArrayList<>();
                        type.add(1);
                        returnVO.setDatas(Collections.singletonList(type));
                        returnVO.setMessage("注册成功且登录成功");
                    }else{
                        returnVO.setCode(500);
                        returnVO.setMessage(subReturnVo.getMessage());
                    }

                }else{
                    returnVO.setCode(500);
                    returnVO.setMessage("登录成功但注册失败");
                }
            }
        }catch (Exception e){
            returnVO.setCode(500);
            e.printStackTrace();
            returnVO.setMessage("登录失败:"+e);
        }

        return  returnVO;
    }

    @RequestMapping(value = "/qqlogin",method = RequestMethod.POST)
    public ReturnVO qqlogin(@RequestParam("code") String code){
        returnVO =new ReturnVO();
        MultiValueMap<String,String> param = new LinkedMultiValueMap<>();
        param.add("appid",qqappid);
        param.add("secret",qqsecretid);
        param.add("js_code",code);
        param.add("grant_type","authorization_code");
        try {
            JSONObject object=  httpClientUtil.client(
                    "https://api.q.qq.com/sns/jscode2session",
                    HttpMethod.GET,
                    param
            );
            WxReturnUser wxReturnUser = new WxReturnUser();
            wxReturnUser.setUnionid((String) object.get("unionid"));
            wxReturnUser.setOpenid((String) object.get("openid"));
            wxReturnUser.setSession_key((String) object.get("session_key"));

            User temptUser = new User();
            temptUser.setUsername(wxReturnUser.getOpenid());
            User loginUser =userService.getOne(new QueryWrapper<User>().eq("username",temptUser.getUsername()));
            if (loginUser!=null){
                FrontUser frontUser = new FrontUser();
                frontUser.setUser(loginUser);
                frontUser.setToken(getToken(loginUser));
                returnVO.setCode(200);
                returnVO.setData(frontUser);
                returnVO.setMessage("登录成功");
            }else{
                User storeUser = new User();
                storeUser.setUsername(wxReturnUser.getOpenid());
                storeUser.setHeadimage("bg1.jpg");
                storeUser.setName("默认用户"+randomUtil.randomAll(4));
                storeUser.setPassword("123456");
                storeUser.setPhone("1234567890");
                storeUser.setSex(0);
                storeUser.setAge(18);
                storeUser.setEmail("");
                storeUser.setDeleted(0);
                storeUser.setType(0);
                storeUser.setBirthday(timeUtil.getCurrentTimeLocalDate());
                storeUser.setCreatetime(timeUtil.getCurrentTimeLocalDateTime());
                boolean result =userService.save(storeUser);
                if(result){

                    FrontUser frontUser = new FrontUser();
                    frontUser.setUser(storeUser);
                    frontUser.setToken(getToken(storeUser));
                    returnVO.setCode(200);
                    returnVO.setData(frontUser);
                    returnVO.setMessage("注册成功且登录成功");
                }else{
                    returnVO.setCode(500);
                    returnVO.setMessage("登录成功但注册失败");
                }
            }
        }catch (Exception e){
            returnVO.setCode(500);
            e.printStackTrace();
            returnVO.setMessage("登录失败:"+e);
        }

        return  returnVO;
    }

    @RequestMapping(value = "/rejisterByWx",method = RequestMethod.POST)

    public ReturnVO rejisterByWx(@RequestParam("uid")Integer uid,@RequestParam(value = "phone") String phone,@RequestParam("headimage")String headimage,@RequestParam("name")String name) throws JsonProcessingException {
        returnVO = new ReturnVO();
        if (uid==null||null==phone||phone.trim().equals("")||null==headimage||headimage.trim().equals("")||null==name||name.trim().equals("")){
            returnVO.setCode(500);
            returnVO.setMessage("参数不为空");
            return  returnVO;
        }
        User user = userService.getById(uid);

        if (user!=null){
            user.setName(name);
            user.setHeadimage(headimage);
            user.setPhone(phone);
            user.setUpdatetime(timeUtil.getCurrentTimeLocalDateTime());
            boolean result = userService.updateById(user);
            if (result){
                FrontUser frontUser = new FrontUser();
                frontUser.setUser(user);
                frontUser.setToken(getToken(user));
                returnVO.setCode(200);
                returnVO.setData(frontUser);
                returnVO.setMessage("登录成功");
            }else{
                returnVO.setCode(500);
                returnVO.setMessage("注册信息失败");
            }

        }else{
            returnVO.setCode(500);
            returnVO.setMessage("找不到该用户");
        }
        return returnVO;

    }

    @RequestMapping(value = "/deleteById",method = RequestMethod.POST)

    public ReturnVO deleteById(@RequestParam("id")Integer id) throws JsonProcessingException {
        returnVO = new ReturnVO();
        if (id==null){
            returnVO.setCode(500);
            returnVO.setMessage("参数不为空");
            return  returnVO;
        }
        User user = userService.getById(id);

        if (user!=null&&user.getType()!=-1){

            boolean result = userService.removeById(user);
            if (result){

                returnVO.setCode(200);
                returnVO.setMessage("注销成功");
            }else{
                returnVO.setCode(500);
                returnVO.setMessage("注销失败");
            }

        }else{
            returnVO.setCode(500);
            returnVO.setMessage("找不到该用户或权限错误");
        }
        return returnVO;

    }
    public String getToken(User user){
        return  TokenUtils.sign(user);
    }
}
