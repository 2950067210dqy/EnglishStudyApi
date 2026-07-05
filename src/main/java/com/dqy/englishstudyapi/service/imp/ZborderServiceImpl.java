package com.dqy.englishstudyapi.service.imp;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dqy.englishstudyapi.entity.frontEntity.DataGraphyTable.DataGraphyTable;
import com.dqy.englishstudyapi.entity.frontEntity.DataGraphyTable.Series;
import com.dqy.englishstudyapi.tablebean.Testrecord;
import com.dqy.englishstudyapi.tablebean.Zborder;
import com.dqy.englishstudyapi.mapper.ZborderMapper;
import com.dqy.englishstudyapi.service.ZborderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dqy.englishstudyapi.util.TimeUtil;
import com.dqy.englishstudyapi.vo.ReturnVO;
import com.dqy.englishstudyapi.vo.SubReturnVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 邓亲优
 * @since 2023-02-20
 */
@Service
//开启事务回滚
@Transactional(rollbackFor = RuntimeException.class)
public class ZborderServiceImpl extends ServiceImpl<ZborderMapper, Zborder> implements ZborderService {
    @Autowired
    TimeUtil timeUtil;
    Integer dataLength=50;
    @Override
    public SubReturnVo cancel(Integer id) {
        SubReturnVo subReturnVO = new SubReturnVo();
        Zborder zborder = getById(id);
        if (zborder != null) {
            zborder.setStatus(2);
            boolean result = updateById(zborder);
            if (result) {
                subReturnVO.setCode(200);
                subReturnVO.setMessage("更改成功");
                subReturnVO.setResult(true);
                return subReturnVO;
            } else {
                subReturnVO.setCode(500);
                subReturnVO.setMessage("更改失败");
                subReturnVO.setResult(false);
                return subReturnVO;
            }
        }else{
            subReturnVO.setCode(500);
            subReturnVO.setMessage("获取失败");
            subReturnVO.setResult(false);
            return subReturnVO;
        }
    }




    public SubReturnVo getDataByUidAndDay() {
        SubReturnVo subReturnVo = new SubReturnVo();

        DataGraphyTable dataGraphyTable = new DataGraphyTable();
        ArrayList<Series> seriesArrayList = new ArrayList<>();
        LocalDateTime nowDateTime =timeUtil.getCurrentTimeLocalDateTime();
        LocalDate startDate = timeUtil.getNowLocalDate();
        LocalDateTime startDateTime =startDate.atTime(0,0,0);

        List<Zborder> zborders =  list(new QueryWrapper<Zborder>().eq("status",1).between("updatetime",startDateTime,nowDateTime));
       if (zborders!=null&&zborders.size()!=0){
           //0-50
           if (zborders.size()>dataLength){
            zborders=zborders.subList(0,dataLength);
           }

           dataGraphyTable.setCategories(timeUtil.getOnlyTimeNoDateZborder(zborders));
           Series series = new Series();
           series.setName("流水 （元）");
           ArrayList<Integer> data = new ArrayList<>();
           for (Zborder zb:zborders
           ) {
               data.add(zb.getMoney().intValue());
           }
           series.setData(data);
           seriesArrayList.add(series);
           dataGraphyTable.setSeries(seriesArrayList);
           subReturnVo.setResult(true);
           subReturnVo.setCode(200);
           subReturnVo.setMessage("获取成功");
           subReturnVo.setData(dataGraphyTable);
           return subReturnVo;
       }else {
           subReturnVo.setResult(false);
           subReturnVo.setCode(500);
           subReturnVo.setMessage("获取失败");
           return subReturnVo;
       }

    }


