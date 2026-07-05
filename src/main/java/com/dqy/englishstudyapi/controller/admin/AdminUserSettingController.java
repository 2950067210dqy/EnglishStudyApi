package com.dqy.englishstudyapi.controller.admin;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dqy.englishstudyapi.entity.adminEntity.BasicSetting.BasicUserSetting;
import com.dqy.englishstudyapi.service.UserService;
import com.dqy.englishstudyapi.tablebean.User;
import com.dqy.englishstudyapi.util.TimeUtil;
import com.dqy.englishstudyapi.vo.ReturnVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("adminUserSetting")
public class AdminUserSettingController {
    @Autowired
    UserService userService;
    @Autowired
    TimeUtil timeUtil;
    ReturnVO returnVO;

    @PostMapping("/getBasicSetting")
    public ReturnVO getBasicSetting(){
        returnVO = new ReturnVO();
        Long allCount = userService.count(new QueryWrapper<User>().eq("type",0));
        if (allCount==null){
            allCount=0L;
        }
        LocalDateTime nowDateTime =timeUtil.getCurrentTimeLocalDateTime();
        LocalDate startDate = timeUtil.getNowLocalDate();
        LocalDateTime startDateTime =startDate.atTime(0,0,0);
        Long todayCount = userService.count(new QueryWrapper<User>().eq("type",0).between("createtime", startDateTime,nowDateTime));
        if (todayCount==null){
            todayCount=0L;
        }
        List<User> registerLast = userService.list(new QueryWrapper<User>().eq("type",0).orderByDesc("createtime").last(" limit 0,5 "));
        BasicUserSetting basicUserSetting = new BasicUserSetting();
        basicUserSetting.setTodayCount(todayCount);
        basicUserSetting.setAllCount(allCount);
        basicUserSetting.setLastRegisters(registerLast);
        returnVO.setCode(200);
        returnVO.setMessage("获取成功");
        returnVO.setData(basicUserSetting);
        return  returnVO;
    }
}
