package com.dqy.englishstudyapi.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dqy.englishstudyapi.entity.frontEntity.DataGraphyTable.DataGraphyTable;
import com.dqy.englishstudyapi.entity.frontEntity.DataGraphyTable.Series;
import com.dqy.englishstudyapi.entity.frontEntity.FrontScoreSource;
import com.dqy.englishstudyapi.entity.frontEntity.TestRecordFront;
import com.dqy.englishstudyapi.entity.page.MyPage;
import com.dqy.englishstudyapi.helper.RequestDataHelper;
import com.dqy.englishstudyapi.service.*;
import com.dqy.englishstudyapi.tablebean.*;
import com.dqy.englishstudyapi.util.JsonUtil;
import com.dqy.englishstudyapi.util.TimeUtil;
import com.dqy.englishstudyapi.vo.ReturnVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 邓亲优
 * @since 2023-03-01
 */
@RestController
@RequestMapping("testrecord")
public class TestrecordController {
    @Autowired
    TestrecordService testrecordService;
    @Autowired
    TesttypeService testtypeService;
    @Autowired
    ScoreService scoreService;
    @Autowired
    ScoresourceService scoresourceService;
    @Autowired
    ScoresourcetypeService scoresourcetypeService;
    @Autowired
    JsonUtil jsonUtil;
    @Autowired
    TimeUtil timeUtil;
    ReturnVO returnVO;

    Integer dataLength = 50;

