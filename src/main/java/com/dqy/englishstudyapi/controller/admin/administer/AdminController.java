package com.dqy.englishstudyapi.controller.admin.administer;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dqy.englishstudyapi.entity.adminEntity.condition.UserCondition;
import com.dqy.englishstudyapi.entity.frontEntity.FrontUser;
import com.dqy.englishstudyapi.entity.frontEntity.WxReturnUser;
import com.dqy.englishstudyapi.entity.page.MyPage;
import com.dqy.englishstudyapi.service.ScoreService;
import com.dqy.englishstudyapi.service.UserService;
import com.dqy.englishstudyapi.tablebean.Score;
import com.dqy.englishstudyapi.tablebean.User;
import com.dqy.englishstudyapi.tablebean.Zborder;
import com.dqy.englishstudyapi.util.HttpClientUtil;
import com.dqy.englishstudyapi.util.RandomUtil;
import com.dqy.englishstudyapi.util.TimeUtil;
import com.dqy.englishstudyapi.util.TokenUtils;
import com.dqy.englishstudyapi.vo.ReturnVO;
import com.dqy.englishstudyapi.vo.SubReturnVo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.sun.org.apache.bcel.internal.generic.NEW;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

@RestController
@RequestMapping("adminUser")
public class AdminController {
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


    @RequestMapping(value = "/deleteBatch",method = RequestMethod.POST)
    public ReturnVO deleteBatch(@RequestParam("ids")List<Integer> ids){
        returnVO = new ReturnVO();
        if (ids==null||ids.size()==0){
            returnVO.setCode(500);
            returnVO.setMessage("数据为空");
            return  returnVO;
        }else{
            boolean result =  userService.removeBatchByIds(ids);
            if (result){
                returnVO.setCode(200);
                returnVO.setMessage("删除成功");
            }else{
                returnVO.setCode(500);
                returnVO.setMessage("删除失败");
            }
        }


        return  returnVO;
    }
    @RequestMapping(value = "/deleteSingle",method = RequestMethod.POST)
    public ReturnVO deleteSingle(@RequestParam("id")Integer id){
        returnVO = new ReturnVO();
        if (id==null){
            returnVO.setCode(500);
            returnVO.setMessage("数据为空");
            return  returnVO;
        }else{
            boolean result =  userService.removeById(id);
            if (result){
                returnVO.setCode(200);
                returnVO.setMessage("删除成功");
            }else{
                returnVO.setCode(500);
                returnVO.setMessage("删除失败");
            }
        }


        return  returnVO;
    }
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

