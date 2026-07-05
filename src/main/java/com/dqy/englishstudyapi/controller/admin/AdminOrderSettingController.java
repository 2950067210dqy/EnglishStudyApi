package com.dqy.englishstudyapi.controller.admin;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dqy.englishstudyapi.entity.adminEntity.BasicSetting.BasicOrderSetting.BasicOrderSetting;
import com.dqy.englishstudyapi.entity.adminEntity.BasicSetting.BasicWordSetting.BasicWordSetting;
import com.dqy.englishstudyapi.entity.frontEntity.DataGraphyTable.DataGraphyTable;
import com.dqy.englishstudyapi.entity.frontEntity.DataGraphyTable.Pie.PieGraphyTable;
import com.dqy.englishstudyapi.entity.frontEntity.DataGraphyTable.Pie.SeriesItem;
import com.dqy.englishstudyapi.entity.frontEntity.DataGraphyTable.Pie.SeriesPie;
import com.dqy.englishstudyapi.entity.frontEntity.DataGraphyTable.Series;
import com.dqy.englishstudyapi.service.OrdersService;
import com.dqy.englishstudyapi.service.OrderstatusService;
import com.dqy.englishstudyapi.service.WordService;
import com.dqy.englishstudyapi.tablebean.Orders;
import com.dqy.englishstudyapi.tablebean.Orderstatus;
import com.dqy.englishstudyapi.util.ListUtil;
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
import java.util.List;

@RestController
@RequestMapping("adminOrderSetting")
public class AdminOrderSettingController {
    @Autowired
    OrdersService ordersService;
    @Autowired
    OrderstatusService orderstatusService;
    @Autowired
    TimeUtil timeUtil;
    @Autowired
    ListUtil listUtil;
    ReturnVO returnVO;

    @PostMapping("/getBasicSetting")
    public ReturnVO getBasicSetting(){
        returnVO = new ReturnVO();
        Long todayCount = 0L;
        Long allCount = 0L;
        LocalDateTime nowDateTime =timeUtil.getCurrentTimeLocalDateTime();
        LocalDate startDate = timeUtil.getNowLocalDate();
        LocalDateTime startDateTime =startDate.atTime(0,0,0);

        List<Orders> orderss= ordersService.list();
        if (orderss!=null&& orderss.size()!=0){
            allCount+=orderss.size();
        }else{
            allCount+=0L;
        }

        List<Orders> orderss2= ordersService.list(new QueryWrapper<Orders>().eq("orderstatus",1).between("updatetime",startDateTime,nowDateTime));
        if (orderss2!=null&& orderss2.size()!=0){
            todayCount+=orderss2.size();
        }else{
            todayCount+=0L;
        }

        PieGraphyTable pieGraphyTable = new PieGraphyTable();
        List<SeriesPie> seriesPieList = new ArrayList<>();
        SeriesPie seriesPie = new SeriesPie();
        List<SeriesItem> seriesItems = new ArrayList<>();
        List<Orderstatus> orderstatuses =orderstatusService.list();
        if (orderstatuses!=null&&orderstatuses.size()!=0){
            for (Orderstatus os:orderstatuses
            ) {
                SeriesItem seriesItem = new SeriesItem();
                seriesItem.setName(os.getDsc());
                List<Orders> ordersList = ordersService.list(new QueryWrapper<Orders>().eq("orderstatus",os.getId()));
                if (ordersList!=null&&ordersList.size()!=0){
                    seriesItem.setValue(ordersList.size());
                }else{
                    seriesItem.setValue(0);
                }

                seriesItems.add(seriesItem);
            }
        }
        seriesPie.setData(seriesItems);
        seriesPieList.add(seriesPie);
        pieGraphyTable.setSeries(seriesPieList);
        BasicOrderSetting basicOrderSetting = new BasicOrderSetting();
        basicOrderSetting.setTodayCount(todayCount);
        basicOrderSetting.setAllCount(allCount);
        basicOrderSetting.setData(pieGraphyTable);
        returnVO.setCode(200);
        returnVO.setMessage("获取成功");
        returnVO.setData(basicOrderSetting);
        return  returnVO;
    }
}