    @RequestMapping(value = "/getByUidAndDay",method = RequestMethod.POST)
    public ReturnVO getDataByUidAndDay(@RequestParam("uid") Integer uid) {
        returnVO = new ReturnVO();
        if (uid == null) {
            returnVO.setCode(500);
            returnVO.setMessage("数据不为空");
            return returnVO;
        } else {
            ArrayList<DataGraphyTable>   dataGraphyTables = new ArrayList<>();
            DataGraphyTable tikuLearnTable = new DataGraphyTable();
            DataGraphyTable tikuTimeTable = new DataGraphyTable();
            DataGraphyTable examScoreTable = new DataGraphyTable();
            DataGraphyTable examTimeTable = new DataGraphyTable();

            ArrayList<Series> tikuLearnSeries= new ArrayList<>();
            ArrayList<Series> tikuTimeSeries= new ArrayList<>();
            ArrayList<Series> examScoreSeries= new ArrayList<>();
            ArrayList<Series> examTimeSeries= new ArrayList<>();



            LocalDateTime nowDateTime =timeUtil.getCurrentTimeLocalDateTime();
            LocalDate  startDate = timeUtil.getNowLocalDate();
            LocalDateTime startDateTime =startDate.atTime(0,0,0);

            List<Testrecord> testrecordsType0 =  testrecordService.list(new QueryWrapper<Testrecord>().and(i->i.eq("uid",uid).eq("type",0).between("updatetime",startDateTime,nowDateTime)));
            List<Testrecord> testrecordsType1 =  testrecordService.list(new QueryWrapper<Testrecord>().and(i->i.eq("uid",uid).eq("type",1).between("updatetime",startDateTime,nowDateTime)));


            if (testrecordsType0!=null&&testrecordsType0.size()!=0){


                //0-50
                if (testrecordsType0.size()>dataLength){
                    testrecordsType0 =testrecordsType0.subList(0,dataLength);
                }
                //添加横坐标
                tikuLearnTable.setCategories(timeUtil.getOnlyTimeNoDate(testrecordsType0));
                tikuTimeTable.setCategories(timeUtil.getOnlyTimeNoDate(testrecordsType0));

                Series tikuLearn0ALLSerie = new Series();
                Series tikuLearn1ErrorSerie = new Series();

                Series tikuTimeSerie = new Series();


                //serie 实例化
                tikuLearn0ALLSerie.setName("总题目数量");
                tikuLearn0ALLSerie.setTextColor("#ffffff");
                tikuLearn1ErrorSerie.setName("错题数量（个）");
                tikuLearn1ErrorSerie.setTextColor("#ffffff");

                tikuTimeSerie.setName("学习时长（分钟）");


                //serie->data 实例化
                ArrayList<Integer>  tikuLearn0ALLSerieData = new ArrayList<>();
                ArrayList<Integer>  tikuLearn1ErrorSerieData = new ArrayList<>();
                ArrayList<Integer>  tikuTimeSerieData = new ArrayList<>();

                //for 在这里
                Iterator<Testrecord> iterable = testrecordsType0.iterator();
                while (iterable.hasNext()){
                    Testrecord temp =iterable.next();
                    tikuLearn0ALLSerieData.add(temp.getErrornum()+temp.getCorrectnum());
                    tikuLearn1ErrorSerieData.add(temp.getErrornum());
                    tikuTimeSerieData.add(temp.getTime());
                }

                tikuLearn0ALLSerie.setData(tikuLearn0ALLSerieData);
                tikuLearn1ErrorSerie.setData(tikuLearn1ErrorSerieData);
                tikuTimeSerie.setData(tikuTimeSerieData);

                tikuLearnSeries.add(tikuLearn0ALLSerie);
                tikuLearnSeries.add(tikuLearn1ErrorSerie);

                tikuTimeSeries.add(tikuTimeSerie);


            }


            if (testrecordsType1!=null&&testrecordsType1.size()!=0){
                if (testrecordsType1.size()>dataLength){
                    testrecordsType1 =testrecordsType1.subList(0,dataLength);
                }
                examScoreTable.setCategories(timeUtil.getOnlyTimeNoDate(testrecordsType1));
                examTimeTable.setCategories(timeUtil.getOnlyTimeNoDate(testrecordsType1));

                Series examScoreSerie = new Series();
                Series examTimeSerie = new Series();

                examScoreSerie.setTextColor("#ffffff");

                examScoreSerie.setName("考试分数（分）");
                examTimeSerie.setName("学习时长（分钟）");

                //serie->data 实例化
                ArrayList<Integer>  examScoreSerieData = new ArrayList<>();
                ArrayList<Integer>  examTimeSerieData = new ArrayList<>();

                //for 在这里
                Iterator<Testrecord> iterable = testrecordsType1.iterator();
                while (iterable.hasNext()){
                    Testrecord temp =iterable.next();
                    examScoreSerieData.add(temp.getScore());
                    examTimeSerieData.add(temp.getTime());
                }

                examScoreSerie.setData(examScoreSerieData);
                examTimeSerie.setData( examTimeSerieData);

                examScoreSeries.add( examScoreSerie);
                examTimeSeries.add(examTimeSerie);
            }

            tikuLearnTable .setSeries(tikuLearnSeries);
             tikuTimeTable.setSeries(tikuTimeSeries);
            examScoreTable .setSeries(examScoreSeries);
            examTimeTable .setSeries(examTimeSeries);



            dataGraphyTables.add(tikuLearnTable);
            dataGraphyTables.add(tikuTimeTable);
            dataGraphyTables.add(examScoreTable);
            dataGraphyTables.add(examTimeTable);

            returnVO.setCode(200);
            returnVO.setMessage("获取成功");
            returnVO.setData( dataGraphyTables);
            return returnVO;
        }

    }

