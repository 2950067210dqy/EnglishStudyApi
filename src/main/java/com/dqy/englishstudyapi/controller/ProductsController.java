package com.dqy.englishstudyapi.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dqy.englishstudyapi.entity.page.MyPage;
import com.dqy.englishstudyapi.service.ProductsService;
import com.dqy.englishstudyapi.tablebean.Products;
import com.dqy.englishstudyapi.util.TimeUtil;
import com.dqy.englishstudyapi.vo.ReturnVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 邓亲优
 * @since 2023-02-21
 */
@RestController
@RequestMapping("products")
public class ProductsController {
    @Autowired
    ProductsService productsService;
    @Autowired
    TimeUtil timeUtil;
    ReturnVO returnVO;

    @PostMapping("/getOne")
    public ReturnVO getOne(@RequestParam("id")Integer id

    ){
        returnVO = new ReturnVO();

        Products products = productsService.getById(id);

        if (products!=null){

            returnVO.setCode(200);
            returnVO.setMessage("获取成功");
            returnVO.setData(products);
            return returnVO;
        }else{
            returnVO.setCode(500);
            returnVO.setMessage("获取失败");
            return returnVO;
        }

    }


    @PostMapping("/getPage")
    public ReturnVO getPage(@RequestParam("ptypeid")Integer ptypeid,@RequestParam("ptypesubid")Integer ptypesubid,
                            @RequestParam(value = "current",defaultValue = "1",required = false)Integer current,@RequestParam(value = "size",defaultValue = "3",required = false)Integer size
                            ){
        returnVO = new ReturnVO();
        Page<Products> page = new Page<>();
        page.setSize(size);
        page.setCurrent(current);
        Map<String,Object> params = new HashMap<>();
        params.put("ptypeid",ptypeid);
        params.put("ptypesubid",ptypesubid);
        IPage<Products> iPage = productsService.page(page,new QueryWrapper<Products>().allEq( params));
        ArrayList<Products> products = null;
        if ( iPage.getRecords()!=null&&iPage.getRecords().size()!=0){
            products = (ArrayList<Products>) iPage.getRecords();
        }
        
        if (products!=null&&products.size()!=0){
            MyPage<Products> myPage = new MyPage<>();
            myPage.setTotal(Math.toIntExact(iPage.getTotal()));
            myPage.setPageSize(Math.toIntExact(iPage.getSize()));
            myPage.setCurrent(Math.toIntExact(iPage.getCurrent()));
            myPage.setData(products);
            returnVO.setCode(200);
            returnVO.setMessage("获取成功");
            returnVO.setData(myPage);
            return returnVO;
        }else{
            returnVO.setCode(500);
            returnVO.setMessage("获取失败");
            return returnVO;
        }

    }
}
