package com.dqy.englishstudyapi.controller.admin;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dqy.englishstudyapi.entity.adminEntity.BasicSetting.BasicDataSetting.BasicDataSetting;
import com.dqy.englishstudyapi.service.RecitedataService;
import com.dqy.englishstudyapi.service.RecitedatasumService;
import com.dqy.englishstudyapi.tablebean.Recitedata;
import com.dqy.englishstudyapi.tablebean.Recitedatasum;
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
@RequestMapping("adminDataSetting")
public class AdminDataSettingController {
    @Autowired
    RecitedataService recitedataService;
    @Autowired
    RecitedatasumService recitedatasumService;

    @Autowired
    TimeUtil timeUtil;

    ReturnVO returnVO;
    @PostMapping("/getBasicSetting")
    public ReturnVO getBasicSetting() {
        returnVO = new ReturnVO();
        Long todayStudyCount=0L;
        Long allStudyCount=0L;
        Long todayReviewCount=0L;
        Long allReviewCount=0L;
        Long todayTimeCount=0L;
        Long allTimeCount=0L;
        Long todayUserCount=0L;
        Long allUserCount=0L;

        List<Recitedatasum> recitedatasums = recitedatasumService.list();
        if (recitedatasums!=null&&recitedatasums.size()!=0){
            for (Recitedatasum rs:recitedatasums
                 ) {
                allStudyCount+=rs.getNum();
                allReviewCount+=rs.getNum2();
                allTimeCount+=rs.getTime();
                allUserCount++;
            }
        }

        LocalDateTime nowDateTime =timeUtil.getCurrentTimeLocalDateTime();
        LocalDate startDate = timeUtil.getNowLocalDate();
        LocalDateTime startDateTime =startDate.atTime(0,0,0);

        List<Recitedata> todayData = recitedataService.list(new QueryWrapper<Recitedata>().between("createtime",startDateTime,nowDateTime));
        if (todayData!=null&&todayData.size()!=0){
            Integer olduid = -1;
            for (Recitedata rd: todayData
                 ) {
                todayStudyCount+=rd.getNum();
                todayReviewCount+=rd.getNum2();
                todayTimeCount+=rd.getTime();
                if (rd.getUid()!=olduid){
                    olduid=rd.getUid();
                    todayUserCount++;
                }

            }
        }
        BasicDataSetting basicDataSetting = new BasicDataSetting() ;
        basicDataSetting.setAllTimeCount(allTimeCount);
        basicDataSetting.setAllStudyCount(allStudyCount);
        basicDataSetting.setAllReviewCount(allReviewCount);
        basicDataSetting.setAllUserCount(allUserCount);
        basicDataSetting.setTodayStudyCount(todayStudyCount);
        basicDataSetting.setTodayReviewCount(todayReviewCount);
        basicDataSetting.setTodayTimeCount(todayTimeCount);
        basicDataSetting.setTodayUserCount(todayUserCount);
        returnVO.setCode(200);
        returnVO.setMessage("获取成功");
        returnVO.setData(basicDataSetting);
        return  returnVO;
    }

}