    @RequestMapping(value = "/getByUidAndWeek",method = RequestMethod.POST)
    public ReturnVO getDataByUidAndWeek(@RequestParam("uid") Integer uid) {
        returnVO = new ReturnVO();
        if (uid == null) {
            returnVO.setCode(500);
            returnVO.setMessage("数据不为空");
            return returnVO;
        } else {
            ArrayList<DataGraphyTable>   dataGraphyTables = new ArrayList<>();
            DataGraphyTable tikuLearnTable = new DataGraphyTable();
            DataGraphyTable tikuTimeTable = new DataGraphyTable();
            DataGraphyTable examScoreTable = new DataGraphyTable();
            DataGraphyTable examTimeTable = new DataGraphyTable();

            tikuLearnTable.setCategories(timeUtil.getRangeDateByDayToStringNotYear(timeUtil.getRangeDateByDay(7)));
            tikuTimeTable.setCategories(timeUtil.getRangeDateByDayToStringNotYear(timeUtil.getRangeDateByDay(7)));
            examScoreTable.setCategories(timeUtil.getRangeDateByDayToStringNotYear(timeUtil.getRangeDateByDay(7)));
            examTimeTable.setCategories(timeUtil.getRangeDateByDayToStringNotYear(timeUtil.getRangeDateByDay(7)));

            ArrayList<Series> tikuLearnSeries= new ArrayList<>();
            ArrayList<Series> tikuTimeSeries= new ArrayList<>();
            ArrayList<Series> examScoreSeries= new ArrayList<>();
            ArrayList<Series> examTimeSeries= new ArrayList<>();



            LocalDate nowDate =timeUtil.getCurrentTimeLocalDate();
            LocalDate weekAgoDate = timeUtil.differDay(nowDate, 6L);

            List<Testrecord> testrecordsType0 =  testrecordService.list(new QueryWrapper<Testrecord>().and(i->i.eq("uid",uid).eq("type",0).between("createdate",weekAgoDate,nowDate)));
            List<Testrecord> testrecordsType1 =  testrecordService.list(new QueryWrapper<Testrecord>().and(i->i.eq("uid",uid).eq("type",1).between("createdate",weekAgoDate,nowDate)));

            if (testrecordsType0!=null&&testrecordsType0.size()!=0){
                Series tikuLearn0ALLSerie = new Series();
                Series tikuLearn1ErrorSerie = new Series();

                Series tikuTimeSerie = new Series();


                //serie 实例化
                tikuLearn0ALLSerie.setName("日均题目数量");
                tikuLearn0ALLSerie.setTextColor("#ffffff");
                tikuLearn1ErrorSerie.setName("日均错题数量（个/分）");
                tikuLearn1ErrorSerie.setTextColor("#ffffff");

                tikuTimeSerie.setName("学习时长（分钟）");

                //serie->data 实例化
                ArrayList<Integer>  tikuLearn0ALLSerieData = new ArrayList<>();
                ArrayList<Integer>  tikuLearn1ErrorSerieData = new ArrayList<>();
                ArrayList<Integer>  tikuTimeSerieData = new ArrayList<>();
                for (int i = 6; i >= 0; i--) {
                    Iterator<Testrecord> iterable =  testrecordsType0.iterator();

                    Integer tikuLearn0ALLNum = 0;
                    Integer tikuLearn1ErrorNum = 0;
                    Integer tikuTimeNum = 0;
                    Integer count = 0;
                    while (iterable.hasNext()) {
                        Testrecord temp = iterable.next();
                        if (timeUtil.differDay(nowDate, (long) i).isEqual(temp.getCreatedate())){
                            count++;
                            tikuLearn0ALLNum+=(temp.getCorrectnum()+temp.getErrornum());
                            tikuLearn1ErrorNum+=(temp.getErrornum());
                            tikuTimeNum+=(temp.getTime());
                            iterable.remove();

                        }
                    }
                    if (count==0){
                        tikuLearn0ALLSerieData.add(0);
                        tikuLearn1ErrorSerieData.add(0);
                    }else{
                        tikuLearn0ALLSerieData.add(tikuLearn0ALLNum/count);
                        tikuLearn1ErrorSerieData.add(tikuLearn1ErrorNum/count);
                    }
                    tikuTimeSerieData.add(tikuTimeNum);

                }
                tikuLearn0ALLSerie.setData(tikuLearn0ALLSerieData);
                tikuLearn1ErrorSerie.setData(tikuLearn1ErrorSerieData);
                tikuTimeSerie.setData(tikuTimeSerieData);

                tikuLearnSeries.add(tikuLearn0ALLSerie);
                tikuLearnSeries.add(tikuLearn1ErrorSerie);

                tikuTimeSeries.add(tikuTimeSerie);


            }else{
                Series tikuLearn0ALLSerie = new Series();
                Series tikuLearn1ErrorSerie = new Series();

                Series tikuTimeSerie = new Series();


                //serie 实例化
                tikuLearn0ALLSerie.setName("日均题目数量");
                tikuLearn0ALLSerie.setTextColor("#ffffff");
                tikuLearn1ErrorSerie.setName("日均错题数量（个/分）");
                tikuLearn1ErrorSerie.setTextColor("#ffffff");

                tikuTimeSerie.setName("学习时长（分钟）");

                //serie->data 实例化
                ArrayList<Integer>  tikuLearn0ALLSerieData = new ArrayList<>();
                ArrayList<Integer>  tikuLearn1ErrorSerieData = new ArrayList<>();
                ArrayList<Integer>  tikuTimeSerieData = new ArrayList<>();
                for (int i = 6; i >= 0; i--) {
                    tikuLearn0ALLSerieData.add(0);
                    tikuLearn1ErrorSerieData.add(0);
                    tikuTimeSerieData.add(0);
                }
                tikuLearn0ALLSerie.setData(tikuLearn0ALLSerieData);
                tikuLearn1ErrorSerie.setData(tikuLearn1ErrorSerieData);
                tikuTimeSerie.setData(tikuTimeSerieData);

                tikuLearnSeries.add(tikuLearn0ALLSerie);
                tikuLearnSeries.add(tikuLearn1ErrorSerie);

                tikuTimeSeries.add(tikuTimeSerie);
            }

            if (testrecordsType1!=null&&testrecordsType1.size()!=0){
                Series examScoreSerie = new Series();
                Series examTimeSerie = new Series();

                examScoreSerie.setTextColor("#ffffff");

                examScoreSerie.setName("日均考试分数（分/天）");
                examTimeSerie.setName("学习时长（分钟）");

                //serie->data 实例化
                ArrayList<Integer>  examScoreSerieData = new ArrayList<>();
                ArrayList<Integer>  examTimeSerieData = new ArrayList<>();

                for (int i = 6; i >= 0; i--) {
                    Iterator<Testrecord> iterable =  testrecordsType1.iterator();

                    Integer examScoreNum = 0;
                    Integer examTimeNum = 0;
                    Integer count = 0;
                    while (iterable.hasNext()) {
                        Testrecord temp = iterable.next();
                        if (timeUtil.differDay(nowDate, (long) i).isEqual(temp.getCreatedate())){
                            count++;
                            examScoreNum+=(temp.getScore());
                            examTimeNum+=(temp.getTime());
                            iterable.remove();

                        }
                    }
                    if (count==0){
                        examScoreSerieData.add(0);
                    }else{
                        examScoreSerieData.add(examScoreNum/count);
                    }
                    examTimeSerieData.add(examTimeNum);
                }

                examScoreSerie.setData(examScoreSerieData);
                examTimeSerie.setData( examTimeSerieData);

                examScoreSeries.add( examScoreSerie);
                examTimeSeries.add(examTimeSerie);

            }else{
                Series examScoreSerie = new Series();
                Series examTimeSerie = new Series();

                examScoreSerie.setTextColor("#ffffff");

                examScoreSerie.setName("日均考试分数（分/天）");
                examTimeSerie.setName("学习时长（分钟）");

                //serie->data 实例化
                ArrayList<Integer>  examScoreSerieData = new ArrayList<>();
                ArrayList<Integer>  examTimeSerieData = new ArrayList<>();

                for (int i = 6; i >= 0; i--) {
                        examScoreSerieData.add(0);
                        examScoreSerieData.add(0);
                        examTimeSerieData.add(0);
                }

                examScoreSerie.setData(examScoreSerieData);
                examTimeSerie.setData( examTimeSerieData);

                examScoreSeries.add( examScoreSerie);
                examTimeSeries.add(examTimeSerie);
            }
            tikuLearnTable .setSeries(tikuLearnSeries);
            tikuTimeTable.setSeries(tikuTimeSeries);
            examScoreTable .setSeries(examScoreSeries);
            examTimeTable .setSeries(examTimeSeries);



            dataGraphyTables.add(tikuLearnTable);
            dataGraphyTables.add(tikuTimeTable);
            dataGraphyTables.add(examScoreTable);
            dataGraphyTables.add(examTimeTable);

            returnVO.setCode(200);
            returnVO.setMessage("获取成功");
            returnVO.setData( dataGraphyTables);
            return returnVO;

        }

    }