    public SubReturnVo getDataByUidAndWeek() {

        SubReturnVo subReturnVo = new SubReturnVo();

        DataGraphyTable dataGraphyTable = new DataGraphyTable();
        ArrayList<Series> seriesArrayList = new ArrayList<>();
        LocalDate nowDate =timeUtil.getCurrentTimeLocalDate();
        LocalDate weekAgoDate = timeUtil.differDay(nowDate, 6L);
        LocalDateTime nowDateTime  =timeUtil.getCurrentTimeLocalDateTime();
        LocalDateTime weekAgoDateTime = timeUtil.differDay(nowDateTime, 6L);
        List<Zborder> zborders =  list(new QueryWrapper<Zborder>().eq("status",1).between("updatetime",weekAgoDateTime,nowDateTime));
        if (zborders!=null&&zborders.size()!=0){
            //0-50
            if (zborders.size()>dataLength){
                zborders=zborders.subList(0,dataLength);
            }
            dataGraphyTable.setCategories(timeUtil.getRangeDateByDayToStringNotYear(timeUtil.getRangeDateByDay(7)));
            Series series = new Series();
            series.setName("流水 （元）");
            ArrayList<Integer> data = new ArrayList<>();
            for (int i = 6; i >= 0; i--) {
                Iterator<Zborder> iterable =  zborders.iterator();
                Integer count = 0;
                Integer money = 0;
                while (iterable.hasNext()) {
                    Zborder temp = iterable.next();
                    if (timeUtil.differDay(nowDate, (long) i).isEqual(temp.getUpdatetime().toLocalDate())){
                        count++;
                        money+=temp.getMoney().intValue();
                        iterable.remove();

                    }
                }
                if (count==0){
                   data.add(0);
                }else{
                    data.add(money);
                }


            }

            series.setData(data);
            seriesArrayList.add(series);
            dataGraphyTable.setSeries(seriesArrayList);
            subReturnVo.setResult(true);
            subReturnVo.setCode(200);
            subReturnVo.setMessage("获取成功");
            subReturnVo.setData(dataGraphyTable);
            return subReturnVo;
        }else {
            subReturnVo.setResult(false);
            subReturnVo.setCode(500);
            subReturnVo.setMessage("获取失败");
            return subReturnVo;
        }

    }


    public SubReturnVo getDataByUidAndMonth() {

        SubReturnVo subReturnVo = new SubReturnVo();

        DataGraphyTable dataGraphyTable = new DataGraphyTable();
        ArrayList<Series> seriesArrayList = new ArrayList<>();
        LocalDate nowDate =timeUtil.getCurrentTimeLocalDate();
        LocalDate monthAgoDate = timeUtil.differMonth(nowDate, 11L);
        LocalDateTime nowDateTime  =timeUtil.getCurrentTimeLocalDateTime();
        LocalDateTime monthAgoDateTime = timeUtil.differMonth(nowDateTime, 11L);
        List<Zborder> zborders =  list(new QueryWrapper<Zborder>().eq("status",1).between("updatetime",monthAgoDateTime,nowDateTime));
        if (zborders!=null&&zborders.size()!=0) {
            //0-50
            if (zborders.size() > dataLength) {
                zborders = zborders.subList(0, dataLength);
            }

            dataGraphyTable.setCategories(timeUtil.getRangeDateByDayToStringNotDay(timeUtil.getRangeDateByMonth(12)));
            Series series = new Series();
            series.setName("流水 （元）");
            ArrayList<Integer> data = new ArrayList<>();
            for (int i = 11; i >= 0; i--) {
                Iterator<Zborder> iterable = zborders.iterator();
                Integer count = 0;
                Integer money = 0;
                while (iterable.hasNext()) {
                    Zborder temp = iterable.next();
                    if (timeUtil.differMonth(nowDate, (long) i).getMonthValue() == temp.getCreatetime().getMonthValue()){
                        count++;
                        money += temp.getMoney().intValue();
                        iterable.remove();
                    }


                }
                if (count == 0) {
                    data.add(0);
                } else {
                    data.add(money);
                }
            }

            series.setData(data);
            seriesArrayList.add(series);
            dataGraphyTable.setSeries(seriesArrayList);
            subReturnVo.setResult(true);
            subReturnVo.setCode(200);
            subReturnVo.setMessage("获取成功");
            subReturnVo.setData(dataGraphyTable);
            return subReturnVo;
        }else {
            subReturnVo.setResult(false);
            subReturnVo.setCode(500);
            subReturnVo.setMessage("获取失败");
            return subReturnVo;
        }


    }













}
