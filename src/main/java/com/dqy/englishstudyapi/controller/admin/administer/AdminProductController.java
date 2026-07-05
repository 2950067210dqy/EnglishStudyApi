package com.dqy.englishstudyapi.controller.admin.administer;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dqy.englishstudyapi.entity.adminEntity.administer.FrontProducts;
import com.dqy.englishstudyapi.entity.adminEntity.condition.ProductCondition;
import com.dqy.englishstudyapi.entity.adminEntity.condition.ReadCondition;
import com.dqy.englishstudyapi.entity.frontEntity.ReadFront.ReadFront;
import com.dqy.englishstudyapi.entity.page.MyPage;
import com.dqy.englishstudyapi.service.*;
import com.dqy.englishstudyapi.tablebean.*;
import com.dqy.englishstudyapi.util.*;
import com.dqy.englishstudyapi.vo.ReturnVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@RestController
@RequestMapping("adminProduct")
public class AdminProductController {
    @Autowired
    PtypeService ptypeService;
    @Autowired
    PtypesubService ptypesubService;
    @Autowired
    ProductsService productsService;
    @Autowired
    UserService userService;
    @Autowired
    TimeUtil timeUtil;
    @Autowired
    Base64Util base64Util;
    @Autowired
    JsonUtil jsonUtil;
    @Autowired
    WordUtil wordUtil;
    @Autowired
    DynamicTableNameUtil dynamicTableNameUtil;

    ReturnVO returnVO;
    @PostMapping("/deleteProductSingle")
    public ReturnVO deleteProductSingle(@RequestParam("pType")Integer pType,@RequestParam("pTypeSub") Integer pTypeSub,@RequestParam("id")Integer id){
        returnVO = new ReturnVO();
        if (pType==null||pTypeSub==null||id==null){
            returnVO.setMessage("参数为空");
            returnVO.setCode(500);
            return  returnVO;
        }

        boolean result = productsService.remove(new QueryWrapper<Products>().eq("id",id).eq("ptypeid",pType).eq("ptypesubid",pTypeSub));
        if (result){
            returnVO.setMessage("删除成功");
            returnVO.setCode(200);
            return  returnVO;
        }else{
            returnVO.setMessage("删除失败");
            returnVO.setCode(500);
            return  returnVO;
        }

    }
    @PostMapping("/deleteProductBatch")
    public ReturnVO deleteProductBatch(@RequestParam("ids") List<Integer> ids,@RequestParam("pType")Integer pType,@RequestParam("pTypeSub") Integer pTypeSub){
        returnVO = new ReturnVO();
        if (pType==null||pTypeSub==null||ids==null||ids.size()==0){
            returnVO.setMessage("参数为空");
            returnVO.setCode(500);
            return  returnVO;
        }

        boolean result = productsService.removeBatchByIds(ids);
        if (result){
            returnVO.setMessage("删除成功");
            returnVO.setCode(200);
            return  returnVO;
        }else{
            returnVO.setMessage("删除失败");
            returnVO.setCode(500);
            return  returnVO;
        }
    }
    @PostMapping("/insert")
    public ReturnVO insert(@RequestBody  Products product){
        returnVO =  new ReturnVO();
        if (product==null){
            returnVO.setCode(500);
            returnVO.setMessage("数据为空");
            return returnVO;
        }


        product.setTime(timeUtil.getNowLocalDateTime());
        product.setDeleted(0);
        product.setProductAddre("");

        boolean result =  productsService.save(product);
        if (result){

            returnVO.setCode(200);
            returnVO.setMessage("保存商品成功");
            return returnVO;

        }else{
            returnVO.setCode(500);
            returnVO.setMessage("保存商品失败");
            return returnVO;
        }

    }
    @PostMapping("/update")
    public ReturnVO update(@RequestBody  Products product){
        returnVO =  new ReturnVO();
        if (product==null){
            returnVO.setCode(500);
            returnVO.setMessage("数据为空");
            return returnVO;
        }


        product.setTime(timeUtil.getNowLocalDateTime());
        product.setDeleted(0);
        boolean result =  productsService.updateById(product);
        if (result){

            returnVO.setCode(200);
            returnVO.setMessage("更新成功");
            return returnVO;

        }else{
            returnVO.setCode(500);
            returnVO.setMessage("更新商品失败");
            return returnVO;
        }


    }