    @RequestMapping(value = "/getByUidAndMonth",method = RequestMethod.POST)
    public ReturnVO getDataByUidAndMonth(@RequestParam("uid") Integer uid) {
        returnVO = new ReturnVO();
        if (uid == null) {
            returnVO.setCode(500);
            returnVO.setMessage("数据不为空");
            return returnVO;
        } else {
            ArrayList<DataGraphyTable>   dataGraphyTables = new ArrayList<>();
            DataGraphyTable tikuLearnTable = new DataGraphyTable();
            DataGraphyTable tikuTimeTable = new DataGraphyTable();
            DataGraphyTable examScoreTable = new DataGraphyTable();
            DataGraphyTable examTimeTable = new DataGraphyTable();

            tikuLearnTable.setCategories(timeUtil.getRangeDateByDayToStringNotDay(timeUtil.getRangeDateByMonth(12)));
            tikuTimeTable.setCategories(timeUtil.getRangeDateByDayToStringNotDay(timeUtil.getRangeDateByMonth(12)));
            examScoreTable.setCategories(timeUtil.getRangeDateByDayToStringNotDay(timeUtil.getRangeDateByMonth(12)));
            examTimeTable.setCategories(timeUtil.getRangeDateByDayToStringNotDay(timeUtil.getRangeDateByMonth(12)));

            ArrayList<Series> tikuLearnSeries= new ArrayList<>();
            ArrayList<Series> tikuTimeSeries= new ArrayList<>();
            ArrayList<Series> examScoreSeries= new ArrayList<>();
            ArrayList<Series> examTimeSeries= new ArrayList<>();



            LocalDate nowDate =timeUtil.getCurrentTimeLocalDate();
            LocalDate monthAgoDate = timeUtil.differMonth(nowDate, 11L);

            List<Testrecord> testrecordsType0 =  testrecordService.list(new QueryWrapper<Testrecord>().and(i->i.eq("uid",uid).eq("type",0).between("createdate",LocalDate.of(monthAgoDate.getYear(),monthAgoDate.getMonth(),1),nowDate)));
            List<Testrecord> testrecordsType1 =  testrecordService.list(new QueryWrapper<Testrecord>().and(i->i.eq("uid",uid).eq("type",1).between("createdate",LocalDate.of(monthAgoDate.getYear(),monthAgoDate.getMonth(),1),nowDate)));

            if (testrecordsType0!=null&&testrecordsType0.size()!=0){
                Series tikuLearn0ALLSerie = new Series();
                Series tikuLearn1ErrorSerie = new Series();

                Series tikuTimeSerie = new Series();


                //serie 实例化
                tikuLearn0ALLSerie.setName("月均题目数量");
                tikuLearn0ALLSerie.setTextColor("#ffffff");
                tikuLearn1ErrorSerie.setName("月均错题数量（个/分）");
                tikuLearn1ErrorSerie.setTextColor("#ffffff");

                tikuTimeSerie.setName("学习时长（分钟）");

                //serie->data 实例化
                ArrayList<Integer>  tikuLearn0ALLSerieData = new ArrayList<>();
                ArrayList<Integer>  tikuLearn1ErrorSerieData = new ArrayList<>();
                ArrayList<Integer>  tikuTimeSerieData = new ArrayList<>();
                for (int i = 11; i >=0 ; i--) {
                    Iterator<Testrecord> iterable =  testrecordsType0.iterator();

                    Integer tikuLearn0ALLNum = 0;
                    Integer tikuLearn1ErrorNum = 0;
                    Integer tikuTimeNum = 0;
                    Integer count = 0;
                    while (iterable.hasNext()) {
                        Testrecord temp = iterable.next();
                        if (timeUtil.differMonth(nowDate, (long) i).getMonthValue()==temp.getCreatedate().getMonthValue()){
                            count++;
                            tikuLearn0ALLNum+=(temp.getCorrectnum()+temp.getErrornum());
                            tikuLearn1ErrorNum+=(temp.getErrornum());
                            tikuTimeNum+=(temp.getTime());
                            iterable.remove();

                        }
                    }
                    if (count==0){
                        tikuLearn0ALLSerieData.add(0);
                        tikuLearn1ErrorSerieData.add(0);
                    }else{
                        tikuLearn0ALLSerieData.add(tikuLearn0ALLNum/count);
                        tikuLearn1ErrorSerieData.add(tikuLearn1ErrorNum/count);
                    }
                    tikuTimeSerieData.add(tikuTimeNum);

                }
                tikuLearn0ALLSerie.setData(tikuLearn0ALLSerieData);
                tikuLearn1ErrorSerie.setData(tikuLearn1ErrorSerieData);
                tikuTimeSerie.setData(tikuTimeSerieData);

                tikuLearnSeries.add(tikuLearn0ALLSerie);
                tikuLearnSeries.add(tikuLearn1ErrorSerie);

                tikuTimeSeries.add(tikuTimeSerie);


            }else {
                Series tikuLearn0ALLSerie = new Series();
                Series tikuLearn1ErrorSerie = new Series();

                Series tikuTimeSerie = new Series();


                //serie 实例化
                tikuLearn0ALLSerie.setName("月均题目数量");
                tikuLearn0ALLSerie.setTextColor("#ffffff");
                tikuLearn1ErrorSerie.setName("月均错题数量（个/分）");
                tikuLearn1ErrorSerie.setTextColor("#ffffff");

                tikuTimeSerie.setName("学习时长（分钟）");

                //serie->data 实例化
                ArrayList<Integer>  tikuLearn0ALLSerieData = new ArrayList<>();
                ArrayList<Integer>  tikuLearn1ErrorSerieData = new ArrayList<>();
                ArrayList<Integer>  tikuTimeSerieData = new ArrayList<>();
                for (int i = 11; i >=0 ; i--) {


                        tikuLearn0ALLSerieData.add(0);
                        tikuLearn1ErrorSerieData.add(0);
                        tikuTimeSerieData.add(0);

                }
                tikuLearn0ALLSerie.setData(tikuLearn0ALLSerieData);
                tikuLearn1ErrorSerie.setData(tikuLearn1ErrorSerieData);
                tikuTimeSerie.setData(tikuTimeSerieData);

                tikuLearnSeries.add(tikuLearn0ALLSerie);
                tikuLearnSeries.add(tikuLearn1ErrorSerie);

                tikuTimeSeries.add(tikuTimeSerie);
            }

            if (testrecordsType1!=null&&testrecordsType1.size()!=0){
                Series examScoreSerie = new Series();
                Series examTimeSerie = new Series();

                examScoreSerie.setTextColor("#ffffff");

                examScoreSerie.setName("月均考试分数（分/天）");
                examTimeSerie.setName("学习时长（分钟）");

                //serie->data 实例化
                ArrayList<Integer>  examScoreSerieData = new ArrayList<>();
                ArrayList<Integer>  examTimeSerieData = new ArrayList<>();

                for (int i = 11; i >= 0; i--) {
                    Iterator<Testrecord> iterable =  testrecordsType1.iterator();

                    Integer examScoreNum = 0;
                    Integer examTimeNum = 0;
                    Integer count = 0;
                    while (iterable.hasNext()) {
                        Testrecord temp = iterable.next();
                        if (timeUtil.differMonth(nowDate, (long) i).getMonthValue()==temp.getCreatedate().getMonthValue()){
                            count++;
                            examScoreNum+=(temp.getScore());
                            examTimeNum+=(temp.getTime());
                            iterable.remove();

                        }
                    }
                    if (count==0){
                        examScoreSerieData.add(0);
                    }else{
                        examScoreSerieData.add(examScoreNum/count);
                    }
                    examTimeSerieData.add(examTimeNum);
                }

                examScoreSerie.setData(examScoreSerieData);
                examTimeSerie.setData( examTimeSerieData);

                examScoreSeries.add( examScoreSerie);
                examTimeSeries.add(examTimeSerie);

            }else {
                Series examScoreSerie = new Series();
                Series examTimeSerie = new Series();

                examScoreSerie.setTextColor("#ffffff");

                examScoreSerie.setName("月均考试分数（分/天）");
                examTimeSerie.setName("学习时长（分钟）");

                //serie->data 实例化
                ArrayList<Integer>  examScoreSerieData = new ArrayList<>();
                ArrayList<Integer>  examTimeSerieData = new ArrayList<>();

                for (int i = 11; i >= 0; i--) {
                        examScoreSerieData.add(0);
                        examScoreSerieData.add(0);
                        examTimeSerieData.add(0);
                }

                examScoreSerie.setData(examScoreSerieData);
                examTimeSerie.setData( examTimeSerieData);

                examScoreSeries.add( examScoreSerie);
                examTimeSeries.add(examTimeSerie);
            }
            tikuLearnTable .setSeries(tikuLearnSeries);
            tikuTimeTable.setSeries(tikuTimeSeries);
            examScoreTable .setSeries(examScoreSeries);
            examTimeTable .setSeries(examTimeSeries);



            dataGraphyTables.add(tikuLearnTable);
            dataGraphyTables.add(tikuTimeTable);
            dataGraphyTables.add(examScoreTable);
            dataGraphyTables.add(examTimeTable);

            returnVO.setCode(200);
            returnVO.setMessage("获取成功");
            returnVO.setData( dataGraphyTables);
            return returnVO;

        }

    }

