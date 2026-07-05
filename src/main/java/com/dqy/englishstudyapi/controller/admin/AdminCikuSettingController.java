package com.dqy.englishstudyapi.controller.admin;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dqy.englishstudyapi.entity.adminEntity.BasicSetting.BasicCikuSetting.BasicCikuSetting;
import com.dqy.englishstudyapi.entity.adminEntity.BasicSetting.BasicCikuSetting.BasicCikuSettingNode;
import com.dqy.englishstudyapi.entity.adminEntity.BasicSetting.BasicCikuSetting.CikuSimpleFull;
import com.dqy.englishstudyapi.entity.adminEntity.BasicSetting.BasicUserSetting;
import com.dqy.englishstudyapi.service.CikuService;
import com.dqy.englishstudyapi.service.CikuexampleService;
import com.dqy.englishstudyapi.service.CikutypeService;
import com.dqy.englishstudyapi.tablebean.Ciku;
import com.dqy.englishstudyapi.tablebean.Cikutype;
import com.dqy.englishstudyapi.tablebean.User;
import com.dqy.englishstudyapi.util.TimeUtil;
import com.dqy.englishstudyapi.vo.ReturnVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.chrono.ChronoLocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("adminCikuSetting")
public class AdminCikuSettingController {
    @Autowired
    CikutypeService cikutypeService;
    @Autowired
    CikuService cikuService;
    @Autowired
    CikuexampleService cikuexampleService;
    @Autowired
    TimeUtil timeUtil;
    ReturnVO returnVO;

    @PostMapping("/getBasicSetting")
    public ReturnVO getBasicSetting(){
        returnVO = new ReturnVO();

        LocalDateTime nowDateTime =timeUtil.getCurrentTimeLocalDateTime();
        LocalDate startDate = timeUtil.getNowLocalDate();
        LocalDateTime startDateTime =startDate.atTime(0,0,0);



        List<Cikutype> cikutypes =  cikutypeService.list();
        List<CikuSimpleFull> lastCikus = new ArrayList<>();
        if (cikutypes!=null&&cikutypes.size()!=0){

            Long allCount = 0L;
            Long todayCount = 0L;
            for (Cikutype ct:cikutypes
                 ) {
                if (ct.getId()!=1){
                    cikuService.createTable(ct.getId());
                    List<Ciku> cikus =  cikuService.select(ct.getId());
                    if (cikus!=null){
                        allCount+=cikus.size();
                        for (Ciku ck:cikus
                             ) {
                            CikuSimpleFull cikuSimpleFull = new CikuSimpleFull();
                            cikuexampleService.createTable(ct.getId(),ck.getId());
                            Integer count = cikuexampleService.count(ct.getId(),ck.getId());
                            cikuSimpleFull.setCiku(ck);
                            cikuSimpleFull.setCount(count);
                            cikuSimpleFull.setParentDsc(ct.getDsc());
                            lastCikus.add(cikuSimpleFull);
                        }
                    }else{
                        allCount+=0L;
                    }
                    List<Ciku> todayCikus = cikuService.selectByToday(ct.getId(),startDateTime,nowDateTime);
                    if (todayCikus!=null){
                        todayCount+=todayCikus.size();
                    }else{
                        todayCount+=0L;
                    }


                }
            }

            Collections.sort(lastCikus,((o1,o2)->{
                if (o1.getCiku().getCreatetime().isAfter(o2.getCiku().getCreatetime())){
                    return  -1;
                }else{
                    return 1;
                }
            }));
            BasicCikuSetting basicCikuSetting = new BasicCikuSetting();
            List<BasicCikuSettingNode> basicCikuSettingNodes = new ArrayList<>();
            Integer limit =5;
            for (CikuSimpleFull csf:lastCikus
                 ) {
                if (limit>0){
                    limit--;
                    BasicCikuSettingNode basicCikuSettingNode = new BasicCikuSettingNode();
                    basicCikuSettingNode.setDsc(csf.getCiku().getDsc());
                    basicCikuSettingNode.setCreatetime(csf.getCiku().getCreatetime());
                    basicCikuSettingNode.setCount(csf.getCount());
                    basicCikuSettingNode.setParentDsc(csf.getParentDsc());
                    basicCikuSettingNodes.add(basicCikuSettingNode);
                }
            }
            basicCikuSetting.setNodes(basicCikuSettingNodes);
            basicCikuSetting.setTodayCount(todayCount);
            basicCikuSetting.setAllCount(allCount);
            returnVO.setCode(200);
            returnVO.setMessage("获取数据成功");
            returnVO.setData( basicCikuSetting);
            return  returnVO;
        }else{
            returnVO.setCode(500);
            returnVO.setMessage("获取数据失败 或为空");
            returnVO.setData(null);
            return returnVO;
        }
    }
}
