package com.dqy.englishstudyapi.controller.admin;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dqy.englishstudyapi.entity.adminEntity.BasicSetting.BasicScoreSetting.BasicScoreSetting;
import com.dqy.englishstudyapi.entity.adminEntity.BasicSetting.BasicScoreSetting.MaxScore;
import com.dqy.englishstudyapi.entity.frontEntity.DataGraphyTable.DataGraphyTable;
import com.dqy.englishstudyapi.service.ScoreService;
import com.dqy.englishstudyapi.service.UserService;
import com.dqy.englishstudyapi.service.ZborderService;
import com.dqy.englishstudyapi.tablebean.Score;
import com.dqy.englishstudyapi.tablebean.User;
import com.dqy.englishstudyapi.tablebean.Zborder;
import com.dqy.englishstudyapi.util.TimeUtil;
import com.dqy.englishstudyapi.vo.ReturnVO;
import com.dqy.englishstudyapi.vo.SubReturnVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@RestController
@RequestMapping("adminScoreSetting")
public class AdminScoreSettingController {
    @Autowired
    ScoreService scoreService;
    @Autowired
    UserService userService;
    @Autowired
    ZborderService zborderService;
    @Autowired
    TimeUtil timeUtil;

    ReturnVO returnVO;
    @PostMapping("/getBasicSetting")
    public ReturnVO getBasicSetting() {
        returnVO = new ReturnVO();

        LocalDateTime nowDateTime =timeUtil.getCurrentTimeLocalDateTime();
        LocalDate startDate = timeUtil.getNowLocalDate();
        LocalDateTime startDateTime =startDate.atTime(0,0,0);

        BasicScoreSetting basicScoreSetting = new BasicScoreSetting();
        List<Score> scores = scoreService.list();
        if (scores!=null&&scores.size()!=0){
            Score maxscore=  Collections.max(scores, Comparator.comparing(k->k.getScore()));
            MaxScore max =new MaxScore();
            max.setNum(maxscore.getScore());
            max.setUser(userService.getOne(new QueryWrapper<User>().eq("id",maxscore.getUid())));
            basicScoreSetting.setMax(max);

            Long allMoney=0L;
            Long todayMoney = 0L;

            List<Zborder> zborders = zborderService.list(new QueryWrapper<Zborder>().eq("status",1));
            for (Zborder zb:zborders
                 ) {
                allMoney+=zb.getMoney().longValue();
            }
            List<Zborder> zborders2 = zborderService.list(new QueryWrapper<Zborder>().eq("status",1).between("createtime",startDateTime,nowDateTime));
            for (Zborder zb:zborders2
            ) {
                todayMoney+=zb.getMoney().longValue();
            }
            basicScoreSetting.setAllMoney(allMoney);
            basicScoreSetting.setTodayMoney(todayMoney);

            List<DataGraphyTable> dataGraphyTables = new ArrayList<>();
            SubReturnVo subReturnVo =zborderService.getDataByUidAndDay();
            if (subReturnVo.isResult()){
                dataGraphyTables.add((DataGraphyTable) subReturnVo.getData());
            }else{
                dataGraphyTables.add(new DataGraphyTable());
            }

            subReturnVo =zborderService.getDataByUidAndWeek();
            if (subReturnVo.isResult()){
                dataGraphyTables.add((DataGraphyTable) subReturnVo.getData());
            }else{
                dataGraphyTables.add(new DataGraphyTable());
            }

            subReturnVo =zborderService.getDataByUidAndMonth();
            if (subReturnVo.isResult()){
                dataGraphyTables.add((DataGraphyTable) subReturnVo.getData());
            }else{
                dataGraphyTables.add(new DataGraphyTable());
            }

            basicScoreSetting.setData(dataGraphyTables);


            returnVO.setCode(200);
            returnVO.setMessage("获取成功");
            returnVO.setData(basicScoreSetting);
            return returnVO;
        }else{
            returnVO.setCode(500);
            returnVO.setMessage("获取错误或数据为空");
            returnVO.setData(null);
            return returnVO;
        }

    }



















}