    @PostMapping("/get")
    public ReturnVO get(@RequestParam("uid") Integer uid,
                        @RequestParam(value = "current",defaultValue = "1",required = false)Integer current,@RequestParam(value = "size",defaultValue = "3",required = false)Integer size){
        returnVO = new ReturnVO();
        if ( uid==null){
            returnVO.setCode(500);
            returnVO.setMessage("数据为空");
            return  returnVO;
        }else{

            Page<Testrecord> page = new Page<>();
            page.setCurrent(current);
            page.setSize(size);
            IPage<Testrecord> IPage =  testrecordService.page(page,new QueryWrapper<Testrecord>().eq("uid",uid).orderByDesc("updatetime"));
            if (IPage.getRecords()!=null&&IPage.getRecords().size()!=0){
                List<Testrecord> testrecords =IPage.getRecords();
                ArrayList<TestRecordFront> datas = new ArrayList<>();
                for (Testrecord tr:testrecords
                     ) {
                    Testtype testtype = testtypeService.getById(tr.getTesttype());
                    TestRecordFront data =new TestRecordFront();
                    data.setTestrecord(tr);
                    data.setTesttype(testtype);
                    datas.add(data);
                }
                MyPage<TestRecordFront> myPage = new MyPage<>();
                myPage.setData(datas);
                myPage.setPageSize(Math.toIntExact(IPage.getSize()));
                myPage.setTotal(Math.toIntExact(IPage.getTotal()));
                myPage.setCurrent(Math.toIntExact(IPage.getCurrent()));
                returnVO.setCode(200);
                returnVO.setMessage("获取成功");
                returnVO.setData( myPage);
                return returnVO;
            }else{
                returnVO.setCode(500);
                returnVO.setMessage("获取失败");
                return returnVO;
            }
        }
    }

