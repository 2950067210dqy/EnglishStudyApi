package com.dqy.englishstudyapi.controller.admin;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dqy.englishstudyapi.entity.adminEntity.BasicSetting.BasicUserSetting;
import com.dqy.englishstudyapi.entity.adminEntity.BasicSetting.BasicWordSetting.BasicWordSetting;
import com.dqy.englishstudyapi.entity.frontEntity.DataGraphyTable.DataGraphyTable;
import com.dqy.englishstudyapi.entity.frontEntity.DataGraphyTable.Pie.PieGraphyTable;
import com.dqy.englishstudyapi.entity.frontEntity.DataGraphyTable.Pie.SeriesItem;
import com.dqy.englishstudyapi.entity.frontEntity.DataGraphyTable.Pie.SeriesPie;
import com.dqy.englishstudyapi.entity.frontEntity.DataGraphyTable.Series;
import com.dqy.englishstudyapi.service.UserService;
import com.dqy.englishstudyapi.service.WordService;
import com.dqy.englishstudyapi.tablebean.User;
import com.dqy.englishstudyapi.util.ListUtil;
import com.dqy.englishstudyapi.util.TimeUtil;
import com.dqy.englishstudyapi.util.WordUtil;
import com.dqy.englishstudyapi.vo.ReturnVO;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("adminWordSetting")
public class AdminWordSettingController {
    @Autowired
    WordService wordService;
    @Autowired
    TimeUtil timeUtil;
    @Autowired
    ListUtil listUtil;
    ReturnVO returnVO;

    @PostMapping("/getBasicSetting")
    public ReturnVO getBasicSetting(){
        returnVO = new ReturnVO();
        Long allCount = 0L;
        DataGraphyTable dataGraphyTable = new DataGraphyTable();
        ArrayList<String> categories = new ArrayList<>();

        ArrayList<Series> seriesArrayList = new ArrayList<>();
       Series series = new Series();
       series.setName("单词个数");
       ArrayList<Integer> data = new ArrayList<>();
        for (Character initial:listUtil.abs
             ) {

            categories.add(initial.toString());
            wordService.createTable(initial.toString());
            Integer count = wordService.count(initial.toString());
            if (count!=null){
                allCount+=count;
                data.add(count);
            }else{
                allCount+=0L;
                data.add(0);
            }


        }
        series.setData(data);
        seriesArrayList.add(series);
        dataGraphyTable.setSeries(seriesArrayList);
        dataGraphyTable.setCategories(categories);
        BasicWordSetting basicWordSetting = new BasicWordSetting();
        basicWordSetting.setAllCount(allCount);
        basicWordSetting.setData(dataGraphyTable);
        returnVO.setCode(200);
        returnVO.setMessage("获取成功");
        returnVO.setData(basicWordSetting);
        return  returnVO;
    }
}