    @PostMapping("/getPTypeSubExist")
    public ReturnVO getPTypeSubExist(@RequestParam("dsc")String dsc,@RequestParam("pType")Integer pType){
        returnVO = new ReturnVO();
        if (dsc==null||dsc.trim().equals("")||pType==null){
            returnVO.setCode(500);
            returnVO.setMessage("数据为空");
            return returnVO;
        }

        Ptypesub ptypesub = ptypesubService.getOne(new QueryWrapper<Ptypesub>().eq("dsc",dsc).eq("ptypeid",pType));
        if (ptypesub!=null){
            returnVO.setCode(200);
            returnVO.setMessage("存在");
            return returnVO;
        }else{
            returnVO.setCode(500);
            returnVO.setMessage("不存在");
            return returnVO;
        }
    }
    @PostMapping("/deletePTypeSub")
    public ReturnVO deletePTypeSub(@RequestParam("id")Integer id,@RequestParam("pType")Integer pType){
        returnVO = new ReturnVO();
        if (id==null||pType==null){
            returnVO.setCode(500);
            returnVO.setMessage("数据为空");
            return returnVO;
        }

        boolean result = ptypesubService.removeById(id);
        if (result){

            long count =productsService.count(new QueryWrapper<Products>().eq("ptypeid",pType).eq("ptypesubid",id));
            if (count!=0){

                boolean result2 =productsService.remove(new QueryWrapper<Products>().eq("ptypeid",pType).eq("ptypesubid",id));
                if (result2){
                    returnVO.setCode(200);
                    returnVO.setMessage("删除成功");
                    return returnVO;
                }else{
                    returnVO.setCode(500);
                    returnVO.setMessage("删除products失败");
                    return returnVO;
                }
            }else{
                returnVO.setCode(200);
                returnVO.setMessage("删除成功");
                return returnVO;
            }


        }else{
            returnVO.setCode(500);
            returnVO.setMessage("删除ptypesub失败");
            return returnVO;
        }
    }

    @PostMapping("/insertPTypeSub")
    public ReturnVO insertPTypeSub(@RequestBody Ptypesub ptypesub){
        returnVO = new ReturnVO();
        if (ptypesub==null){
            returnVO.setCode(500);
            returnVO.setMessage("数据为空");
            return returnVO;
        }

        ptypesub.setCreatetime(timeUtil.getNowLocalDateTime());
        ptypesub.setDeleted(0);
        ptypesub.setId(null);

        boolean result = ptypesubService.save(ptypesub);
        if (result){

            returnVO.setCode(200);
            returnVO.setMessage("添加成功");
            returnVO.setData(ptypesub);
            return returnVO;
        }else{
            returnVO.setCode(500);
            returnVO.setMessage("添加失败");
            return returnVO;
        }


    }
    @PostMapping("/updatePTypeSub")
    public ReturnVO updatePTypeSub(@RequestBody Ptypesub ptypesub){
        returnVO = new ReturnVO();
        if (ptypesub==null){
            returnVO.setCode(500);
            returnVO.setMessage("数据为空");
            return returnVO;
        }

        ptypesub.setUpdatetime(timeUtil.getNowLocalDateTime());
        ptypesub.setDeleted(0);

        boolean result = ptypesubService.updateById(ptypesub);
        if (result){

            returnVO.setCode(200);
            returnVO.setMessage("修改成功");
            returnVO.setData(ptypesub);
            return returnVO;
        }else{
            returnVO.setCode(500);
            returnVO.setMessage("修改失败");
            return returnVO;
        }
    }