    @PostMapping("/set")
    public ReturnVO set(@RequestBody Testrecord testrecord){
        returnVO = new ReturnVO();
        if (testrecord==null){
            returnVO.setCode(500);
            returnVO.setMessage("数据为空");
            return  returnVO;
        }else{
            testrecord.setCreatetime(timeUtil.getNowLocalDateTime());
            testrecord.setCreatedate(timeUtil.getNowLocalDate());
            testrecord.setDeleted(0);
            boolean result =testrecordService.save(testrecord);
            if (result){
                Score score = scoreService.getOne(new QueryWrapper<Score>().eq("uid",testrecord.getUid()));
                if (score!=null){
                    Integer id =null;
                    if (testrecord.getType()==0){
                        id=6;
                    }else{
                        id=14;
                    }
                    Scoresourcetype scoresourcetype = scoresourcetypeService.getById(id);
                    if (scoresourcetype!=null){
                        score.setScore(score.getScore()+scoresourcetype.getDefaults());
                        boolean result2 =  scoreService.updateById(score);
                        if (result2){
                            Scoresource scoresource = new Scoresource();
                            scoresource.setSourceid(id);
                            scoresource.setNum(Long.valueOf(scoresourcetype.getDefaults()));
                            scoresource.setDeleted(0);
                            scoresource.setUid(testrecord.getUid());
                            scoresource.setCreatetime(timeUtil.getCurrentTimeLocalDateTime());
                            boolean result3= scoresourceService.save(scoresource);
                            if (result3){
                                returnVO.setCode(200);
                                returnVO.setMessage("保存记录成功");
                                return  returnVO;
                            }else{
                                returnVO.setCode(500);
                                returnVO.setMessage("添加scoresourse失败");
                                return returnVO;
                            }
                        }else{
                            returnVO.setCode(500);
                            returnVO.setMessage("更新score失败");
                            return returnVO;
                        }
                    }else{
                        returnVO.setCode(500);
                        returnVO.setMessage("获取scoresourcetype失败");
                        return returnVO;
                    }
                }else{
                    returnVO.setCode(500);
                    returnVO.setMessage("获取积分失败");
                    return returnVO;
                }

            }else{
                returnVO.setCode(500);
                returnVO.setMessage("保存记录失败");
                return  returnVO;
            }
        }
    }
}