    @RequestMapping(value = "/toAdmin",method = RequestMethod.POST)
    public ReturnVO toAdmin(@RequestParam("id")Integer id){
        returnVO = new ReturnVO();
        User user = userService.getById(id);
        if (user!=null){
            user.setType(1);
            boolean result =  userService.updateById(user);
            if (result){
                returnVO.setCode(200);
                returnVO.setMessage("修改成功");
                returnVO.setData(user);
            }else{
                returnVO.setCode(500);
                returnVO.setMessage("修改失败");
                returnVO.setData(user);
            }

        }else{
            returnVO.setCode(500);
            returnVO.setMessage("获取失败");
        }

        return returnVO;
    }
    @RequestMapping(value = "/toNormalUser",method = RequestMethod.POST)
    public ReturnVO toNormalUser(@RequestParam("id")Integer id){
        returnVO = new ReturnVO();
        User user = userService.getById(id);
        if (user!=null){
            user.setType(0);
            boolean result =  userService.updateById(user);
            if (result){
                returnVO.setCode(200);
                returnVO.setMessage("修改成功");
                returnVO.setData(user);
            }else{
                returnVO.setCode(500);
                returnVO.setMessage("修改失败");
                returnVO.setData(user);
            }

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


    @RequestMapping(value = "/getAllMerchants",method = RequestMethod.POST)
    public ReturnVO getAllMerchants(){
        returnVO = new ReturnVO();



        List<User> users = userService.list(new QueryWrapper<User>().ne("type",0));
        if (users!=null){
            returnVO.setCode(200);
            returnVO.setMessage("获取成功");
            returnVO.setData(users);
        }else{
            returnVO.setCode(500);
            returnVO.setMessage("获取失败");
        }

        return returnVO;
    }
    @RequestMapping(value = "/getAllUserNoPageNoType",method = RequestMethod.POST)
    public ReturnVO getAllUserNoPageNoType(){
        returnVO = new ReturnVO();



        List<User> users = userService.list();
        if (users!=null){



            returnVO.setCode(200);
            returnVO.setMessage("获取成功");
            returnVO.setData(users);
        }else{
            returnVO.setCode(500);
            returnVO.setMessage("获取失败");
        }

        return returnVO;
    }
    @RequestMapping(value = "/getAllUser",method = RequestMethod.POST)
    public ReturnVO getAllUser(@RequestBody UserCondition condition){
        returnVO = new ReturnVO();
        if (condition==null){
            returnVO.setCode(500);
            returnVO.setMessage("数据为空");
            return returnVO;
        }
        Page<User> page = new Page<>();
        page.setCurrent(condition.getCurrent());
        page.setSize(condition.getSize());

        IPage<User> iPage = userService.page(page,getConditionWrapper(new QueryWrapper<User>().eq("type",0),condition));
        if (iPage.getRecords()!=null){
            MyPage<User> myPage = new MyPage<>();
            myPage.setData( iPage.getRecords().size()==0?new ArrayList<>(): (ArrayList<User>) iPage.getRecords());
            myPage.setPageSize(Math.toIntExact(iPage.getSize()));
            myPage.setTotal(Math.toIntExact(iPage.getTotal()));
            myPage.setCurrent(Math.toIntExact(iPage.getCurrent()));
            returnVO.setCode(200);
            returnVO.setMessage("获取成功");
            returnVO.setData(myPage);
        }else{
            returnVO.setCode(500);
            returnVO.setMessage("获取失败");
        }

        return returnVO;
    }

    @RequestMapping(value = "/getAllAdminNoPage",method = RequestMethod.POST)
    public ReturnVO getAllAdminNoPage(){
        returnVO = new ReturnVO();


        List<User> admins = userService.list(new QueryWrapper<User>().eq("type",1).or().eq("type",-1));
        if (admins!=null&&admins.size()!=0){

            returnVO.setCode(200);
            returnVO.setMessage("获取成功");
            returnVO.setData(admins);
        }else{
            returnVO.setCode(500);
            returnVO.setMessage("获取失败");
        }

        return returnVO;
    }
    @RequestMapping(value = "/getAllAdmin",method = RequestMethod.POST)
    public ReturnVO getAllAdmin(@RequestBody UserCondition condition){
        returnVO = new ReturnVO();
        if (condition==null){
            returnVO.setCode(500);
            returnVO.setMessage("数据为空");
            return returnVO;
        }
        Page<User> page = new Page<>();
        page.setCurrent(condition.getCurrent());
        page.setSize(condition.getSize());
        IPage<User> iPage = userService.page(page,getConditionWrapper(new QueryWrapper<User>().eq("type",1),condition));
        if (iPage.getRecords()!=null){
            MyPage<User> myPage = new MyPage<>();
            myPage.setData(iPage.getRecords().size()==0?new ArrayList<>(): (ArrayList<User>) iPage.getRecords());
            myPage.setPageSize(Math.toIntExact(iPage.getSize()));
            myPage.setTotal(Math.toIntExact(iPage.getTotal()));
            myPage.setCurrent(Math.toIntExact(iPage.getCurrent()));
            returnVO.setCode(200);
            returnVO.setMessage("获取成功");
            returnVO.setData(myPage);
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







    @ApiOperation(value = "登录")
    @RequestMapping(value = "/loginByUserName",method = RequestMethod.POST)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userName", value = "用户名", paramType = "String"),
            @ApiImplicitParam(name = "passWord", value = "密码", paramType = "String")
    })
    public ReturnVO loginByUserName(@RequestParam(value = "username") String username, @RequestParam(value = "password") String password ) throws JsonProcessingException {
        returnVO = new ReturnVO();

        HashMap<String,Object> param = new HashMap<>();
        param.put("username",username);
        param.put("password",password);
        param.put("type",1);
        HashMap<String,Object> param2 = new HashMap<>();
        param2.put("username",username);
        param2.put("password",password);
        param2.put("type",-1);
        User user = userService.getOne(new QueryWrapper<User>().allEq(param).or().allEq(param2));
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
        User user = userService.getOne(new QueryWrapper<User>().eq("phone",phone).and(t->t.eq("type",1).or().eq("type",-1)) );

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
            User loginUser =userService.getOne(new QueryWrapper<User>().eq("username",temptUser.getUsername()).and(t->t.eq("type",1).or().eq("type",-1)));
            if (loginUser!=null){
                FrontUser frontUser = new FrontUser();
                frontUser.setUser(loginUser);
                frontUser.setToken(getToken(loginUser));
                returnVO.setCode(200);
                returnVO.setData(frontUser);
                returnVO.setMessage("登录成功");
            }else{
                returnVO.setCode(500);
                returnVO.setMessage("登录失败:");
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
            User loginUser =userService.getOne(new QueryWrapper<User>().eq("username",temptUser.getUsername()).and(t->t.eq("type",1).or().eq("type",-1)));
            if (loginUser!=null){
                FrontUser frontUser = new FrontUser();
                frontUser.setUser(loginUser);
                frontUser.setToken(getToken(loginUser));
                returnVO.setCode(200);
                returnVO.setData(frontUser);
                returnVO.setMessage("登录成功");
            }else{
                returnVO.setCode(500);
                returnVO.setMessage("登录失败:");
            }
        }catch (Exception e){
            returnVO.setCode(500);
            e.printStackTrace();
            returnVO.setMessage("登录失败:"+e);
        }

        return  returnVO;
    }
    public String getToken(User user){
        return  TokenUtils.sign(user);
    }

    public QueryWrapper<User> getConditionWrapper(QueryWrapper<User> wrapper,UserCondition condition){
        if (wrapper==null){
            wrapper = new QueryWrapper<>();
        }
        if (condition.getUsernameSearch()!=null){
            wrapper = wrapper.like("username",condition.getUsernameSearch());
        }
        if (condition.getPasswordSearch()!=null){
            wrapper = wrapper.like("password",condition.getPasswordSearch());
        }
        if (condition.getNameSearch()!=null){
            wrapper = wrapper.like("name",condition.getNameSearch());
        }
        if (condition.getEmailSearch()!=null){
            wrapper = wrapper.like("email",condition.getEmailSearch());
        }
        if (condition.getPhoneSearch()!=null){
            wrapper = wrapper.like("phone",condition.getPhoneSearch());
        }
        if (condition.getSexSelect()!=null){
            Consumer<QueryWrapper<User>> consumer = new Consumer<QueryWrapper<User>>() {
                @Override
                public void accept(QueryWrapper<User> wrapper1) {
                    for (int i = 0; i < condition.getSexSelect().size(); i++) {
                        if (i!=condition.getSexSelect().size()-1){
                            wrapper1 = wrapper1.eq("sex",condition.getSexSelect().get(i)).or();
                        }else{
                            wrapper1 = wrapper1.eq("sex",condition.getSexSelect().get(i));
                        }
                    }
                }
            };
            wrapper.and(consumer);
        }
        if (condition.getOrderbyAsc()!=null&&condition.getOrderbyAsc().size()!=0){
           wrapper= wrapper.orderByAsc(condition.getOrderbyAsc());
        }
        List<String> orderbydesc = condition.getOrderbyDesc();
        orderbydesc.add("updatetime");
        condition.setOrderbyDesc(orderbydesc);
        if (condition.getOrderbyDesc()!=null&&condition.getOrderbyDesc().size()!=0){
            wrapper= wrapper.orderByDesc(condition.getOrderbyDesc());
        }
        if (condition.getBirthdayDate()!=null&&condition.getBirthdayDate().size()!=0){
            wrapper=wrapper.between("birthday",condition.getBirthdayDate().get(0),condition.getBirthdayDate().get(1));
        }

        return  wrapper;
    }

}
