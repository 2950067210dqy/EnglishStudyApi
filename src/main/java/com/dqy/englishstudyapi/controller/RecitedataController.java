package com.dqy.englishstudyapi.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dqy.englishstudyapi.entity.frontEntity.DataGraphyTable.DataGraphyTable;
import com.dqy.englishstudyapi.entity.frontEntity.DataGraphyTable.Series;
import com.dqy.englishstudyapi.entity.frontEntity.SimpleReciteDataSum;
import com.dqy.englishstudyapi.service.RecitedataService;
import com.dqy.englishstudyapi.service.RecitedatasumService;
import com.dqy.englishstudyapi.tablebean.Recitedata;
import com.dqy.englishstudyapi.tablebean.Recitedatasum;
import com.dqy.englishstudyapi.util.JsonUtil;
import com.dqy.englishstudyapi.util.TimeUtil;
import com.dqy.englishstudyapi.vo.ReturnVO;
import com.dqy.englishstudyapi.vo.SubReturnVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 邓亲优
 * @since 2023-02-19
 */
@RestController
@RequestMapping("recitedata")
public class RecitedataController {
    @Autowired
    RecitedataService recitedataService;
    @Autowired
    RecitedatasumService recitedatasumService;
    @Autowired
    JsonUtil jsonUtil;
    @Autowired
    TimeUtil timeUtil;
    ReturnVO returnVO;


    @RequestMapping(value = "/getByUidAndMonth",method = RequestMethod.POST)
    public ReturnVO getDataByUidAndMonth(@RequestParam("uid") Integer uid){
        returnVO = new ReturnVO();
        if (uid==null){
            returnVO.setCode(500);
            returnVO.setMessage("数据不为空");
            return returnVO;
        }else{
            LocalDate nowDate =timeUtil.getCurrentTimeLocalDate();
            LocalDate monthAgoDate = timeUtil.differMonth(nowDate, 11L);
            ArrayList<DataGraphyTable>   dataGraphyTables = new ArrayList<>();
            DataGraphyTable weekNumTable = new DataGraphyTable();
            DataGraphyTable weekTimeTable = new DataGraphyTable();
            weekNumTable.setCategories(timeUtil.getRangeDateByDayToStringNotDay(timeUtil.getRangeDateByMonth(12)));
            weekTimeTable.setCategories(timeUtil.getRangeDateByDayToStringNotDay(timeUtil.getRangeDateByMonth(12)));
            ArrayList<Series> seriesNum = new ArrayList<>();
            ArrayList<Series> seriesTime = new ArrayList<>();
            Series learnNumS = new Series();
            learnNumS.setName("当月学习（个）");
//                learnNumS.setTextColor("#ffffff");
            Series reviewNumS = new Series();
            reviewNumS.setName("当月复习（个）");
//                reviewNumS.setTextColor("#ffffff");
            Series timeS = new Series();
            timeS.setName("当月学习（分钟）");
            ArrayList<Integer> learndata = new ArrayList<>();
            ArrayList<Integer> reviewdata = new ArrayList<>();
            ArrayList<Integer> timedata = new ArrayList<>();
            ArrayList<Recitedata> recitedatas = (ArrayList<Recitedata>) recitedataService.list(new QueryWrapper<Recitedata>().and(i->i.eq("uid",uid).between("createdate",LocalDate.of(monthAgoDate.getYear(),monthAgoDate.getMonth(),1),nowDate)));
            if (recitedatas!=null&&recitedatas.size()!=0){


                for (int i = 11; i >=0 ; i--) {

                        Iterator<Recitedata> iterable =  recitedatas.iterator();

                        Integer learncount = 0;
                        Integer reviewcount = 0;
                        Integer timecount= 0 ;
                        while (iterable.hasNext()) {
                            Recitedata temp = iterable.next();
                            System.out.println("before"+ timeUtil.differMonth(nowDate, (long) i).getMonthValue());
                            System.out.println("database"+temp.getCreatedate().getMonthValue());

                            if (timeUtil.differMonth(nowDate, (long) i).getMonthValue()==temp.getCreatedate().getMonthValue()){

                                learncount+=(temp.getNum());
                                reviewcount+=(temp.getNum2());
                                timecount+=(temp.getTime());
                                iterable.remove();

                            }



                        }

                        learndata.add(learncount);
                        reviewdata.add(reviewcount);
                        timedata.add(timecount);



                }
                learnNumS.setData(learndata);
                reviewNumS.setData(reviewdata);
                timeS.setData(timedata);
                seriesNum.add(learnNumS);
                seriesNum.add(reviewNumS);
                seriesTime.add(timeS);
                weekNumTable.setSeries(seriesNum);
                weekTimeTable.setSeries(seriesTime);
                dataGraphyTables.add(weekNumTable);
                dataGraphyTables.add(weekTimeTable);
                returnVO.setCode(200);
                returnVO.setMessage("获取成功");
                returnVO.setData(dataGraphyTables);
                return returnVO;
            }else{
                for (int i = 11; i >=0 ; i--) {
                    learndata.add(0);
                    reviewdata.add(0);
                    timedata.add(0);
                }
                learnNumS.setData(learndata);
                reviewNumS.setData(reviewdata);
                timeS.setData(timedata);
                seriesNum.add(learnNumS);
                seriesNum.add(reviewNumS);
                seriesTime.add(timeS);
                weekNumTable.setSeries(seriesNum);
                weekTimeTable.setSeries(seriesTime);
                dataGraphyTables.add(weekNumTable);
                dataGraphyTables.add(weekTimeTable);
                returnVO.setCode(200);
                returnVO.setMessage("未有数据");
                returnVO.setData(dataGraphyTables);
                return returnVO;
            }
        }
    }

