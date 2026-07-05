package com.dqy.englishstudyapi.controller.admin;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dqy.englishstudyapi.entity.adminEntity.BasicSetting.BasicCikuSetting.BasicCikuSettingNode;
import com.dqy.englishstudyapi.entity.adminEntity.BasicSetting.BasicReadSetting.BasicReadSetting;
import com.dqy.englishstudyapi.entity.adminEntity.BasicSetting.BasicReadSetting.BasicReadSettingNode;
import com.dqy.englishstudyapi.entity.adminEntity.BasicSetting.BasicReadSetting.ReadSimpleFull;
import com.dqy.englishstudyapi.entity.adminEntity.BasicSetting.BasicTestSetting.BasicTestSetting;
import com.dqy.englishstudyapi.entity.adminEntity.BasicSetting.BasicTestSetting.BasicTestSettingNode;
import com.dqy.englishstudyapi.service.*;
import com.dqy.englishstudyapi.tablebean.Readtype;
import com.dqy.englishstudyapi.tablebean.Readtypesub;
import com.dqy.englishstudyapi.tablebean.Test;
import com.dqy.englishstudyapi.tablebean.Testtype;
import com.dqy.englishstudyapi.util.DynamicTableNameUtil;
import com.dqy.englishstudyapi.util.TimeUtil;
import com.dqy.englishstudyapi.vo.ReturnVO;
import com.sun.org.apache.bcel.internal.generic.NEW;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("adminTestSetting")
public class AdminTestSettingController {
    @Autowired
    TesttypeService testtypeService;

    @Autowired
    TestService testService;
    @Autowired
    TimeUtil timeUtil;
    @Autowired
    DynamicTableNameUtil dynamicTableNameUtil;
    ReturnVO returnVO;

    @PostMapping("/getBasicSetting")
    public ReturnVO getBasicSetting(){
        returnVO = new ReturnVO();

        LocalDateTime nowDateTime =timeUtil.getCurrentTimeLocalDateTime();
        LocalDate startDate = timeUtil.getNowLocalDate();
        LocalDateTime startDateTime =startDate.atTime(0,0,0);

        Long allCount = 0L;
        Long todayCount = 0L;
        List<Testtype> alltesttypes = testtypeService.list(new QueryWrapper<Testtype>().orderByDesc("createtime"));
        if (alltesttypes!=null){
            allCount+=alltesttypes.size();
        }else{
            allCount+=0L;
        }
        List<Testtype> todaytesttypes = testtypeService.list(new QueryWrapper<Testtype>().between("createtime",startDateTime,nowDateTime));
        if (todaytesttypes !=null){
            todayCount+=todaytesttypes .size();
        }else{
            todayCount+=0L;
        }
        BasicTestSetting basicTestSetting = new BasicTestSetting();
        basicTestSetting.setTodayCount(todayCount);
        basicTestSetting.setAllCount(allCount);
        Integer limit =5;
        if (alltesttypes.size()>limit){
            alltesttypes=alltesttypes.subList(0,limit);
        }
        List<BasicTestSettingNode> nodes = new ArrayList<>();
        for (Testtype tt:alltesttypes
             ) {
            testService.createTableIfNotExist(tt.getId());
            dynamicTableNameUtil.SetTableName("test","_"+tt.getId());
            Long count = testService.count();
            BasicTestSettingNode basicTestSettingNode = new BasicTestSettingNode();
            basicTestSettingNode.setDsc(tt.getDsc());
            basicTestSettingNode.setCreatetime(tt.getCreatetime());
            if (count!=null){
                basicTestSettingNode.setCount(Math.toIntExact(count));
            }else{
                basicTestSettingNode.setCount(0);
            }
            nodes.add(basicTestSettingNode);
        }
        basicTestSetting.setNodes(nodes);
        returnVO.setCode(200);
        returnVO.setMessage("获取数据成功");
        returnVO.setData(basicTestSetting);
        return  returnVO;

    }
}
