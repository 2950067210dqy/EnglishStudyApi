package com.dqy.englishstudyapi.controller.admin;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dqy.englishstudyapi.entity.adminEntity.BasicSetting.BasicCikuSetting.BasicCikuSettingNode;
import com.dqy.englishstudyapi.entity.adminEntity.BasicSetting.BasicProductSetting.BasicProductSetting;
import com.dqy.englishstudyapi.entity.adminEntity.BasicSetting.BasicProductSetting.BasicProductSettingNode;
import com.dqy.englishstudyapi.entity.adminEntity.BasicSetting.BasicUserSetting;
import com.dqy.englishstudyapi.service.ProductsService;
import com.dqy.englishstudyapi.service.PtypeService;
import com.dqy.englishstudyapi.service.PtypesubService;
import com.dqy.englishstudyapi.service.UserService;
import com.dqy.englishstudyapi.tablebean.Products;
import com.dqy.englishstudyapi.tablebean.Ptype;
import com.dqy.englishstudyapi.tablebean.Ptypesub;
import com.dqy.englishstudyapi.tablebean.User;
import com.dqy.englishstudyapi.util.TimeUtil;
import com.dqy.englishstudyapi.vo.ReturnVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("adminProductSetting")
public class AdminProductSettingController {
    @Autowired
    ProductsService productsService;
    @Autowired
    PtypeService ptypeService;
    @Autowired
    PtypesubService ptypesubService;
    @Autowired
    UserService userService;
    @Autowired
    TimeUtil timeUtil;
    ReturnVO returnVO;

    @PostMapping("/getBasicSetting")
    public ReturnVO getBasicSetting(){
        returnVO = new ReturnVO();
        Long allCount = productsService.count();
        if (allCount==null){
            allCount=0L;
        }
        LocalDateTime nowDateTime =timeUtil.getCurrentTimeLocalDateTime();
        LocalDate startDate = timeUtil.getNowLocalDate();
        LocalDateTime startDateTime =startDate.atTime(0,0,0);
        Long todayCount = productsService.count(new QueryWrapper<Products>().between("time", startDateTime,nowDateTime));
        if (todayCount==null){
            todayCount=0L;
        }


        List<Products> productsLast = productsService.list(new QueryWrapper<Products>().orderByDesc("time").last(" limit 0,5 "));

        List<BasicProductSettingNode> nodes = new ArrayList<>();
        if (productsLast!=null&&productsLast.size()!=0){
            for (Products pd:productsLast
                 ) {
                BasicProductSettingNode basicProductSettingNode = new BasicProductSettingNode();
                basicProductSettingNode.setProducts(pd);
                Ptype ptype = ptypeService.getById(pd.getPtypeid());
                if (ptype!=null){
                    basicProductSettingNode.setParentDsc(ptype.getDsc());
                }else{
                    basicProductSettingNode.setParentDsc("");
                }
                Ptypesub ptypesub = ptypesubService.getById(pd.getPtypesubid());
                if (ptypesub!=null){
                    basicProductSettingNode.setDsc(ptypesub.getDsc());
                }else{
                    basicProductSettingNode.setDsc("");
                }
                basicProductSettingNode.setMerchant(userService.getById(pd.getMerchantId()));
                nodes.add(basicProductSettingNode);
            }
        }

        BasicProductSetting basicProductSetting = new BasicProductSetting();
        basicProductSetting.setNodes(nodes);
        basicProductSetting.setTodayCount(todayCount);
        basicProductSetting.setAllCount(allCount);
        returnVO.setCode(200);
        returnVO.setMessage("获取成功");
        returnVO.setData(basicProductSetting);
        return  returnVO;
    }
}