    @RequestMapping(value = "/getByUidAndWeek",method = RequestMethod.POST)
    public ReturnVO getDataByUidAndWeek(@RequestParam("uid") Integer uid){
        returnVO = new ReturnVO();
        if (uid==null){
            returnVO.setCode(500);
            returnVO.setMessage("数据不为空");
            return returnVO;
        }else{
            LocalDate nowDate =timeUtil.getCurrentTimeLocalDate();
            LocalDate weekAgoDate = timeUtil.differDay(nowDate, 6L);
            ArrayList<DataGraphyTable>   dataGraphyTables = new ArrayList<>();
            DataGraphyTable weekNumTable = new DataGraphyTable();
            DataGraphyTable weekTimeTable = new DataGraphyTable();
            weekNumTable.setCategories(timeUtil.getRangeDateByDayToStringNotYear(timeUtil.getRangeDateByDay(7)));
            weekTimeTable.setCategories(timeUtil.getRangeDateByDayToStringNotYear(timeUtil.getRangeDateByDay(7)));
            ArrayList<Series> seriesNum = new ArrayList<>();
            ArrayList<Series> seriesTime = new ArrayList<>();
            Series learnNumS = new Series();
            learnNumS.setName("当日学习（个）");
//                learnNumS.setTextColor("#000000");
            Series reviewNumS = new Series();
            reviewNumS.setName("当日复习（个）");
//                reviewNumS.setTextColor("#000000");
            Series timeS = new Series();
            timeS.setName("当日学习（分钟）");
            ArrayList<Integer> learndata = new ArrayList<>();
            ArrayList<Integer> reviewdata = new ArrayList<>();
            ArrayList<Integer> timedata = new ArrayList<>();
            ArrayList<Recitedata> recitedatas = (ArrayList<Recitedata>) recitedataService.list(new QueryWrapper<Recitedata>().and(i->i.eq("uid",uid).between("createdate",weekAgoDate,nowDate)));
            if (recitedatas!=null&&recitedatas.size()!=0){


                for (int i = 6; i >= 0; i--) {

                    Iterator<Recitedata> iterable =  recitedatas.iterator();
                    boolean flag =true;
                    while (iterable.hasNext()) {
                        Recitedata temp = iterable.next();
                        if (timeUtil.differDay(nowDate, (long) i).isEqual(temp.getCreatedate())){
                            flag=false;
                            learndata.add(temp.getNum());
                            reviewdata.add(temp.getNum2());
                            timedata.add(temp.getTime());
                            iterable.remove();
                            break;
                        }
                    }
                    if (flag){
                        learndata.add(0);
                        reviewdata.add(0);
                        timedata.add(0);

                    }

                }
                learnNumS.setData(learndata);
                reviewNumS.setData(reviewdata);
                timeS.setData(timedata);
                seriesNum.add(learnNumS);
                seriesNum.add(reviewNumS);
                seriesTime.add(timeS);
                weekNumTable.setSeries(seriesNum);
                weekTimeTable.setSeries(seriesTime);
                dataGraphyTables.add(weekNumTable);
                dataGraphyTables.add(weekTimeTable);
                returnVO.setCode(200);
                returnVO.setMessage("获取成功");
                returnVO.setData(dataGraphyTables);
                return returnVO;
            }else{
                for (int i = 6; i >= 0; i--) {
                        learndata.add(0);
                        reviewdata.add(0);
                        timedata.add(0);
                }
                learnNumS.setData(learndata);
                reviewNumS.setData(reviewdata);
                timeS.setData(timedata);
                seriesNum.add(learnNumS);
                seriesNum.add(reviewNumS);
                seriesTime.add(timeS);
                weekNumTable.setSeries(seriesNum);
                weekTimeTable.setSeries(seriesTime);
                dataGraphyTables.add(weekNumTable);
                dataGraphyTables.add(weekTimeTable);
                returnVO.setCode(200);
                returnVO.setMessage("未有数据");
                returnVO.setData(dataGraphyTables);
                return returnVO;
            }
        }
    }


