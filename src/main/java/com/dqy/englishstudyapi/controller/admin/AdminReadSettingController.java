package com.dqy.englishstudyapi.controller.admin;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dqy.englishstudyapi.entity.adminEntity.BasicSetting.BasicCikuSetting.BasicCikuSetting;
import com.dqy.englishstudyapi.entity.adminEntity.BasicSetting.BasicCikuSetting.BasicCikuSettingNode;
import com.dqy.englishstudyapi.entity.adminEntity.BasicSetting.BasicCikuSetting.CikuSimpleFull;
import com.dqy.englishstudyapi.entity.adminEntity.BasicSetting.BasicReadSetting.BasicReadSetting;
import com.dqy.englishstudyapi.entity.adminEntity.BasicSetting.BasicReadSetting.BasicReadSettingNode;
import com.dqy.englishstudyapi.entity.adminEntity.BasicSetting.BasicReadSetting.ReadSimpleFull;
import com.dqy.englishstudyapi.service.*;
import com.dqy.englishstudyapi.tablebean.*;
import com.dqy.englishstudyapi.util.DynamicTableNameUtil;
import com.dqy.englishstudyapi.util.TimeUtil;
import com.dqy.englishstudyapi.vo.ReturnVO;
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
@RequestMapping("adminReadSetting")
public class AdminReadSettingController {
    @Autowired
    ReadtypeService readtypeService;
    @Autowired
    ReadtypesubService readtypesubService;
    @Autowired
   ReadService readService;
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



        List<Readtype> readtypes =  readtypeService.list();
        List<ReadSimpleFull> lastReads = new ArrayList<>();
        if (readtypes!=null&&readtypes.size()!=0){

            Long allCount = 0L;
            Long todayCount = 0L;
            for (Readtype rt:readtypes
                 ) {


                    readtypesubService.createTable(rt.getId());
                    dynamicTableNameUtil.SetTableName("readtypesub","_"+rt.getId());
                    List<Readtypesub> readtypesubs =  readtypesubService.list();
                    if (readtypesubs!=null&&readtypesubs.size()!=0){
                        for (Readtypesub rts:readtypesubs
                             ) {
                            readService.createTable(rt.getId(),rts.getId());
                            dynamicTableNameUtil.SetTableName("read","_"+rt.getId()+"_"+rts.getId());
                            Long count = readService.count();
                            if (count!=null){
                                allCount+=count;
                            }else{
                                allCount+=0;
                            }


                            ReadSimpleFull  readSimpleFull = new  ReadSimpleFull();
                            readService.createTable(rt.getId(),rts.getId());
                            dynamicTableNameUtil.SetTableName("read","_"+rt.getId()+"_"+rts.getId());
                            count = readService.count();
                            readSimpleFull.setReadtypesub(rts);
                            if (count!=null){
                                readSimpleFull.setCount(Math.toIntExact(count));
                            }else{
                                readSimpleFull.setCount(0);
                            }

                            readSimpleFull.setParentDsc(rt.getDsc());
                            lastReads.add(readSimpleFull);
                        }
                    }else{
                        allCount+=0L;
                    }

                    readtypesubService.createTable(rt.getId());
                    dynamicTableNameUtil.SetTableName("readtypesub","_"+rt.getId());
                    List<Readtypesub> todayReadtypesubs = readtypesubService.list();
                    if (todayReadtypesubs!=null){
                        for (Readtypesub rts:todayReadtypesubs
                        ) {
                            readService.createTable(rt.getId(),rts.getId());
                            dynamicTableNameUtil.SetTableName("read","_"+rt.getId()+"_"+rts.getId());
                            Long count = readService.count(new QueryWrapper<Read>().between("createtime",startDateTime,nowDateTime) );
                            if (count!=null){
                                todayCount+=count;
                            }else{
                                todayCount+=0;
                            }

                        }
                    }else{
                        todayCount+=0L;
                    }




            }

            Collections.sort(lastReads,((o1,o2)->{
                if (o1.getReadtypesub().getCreatetime().isAfter(o2.getReadtypesub().getCreatetime())){
                    return  -1;
                }else{
                    return 1;
                }
            }));
            BasicReadSetting basicReadSetting = new BasicReadSetting();
            List<BasicReadSettingNode> basicReadSettingNodes = new ArrayList<>();
            Integer limit =5;
            for (ReadSimpleFull rsf:lastReads
                 ) {
                if (limit>0){
                    limit--;
                    BasicReadSettingNode basicReadSettingNode = new BasicReadSettingNode();
                    basicReadSettingNode.setDsc(rsf.getReadtypesub().getDsc());
                    basicReadSettingNode.setCreatetime(rsf.getReadtypesub().getCreatetime());
                    basicReadSettingNode.setCount(rsf.getCount());
                    basicReadSettingNode.setParentDsc(rsf.getParentDsc());
                    basicReadSettingNodes.add(basicReadSettingNode);
                }
            }
            basicReadSetting.setNodes(basicReadSettingNodes);
            basicReadSetting.setTodayCount(todayCount);
            basicReadSetting.setAllCount(allCount);
            returnVO.setCode(200);
            returnVO.setMessage("获取数据成功");
            returnVO.setData( basicReadSetting);
            return  returnVO;
        }else{
            returnVO.setCode(500);
            returnVO.setMessage("获取数据失败 或为空");
            returnVO.setData(null);
            return returnVO;
        }
    }
}
