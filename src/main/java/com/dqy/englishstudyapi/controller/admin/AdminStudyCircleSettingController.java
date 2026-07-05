package com.dqy.englishstudyapi.controller.admin;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dqy.englishstudyapi.entity.adminEntity.BasicSetting.BasicDataSetting.BasicDataSetting;
import com.dqy.englishstudyapi.entity.adminEntity.BasicSetting.BasicStudyCircleSetting.BasicStudyCircleSetting;
import com.dqy.englishstudyapi.entity.adminEntity.BasicSetting.BasicStudyCircleSetting.StudyCircleSimpleFull;
import com.dqy.englishstudyapi.service.StudycircleService;
import com.dqy.englishstudyapi.service.UserService;
import com.dqy.englishstudyapi.tablebean.Studycircle;
import com.dqy.englishstudyapi.util.TimeUtil;
import com.dqy.englishstudyapi.vo.ReturnVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("adminStudyCircleSetting")
public class AdminStudyCircleSettingController {
    @Autowired
    StudycircleService studycircleService;
    @Autowired
    UserService userService;

    @Autowired
    TimeUtil timeUtil;
    ReturnVO returnVO;
    @PostMapping("/getBasicSetting")
    public ReturnVO getBasicSetting() {
        returnVO = new ReturnVO();

        LocalDateTime nowDateTime =timeUtil.getCurrentTimeLocalDateTime();
        LocalDate startDate = timeUtil.getNowLocalDate();
        LocalDateTime startDateTime =startDate.atTime(0,0,0);

        Long allCount = 0L;
        Long todayCount = 0L;

        List<Studycircle> studycircles = studycircleService.list(new QueryWrapper<Studycircle>().orderByDesc("updatetime"));
        BasicStudyCircleSetting basicStudyCircleSetting = new BasicStudyCircleSetting();
        List<StudyCircleSimpleFull> lastStudycicles = new ArrayList<>();
        if (studycircles!=null&&studycircles.size()!=0){
            allCount+=studycircles.size();
            for (Studycircle sc:studycircles
                 ) {
                StudyCircleSimpleFull studyCircleSimpleFull = new StudyCircleSimpleFull();
                studyCircleSimpleFull.setTitle(sc.getTitle());
                studyCircleSimpleFull.setUpdatetime(sc.getUpdatetime());
                studyCircleSimpleFull.setUser(userService.getById(sc.getUid()));
                lastStudycicles.add(studyCircleSimpleFull);
            }
        }
        basicStudyCircleSetting.setLastStudyCircles(lastStudycicles);
        basicStudyCircleSetting.setAllCount(allCount);
        List<Studycircle> studycircles2 = studycircleService.list(new QueryWrapper<Studycircle>().between("updatetime",startDateTime,nowDateTime).orderByDesc("updatetime"));
        if (studycircles2!=null&&studycircles2.size()!=0){
            todayCount+=studycircles2.size();
        }
        basicStudyCircleSetting.setTodayCount(todayCount);
        returnVO.setCode(200);
        returnVO.setMessage("获取成功");
        returnVO.setData(basicStudyCircleSetting);
        return  returnVO;
    }
}