    @RequestMapping(value = "/getByUidCount",method = RequestMethod.POST)
    public ReturnVO getDataByUidCount(@RequestParam("uid") Integer uid){
        returnVO = new ReturnVO();
        if (uid==null){
            returnVO.setCode(500);
            returnVO.setMessage("数据不为空");
            return returnVO;
        }else{
            LocalDate nowDate =timeUtil.getCurrentTimeLocalDate();

            ArrayList<Recitedata> recitedatas = (ArrayList<Recitedata>) recitedataService.list(new QueryWrapper<Recitedata>().eq("uid",uid));
            if (recitedatas!=null&&recitedatas.size()!=0){
                Integer sum = 0;
                Integer time =0;
                for (Recitedata r:recitedatas
                     ) {
                    sum+=(r.getNum()+r.getNum2());
                    time+=r.getTime();
                }
                SimpleReciteDataSum simpleReciteDataSum = new SimpleReciteDataSum();
                simpleReciteDataSum.setSum(sum);
                simpleReciteDataSum.setTime(time);
                returnVO.setCode(200);
                returnVO.setMessage("获取成功");
                returnVO.setData(simpleReciteDataSum);
                return returnVO;
            }else{
                returnVO.setCode(500);
                returnVO.setMessage("未有数据");
                return returnVO;
            }
        }
    }

    @RequestMapping(value = "/getByUid",method = RequestMethod.POST)
    public ReturnVO getDataByUid(@RequestParam("uid") Integer uid){
        returnVO = new ReturnVO();
        if (uid==null){
            returnVO.setCode(500);
            returnVO.setMessage("数据不为空");
            return returnVO;
        }else{
            LocalDate nowDate =timeUtil.getCurrentTimeLocalDate();

            ArrayList<Recitedata> recitedatas = (ArrayList<Recitedata>) recitedataService.list(new QueryWrapper<Recitedata>().eq("uid",uid));
            if (recitedatas!=null&&recitedatas.size()!=0){
                returnVO.setCode(200);
                returnVO.setMessage("获取成功");
                returnVO.setData(recitedatas);
                return returnVO;
            }else{
                returnVO.setCode(500);
                returnVO.setMessage("未有数据");
                return returnVO;
            }
        }
    }