    @PostMapping("/getPTypeExist")
    public ReturnVO getPTypeExist(@RequestParam("dsc")String dsc){
        returnVO = new ReturnVO();
        if (dsc==null||dsc.trim().equals("")){
            returnVO.setCode(500);
            returnVO.setMessage("数据为空");
            return returnVO;
        }
        Ptype ptype = ptypeService.getOne(new QueryWrapper<Ptype>().eq("dsc",dsc));
        if (ptype!=null){
            returnVO.setCode(200);
            returnVO.setMessage("存在");
            return returnVO;
        }else{
            returnVO.setCode(500);
            returnVO.setMessage("不存在");
            return returnVO;
        }
    }
    @PostMapping("/deletePType")
    public ReturnVO deletePType(@RequestParam("id")Integer id){
        returnVO = new ReturnVO();
        if (id==null||id==1){
            returnVO.setCode(500);
            returnVO.setMessage("数据为空");
            return returnVO;
        }
        boolean result = ptypeService.removeById(id);
        if (result){

            long count = ptypesubService.count(new QueryWrapper<Ptypesub>().eq("ptypeid",id));
            if (count!=0){

                boolean result2 =ptypesubService.remove(new QueryWrapper<Ptypesub>().eq("ptypeid",id));
                if (result2){
                    returnVO.setCode(200);
                    returnVO.setMessage("删除成功");
                    return returnVO;
                }else{
                    returnVO.setCode(500);
                    returnVO.setMessage("删除ptypesub失败");
                    return returnVO;
                }
            }else{
                returnVO.setCode(200);
                returnVO.setMessage("删除成功");
                return returnVO;
            }


        }else{
            returnVO.setCode(500);
            returnVO.setMessage("删除ptype失败");
            return returnVO;
        }
    }

    @PostMapping("/insertPType")
    public ReturnVO insertPType(@RequestBody Ptype ptype){
        returnVO = new ReturnVO();
        if (ptype==null){
            returnVO.setCode(500);
            returnVO.setMessage("数据为空");
            return returnVO;
        }
        ptype.setCreatetime(timeUtil.getNowLocalDateTime());
        ptype.setDeleted(0);
        boolean result = ptypeService.save(ptype);
        if (result){

            returnVO.setCode(200);
            returnVO.setMessage("添加成功");
            returnVO.setData(ptype);
            return returnVO;
        }else{
            returnVO.setCode(500);
            returnVO.setMessage("添加失败");
            return returnVO;
        }
    }
    @PostMapping("/updatePType")
    public ReturnVO updatePType(@RequestBody Ptype ptype){
        returnVO = new ReturnVO();
        if (ptype==null){
            returnVO.setCode(500);
            returnVO.setMessage("数据为空");
            return returnVO;
        }
        ptype.setUpdatetime(timeUtil.getNowLocalDateTime());
        ptype.setDeleted(0);
        boolean result = ptypeService.updateById(ptype);
        if (result){

            returnVO.setCode(200);
            returnVO.setMessage("修改成功");
            return returnVO;
        }else{
            returnVO.setCode(500);
            returnVO.setMessage("修改失败");
            return returnVO;
        }
    }

    @PostMapping("/getPType")
    public  ReturnVO getPType(){
        returnVO = new ReturnVO();
        List<Ptype> ptypes =ptypeService.list();
        if (ptypes!=null&&ptypes.size()!=0){
            returnVO.setCode(200);
            returnVO.setMessage("获取成功");
            returnVO.setData(ptypes);
            return returnVO;
        }else{
            returnVO.setCode(500);
            returnVO.setMessage("获取失败");
            return returnVO;
        }
    }

    @PostMapping("/getPTypeSub")
    public  ReturnVO getPTypeSub(@RequestParam("pType")Integer pType){
        returnVO = new ReturnVO();
        if (pType==null){
            returnVO.setCode(500);
            returnVO.setMessage("数据为空");
            return returnVO;
        }

        List<Ptypesub> ptypes = ptypesubService.list(new QueryWrapper<Ptypesub>().eq("ptypeid",pType));
        if (ptypes!=null&&ptypes.size()!=0){
            returnVO.setCode(200);
            returnVO.setMessage("获取成功");
            returnVO.setData(ptypes);
            return returnVO;
        }else{
            returnVO.setCode(500);
            returnVO.setMessage("获取失败");
            return returnVO;
        }
    }