    @RequestMapping(value = "/getByUidToday",method = RequestMethod.POST)
    public ReturnVO getDataByUidToday(@RequestParam("uid") Integer uid){
        returnVO = new ReturnVO();
        if (uid==null){
            returnVO.setCode(500);
            returnVO.setMessage("数据不为空");
            return returnVO;
        }else{
            LocalDate nowDate =timeUtil.getCurrentTimeLocalDate();
            Map<String,Object> params = new HashMap<>();
            params.put("uid",uid);
            params.put("createdate",nowDate);
            Recitedata recitedata =  recitedataService.getOne(new QueryWrapper<Recitedata>().allEq(params));
            if (recitedata!=null){
                returnVO.setCode(200);
                returnVO.setMessage("获取成功");
                returnVO.setData(recitedata);
                return returnVO;
            }else{
                returnVO.setCode(500);
                returnVO.setMessage("今天还未背诵");
                return returnVO;
            }
        }
    }

    @RequestMapping(value = "/setByUid",method = RequestMethod.POST)
    public ReturnVO setDataByUid(@RequestParam("uid") Integer uid,@RequestParam("time")Integer time,@RequestParam(value = "num",required = false,defaultValue = "0")Integer num,@RequestParam(value = "num2",required = false,defaultValue = "0")Integer num2){
        returnVO = new ReturnVO();
        if (uid==null){
            returnVO.setCode(500);
            returnVO.setMessage("数据不为空");
            return returnVO;
        }else{
            Recitedatasum recitedatasum = new Recitedatasum();
            recitedatasum.setNum(num);
            recitedatasum.setCountnum(1);
            recitedatasum.setNum2(num2);
            recitedatasum.setTime(time);
            recitedatasum.setUid(uid);
            SubReturnVo subReturnVo = recitedatasumService.setData(recitedatasum);
            if (!subReturnVo.isResult()){
                returnVO.setCode(subReturnVo.getCode());
                returnVO.setMessage(subReturnVo.getMessage());
                return returnVO;
            }

            LocalDate nowDate =timeUtil.getCurrentTimeLocalDate();
            Map<String,Object> params = new HashMap<>();
            params.put("uid",uid);
            params.put("createdate",nowDate);
            Recitedata recitedata =  recitedataService.getOne(new QueryWrapper<Recitedata>().allEq(params));
            if (recitedata!=null){
               recitedata.setNum(recitedata.getNum()+num);
               recitedata.setNum2(recitedata.getNum2()+num2);
               recitedata.setTime(recitedata.getTime()+time);
               boolean result =recitedataService.updateById(recitedata);
               if (result){
                   returnVO.setCode(200);
                   returnVO.setMessage("更新存储成功");
                   return returnVO;
               }else{
                   returnVO.setCode(500);
                   returnVO.setMessage("更新存储失败");
                   return returnVO;
               }
            }else{
                recitedata = new Recitedata();
               recitedata.setUid(uid);
               recitedata.setCreatetime(timeUtil.getCurrentTimeLocalDateTime());
               recitedata.setNum(num);
               recitedata.setNum2(num2);
               recitedata.setDeleted(0);
               recitedata.setCreatedate(nowDate);
               recitedata.setTime(time);
               boolean result = recitedataService.save(recitedata);
               if (result){
                   returnVO.setCode(200);
                   returnVO.setMessage("存储成功");
                   return returnVO;
               }else{
                   returnVO.setCode(500);
                   returnVO.setMessage("存储失败");
                   return returnVO;
               }
            }



        }

    }
}