    @PostMapping("/getOneById")
    public  ReturnVO getOneById(@RequestParam("pType")Integer pType,@RequestParam("pTypeSub")Integer pTypeSub,@RequestParam("id")Integer id){
        returnVO = new ReturnVO();
        if (pType==null||pTypeSub==null||id==null){
            returnVO.setCode(500);
            returnVO.setMessage("数据为空");
            return returnVO;
        }


        Products product = productsService.getOne(new QueryWrapper<Products>().eq("id",id).eq("ptypeid",pType).eq("ptypesubid",pTypeSub));
        if (product!=null){
            FrontProducts frontProduct = new FrontProducts();
            frontProduct.setProduct(product);
            User user = userService.getById(product.getMerchantId());
            frontProduct.setUser(user);
            returnVO.setCode(200);
            returnVO.setMessage("获取成功");
            returnVO.setData( frontProduct);
        }else{
            returnVO.setCode(500);
            returnVO.setMessage("获取失败");
        }

        return returnVO;
    }

    @PostMapping("/getProduct")
    public  ReturnVO getProduct(@RequestBody ProductCondition condition){
        returnVO = new ReturnVO();
        if (condition==null){
            returnVO.setCode(500);
            returnVO.setMessage("数据为空");
            return returnVO;
        }

        Page<Products> page = new Page<>();
        page.setCurrent(condition.getCurrent());
        page.setSize(condition.getSize());

        IPage<Products> iPage = productsService.page(page,getConditionWrapper(new QueryWrapper<Products>(),condition));
        if (iPage.getRecords()!=null){
            List<Products> products =iPage.getRecords().size()==0?new ArrayList<>(): (ArrayList<Products>) iPage.getRecords();
            ArrayList<FrontProducts> frontProducts = new ArrayList<>();
            for (Products product:products
            ) {
                FrontProducts frontProduct = new FrontProducts();
                frontProduct.setProduct(product);
                User user = userService.getById(product.getMerchantId());
                frontProduct.setUser(user);
                frontProducts.add(frontProduct);

            }
            MyPage<FrontProducts> myPage = new MyPage<>();
            myPage.setData(frontProducts);
            myPage.setPageSize(Math.toIntExact(iPage.getSize()));
            myPage.setTotal(Math.toIntExact(iPage.getTotal()));
            myPage.setCurrent(Math.toIntExact(iPage.getCurrent()));
            returnVO.setCode(200);
            returnVO.setMessage("获取成功");
            returnVO.setData(myPage);
        }else{
            returnVO.setCode(500);
            returnVO.setMessage("获取失败");
        }

        return returnVO;
    }

    private QueryWrapper<Products> getConditionWrapper(QueryWrapper<Products> wrapper, ProductCondition condition) {
        if (wrapper==null){
            wrapper = new QueryWrapper<>();
        }
        wrapper=wrapper.eq("ptypeid",condition.getPType()).eq("ptypesubid",condition.getPTypeSub());
        if (condition.getTitleSearch()!=null){
            wrapper = wrapper.like("title",condition.getTitleSearch());
        }

        if (condition.getMerchantSelect()!=null){
            Consumer<QueryWrapper<Products>> consumer = new Consumer<QueryWrapper<Products>>() {
                @Override
                public void accept(QueryWrapper<Products> wrapper1) {
                    for (int i = 0; i < condition.getMerchantSelect().size(); i++) {
                        if (i!=condition.getMerchantSelect().size()-1){
                            wrapper1 = wrapper1.eq("merchant_id",condition.getMerchantSelect().get(i)).or();
                        }else{
                            wrapper1 = wrapper1.eq("merchant_id",condition.getMerchantSelect().get(i));
                        }
                    }
                }
            };
            wrapper.and(consumer);
        }
        if (condition.getOrderbyAsc()!=null&&condition.getOrderbyAsc().size()!=0){
            wrapper= wrapper.orderByAsc(condition.getOrderbyAsc());
        }
        List<String> orderbydesc = condition.getOrderbyDesc();
        orderbydesc.add("time");
        condition.setOrderbyDesc(orderbydesc);
        if (condition.getOrderbyDesc()!=null&&condition.getOrderbyDesc().size()!=0){
            wrapper= wrapper.orderByDesc(condition.getOrderbyDesc());
        }
        return  wrapper;
    }
}
