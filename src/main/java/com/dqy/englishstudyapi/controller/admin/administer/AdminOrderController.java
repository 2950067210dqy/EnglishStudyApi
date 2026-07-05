package com.dqy.englishstudyapi.controller.admin.administer;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dqy.englishstudyapi.entity.adminEntity.administer.FrontOrders;
import com.dqy.englishstudyapi.entity.adminEntity.administer.FrontProducts;
import com.dqy.englishstudyapi.entity.adminEntity.condition.OrderCondition;
import com.dqy.englishstudyapi.entity.adminEntity.condition.TestCondition;
import com.dqy.englishstudyapi.entity.frontEntity.OrderFull.Logistics;
import com.dqy.englishstudyapi.entity.page.MyPage;
import com.dqy.englishstudyapi.service.*;
import com.dqy.englishstudyapi.tablebean.*;
import com.dqy.englishstudyapi.util.*;
import com.dqy.englishstudyapi.vo.ReturnVO;
import com.dqy.englishstudyapi.vo.SubReturnVo;
import net.sf.jsqlparser.expression.operators.relational.NotEqualsTo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.MethodWrapper;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

@RestController
@RequestMapping("adminOrder")
public class AdminOrderController {
    @Autowired
    OrderstatusService orderstatusService;

    @Autowired
    OrdersService ordersService;
    @Autowired
    ProductsService productsService;
    @Autowired
    AddressService addressService;
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
    LogisticsUtil logisticsUtil;
    @Autowired
    DynamicTableNameUtil dynamicTableNameUtil;

    ReturnVO returnVO;
    @PostMapping("/cancelOrderSingle")
    public ReturnVO cancelOrderSingle(@RequestParam("id")Integer id){
        returnVO = new ReturnVO();
        if (id==null){
            returnVO.setMessage("参数为空");
            returnVO.setCode(500);
            return  returnVO;
        }
        SubReturnVo subReturnVo = ordersService.cancel(id);
        if (subReturnVo.isResult()){

           returnVO.setMessage("取消成功");
           returnVO.setCode(200);
           return  returnVO;

        }else{
            returnVO.setMessage(subReturnVo.getMessage());
            returnVO.setCode(subReturnVo.getCode());
            return  returnVO;
        }
    }
    @PostMapping("/cancelOrderBatch")
    public ReturnVO cancelOrderBatch(@RequestParam("ids") List<Integer> ids){
        returnVO = new ReturnVO();
        if (ids==null||ids.size()==0){
            returnVO.setMessage("参数为空");
            returnVO.setCode(500);
            return  returnVO;
        }
        for (Integer id:ids
             ) {
            SubReturnVo subReturnVo = ordersService.cancel(id);
            if (subReturnVo.isResult()){

                returnVO.setMessage("取消成功");
                returnVO.setCode(200);


            }else{
                returnVO.setMessage(subReturnVo.getMessage());
                returnVO.setCode(subReturnVo.getCode());

            }
        }
        return  returnVO;
    }

    @PostMapping("/sendOutOrderSingle")
    public ReturnVO sendOutOrderSingle(@RequestParam("id")Integer id,@RequestParam("trackid")String trackid){
        returnVO = new ReturnVO();
        if (id==null||trackid==null||trackid.equals("")){
            returnVO.setMessage("参数为空");
            returnVO.setCode(500);
            return  returnVO;
        }
        Orders order = ordersService.getById(id);
        if (order!=null){
            order.setTrackid(trackid);
            order.setOrderstatus(2);
            order.setUpdatetime(timeUtil.getNowLocalDateTime());
            boolean result = ordersService.updateById(order);
            if (result){
                returnVO.setMessage("发货成功");
                returnVO.setCode(200);
                return  returnVO;
            }else{
                returnVO.setMessage("发货失败");
                returnVO.setCode(500);
                return  returnVO;
            }
        }else{
            returnVO.setMessage("订单不存在");
            returnVO.setCode(500);
            return  returnVO;
        }
    }
    @PostMapping("/sendOutOrderBatch")
    public ReturnVO sendOutOrderBatch(@RequestParam("ids") List<Integer> ids,@RequestParam("trackids")String trackids){
        returnVO = new ReturnVO();
        if (ids==null||ids.size()==0||trackids==null||trackids.equals("")){
            returnVO.setMessage("参数为空");
            returnVO.setCode(500);
            return  returnVO;
        }
        List<Orders> orders = ordersService.listByIds(ids);
        if (orders!=null&&orders.size()!=0){
            List<String> trackList = wordUtil.spilitComma(trackids);
            if (trackList==null||trackList.size()!=orders.size()){
                returnVO.setMessage("快递单号个数不匹配");
                returnVO.setCode(500);
                return  returnVO;
            }
            for (int i = 0; i < orders.size(); i++) {
                orders.get(i).setTrackid(trackList.get(i));
                orders.get(i).setOrderstatus(2);
                orders.get(i).setUpdatetime(timeUtil.getNowLocalDateTime());
            }
            boolean result = ordersService.updateBatchById(orders);
            if (result){
                returnVO.setMessage("发货成功");
                returnVO.setCode(200);
                return  returnVO;
            }else{
                returnVO.setMessage("发货失败");
                returnVO.setCode(500);
                return  returnVO;
            }
        }else{
            returnVO.setMessage("订单不存在");
            returnVO.setCode(500);
            return  returnVO;
        }

    }

    @PostMapping("/recieveOrderSingle")
    public ReturnVO recieveOrderSingle(@RequestParam("id")Integer id){
        returnVO = new ReturnVO();
        if (id==null){
            returnVO.setMessage("参数为空");
            returnVO.setCode(500);
            return  returnVO;
        }
        Orders order = ordersService.getById(id);
        if (order!=null){
            order.setOrderstatus(3);
            order.setUpdatetime(timeUtil.getNowLocalDateTime());
            boolean result = ordersService.updateById(order);
            if (result){
                returnVO.setMessage("收货成功");
                returnVO.setCode(200);
                return  returnVO;
            }else{
                returnVO.setMessage("收货失败");
                returnVO.setCode(500);
                return  returnVO;
            }
        }else{
            returnVO.setMessage("订单不存在");
            returnVO.setCode(500);
            return  returnVO;
        }
    }
    @PostMapping("/recieveOrderBatch")
    public ReturnVO recieveOrderBatch(@RequestParam("ids") List<Integer> ids){
        returnVO = new ReturnVO();
        if (ids==null||ids.size()==0){
            returnVO.setMessage("参数为空");
            returnVO.setCode(500);
            return  returnVO;
        }
        List<Orders> orders = ordersService.listByIds(ids);
        if (orders!=null&&orders.size()!=0){
            for (Orders o:orders
            ) {
                o.setOrderstatus(3);
                o.setUpdatetime(timeUtil.getNowLocalDateTime());
            }
            boolean result = ordersService.updateBatchById(orders);
            if (result){
                returnVO.setMessage("收货成功");
                returnVO.setCode(200);
                return  returnVO;
            }else{
                returnVO.setMessage("收货失败");
                returnVO.setCode(500);
                return  returnVO;
            }
        }else{
            returnVO.setMessage("订单不存在");
            returnVO.setCode(500);
            return  returnVO;
        }

    }


    @PostMapping("/getOrderStatusExist")
    public ReturnVO getOrderStatusExist(@RequestParam("dsc")String dsc){
        returnVO = new ReturnVO();
        if (dsc==null||dsc.trim().equals("")){
            returnVO.setCode(500);
            returnVO.setMessage("数据为空");
            return returnVO;
        }
        Orderstatus orderstatus = orderstatusService.getOne(new QueryWrapper<Orderstatus>().eq("dsc",dsc));
        if (orderstatus!=null){
            returnVO.setCode(200);
            returnVO.setMessage("存在");
            return returnVO;
        }else{
            returnVO.setCode(500);
            returnVO.setMessage("不存在");
            return returnVO;
        }
    }
    @PostMapping("/deleteOrderStatus")
    public ReturnVO deleteOrderStatus(@RequestParam("id")Integer id){
        returnVO = new ReturnVO();
        returnVO.setCode(500);
        returnVO.setMessage("暂未权限");
        return returnVO;
//        if (id==null||id==1){
//            returnVO.setCode(500);
//            returnVO.setMessage("数据为空");
//            return returnVO;
//        }
//        boolean result = orderstatusService.removeById(id);
//        if (result){
//                returnVO.setCode(200);
//                returnVO.setMessage("删除成功");
//                return returnVO;
//        }else{
//            returnVO.setCode(500);
//            returnVO.setMessage("删除testtype失败");
//            return returnVO;
//        }
    }

    @PostMapping("/insertOrderStatus")
    public ReturnVO insertOrderStatus(@RequestBody Orderstatus orderstatus){
        returnVO = new ReturnVO();
        if (orderstatus==null){
            returnVO.setCode(500);
            returnVO.setMessage("数据为空");
            return returnVO;
        }
        orderstatus.setCreatetime(timeUtil.getNowLocalDateTime());
        orderstatus.setDeleted(0);
        boolean result = orderstatusService.save(orderstatus);
        if (result){

            returnVO.setCode(200);
            returnVO.setMessage("添加成功");
            returnVO.setData(orderstatus);
            return returnVO;
        }else{
            returnVO.setCode(500);
            returnVO.setMessage("添加失败");
            return returnVO;
        }
    }
    @PostMapping("/updateOrderStatus")
    public ReturnVO updateOrderStatus(@RequestBody Orderstatus orderstatus){
        returnVO = new ReturnVO();
        if (orderstatus==null){
            returnVO.setCode(500);
            returnVO.setMessage("数据为空");
            return returnVO;
        }
        orderstatus.setUpdatetime(timeUtil.getNowLocalDateTime());
        orderstatus.setDeleted(0);
        boolean result = orderstatusService.updateById(orderstatus);
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

    @PostMapping("/getOrderStatus")
    public  ReturnVO getOrderStatus(){
        returnVO = new ReturnVO();
        List<Orderstatus> orderstatuses =orderstatusService.list();
        if (orderstatuses!=null&&orderstatuses.size()!=0){
            returnVO.setCode(200);
            returnVO.setMessage("获取成功");
            returnVO.setData(orderstatuses);
            return returnVO;
        }else{
            returnVO.setCode(500);
            returnVO.setMessage("获取失败");
            return returnVO;
        }
    }
    @PostMapping("/getOrderNums")
    public  ReturnVO getOrderNums(){
        returnVO = new ReturnVO();
        List<Orderstatus> orderstatuses =orderstatusService.list();
        if (orderstatuses!=null&&orderstatuses.size()!=0){
            List<Integer> nums = new ArrayList<>();
            for (Orderstatus os :orderstatuses
                    ) {
                Long count = ordersService.count(new QueryWrapper<Orders>().eq("orderstatus",os.getId()));
                if (count==null||count==0){
                    nums.add(0);
                }else{
                    nums.add(Math.toIntExact(count));
                }
            }
            returnVO.setCode(200);
            returnVO.setMessage("获取成功");
            returnVO.setData(nums);
            return returnVO;
        }else{
            returnVO.setCode(500);
            returnVO.setMessage("获取orderstatus失败");
            return returnVO;
        }
    }
    @PostMapping("/getOrderProducts")
    public  ReturnVO getOrderStatus(@RequestParam("orderstatus")Integer orderstatus){
        returnVO = new ReturnVO();
        if (orderstatus==null||orderstatus==0){
            returnVO.setCode(500);
            returnVO.setMessage("数据为空");
            return returnVO;
        }

        List<Orders> orders = ordersService.list(new QueryWrapper<Orders>().eq("orderstatus",orderstatus));

        if (orders!=null&&orders.size()!=0){
            List<Integer> pids = new ArrayList<>();
            for (Orders o:orders
                 ) {
                if (!(pids.contains(o.getPid()))){
                    pids.add(o.getPid());
                }
            }
            List<Products> products = productsService.listByIds(pids);
            if (products!=null&&products.size()!=0){
                returnVO.setCode(200);
                returnVO.setMessage("获取成功");
                returnVO.setData(products);
                return returnVO;
            }else{
                returnVO.setCode(500);
                returnVO.setMessage("获取失败2");
                return returnVO;
            }

        }else{
            returnVO.setCode(500);
            returnVO.setMessage("获取失败1");
            return returnVO;
        }
    }




    @PostMapping("/getOrder")
    public  ReturnVO getOrder(@RequestBody OrderCondition condition){
        returnVO = new ReturnVO();
        if (condition==null){
            returnVO.setCode(500);
            returnVO.setMessage("数据为空");
            return returnVO;
        }

        Page<Orders> page = new Page<>();
        page.setCurrent(condition.getCurrent());
        page.setSize(condition.getSize());

        IPage<Orders> iPage = ordersService.page(page,getConditionWrapper(new QueryWrapper<Orders>(),condition));
        if (iPage.getRecords()!=null){
            ArrayList<Orders> orders =iPage.getRecords().size()==0?new ArrayList<>(): (ArrayList<Orders>) iPage.getRecords();
            ArrayList<FrontOrders> frontOrders = new ArrayList<FrontOrders>();
            for (Orders o:orders
                 ) {
                FrontOrders frontOrder = new FrontOrders();
                frontOrder.setOrder(o);
                FrontProducts frontProduct = new FrontProducts();
                Products product = productsService.getById(o.getPid());
                frontProduct.setProduct(product);
                User productUser = userService.getById(product.getMerchantId());
                frontProduct.setUser(productUser);
                frontOrder.setProduct(frontProduct);
                User user= userService.getById(o.getUid());
                frontOrder.setUser(user);
                Address address = addressService.getById(o.getAid());
                frontOrder.setAddress(address);
                if (condition.getOrderstatus()==2||condition.getOrderstatus()==3){
                    ReturnVO subReuturn = logisticsUtil.get(o.getTrackid(),o.getTracktype());
                    if (subReuturn.getCode()==200){
                        frontOrder.setLogistics((Logistics) subReuturn.getData());
                    }else{
                        frontOrder.setLogistics(null);
                    }
                }else{
                    frontOrder.setLogistics(null);
                }
                frontOrders.add(frontOrder);
            }
            MyPage<FrontOrders> myPage = new MyPage<>();
            myPage.setData(frontOrders);
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

    private QueryWrapper<Orders> getConditionWrapper(QueryWrapper<Orders> wrapper, OrderCondition condition) {
        if (wrapper==null){
            wrapper = new QueryWrapper<>();
        }
        wrapper = wrapper.eq("orderstatus",condition.getOrderstatus());
        if (condition.getOrdersSearch()!=null){
            wrapper = wrapper.like("orders",condition.getOrderstatus());
        }

        if (condition.getProductSelect()!=null){
            Consumer<QueryWrapper<Orders>> consumer = new Consumer<QueryWrapper<Orders>>() {
                @Override
                public void accept(QueryWrapper<Orders> wrapper1) {
                    for (int i = 0; i < condition.getProductSelect().size(); i++) {
                        if (i!=condition.getProductSelect().size()-1){
                            wrapper1 = wrapper1.eq("pid",condition.getProductSelect().get(i)).or();
                        }else{
                            wrapper1 = wrapper1.eq("pid",condition.getProductSelect().get(i));
                        }
                    }
                }
            };
            wrapper.and(consumer);
        }
        if (condition.getUserSelect()!=null){
            Consumer<QueryWrapper<Orders>> consumer = new Consumer<QueryWrapper<Orders>>() {
                @Override
                public void accept(QueryWrapper<Orders> wrapper1) {
                    for (int i = 0; i < condition.getUserSelect().size(); i++) {
                        if (i!=condition.getUserSelect().size()-1){
                            wrapper1 = wrapper1.eq("uid",condition.getUserSelect().get(i)).or();
                        }else{
                            wrapper1 = wrapper1.eq("uid",condition.getUserSelect().get(i));
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
        orderbydesc.add("updatetime");
        condition.setOrderbyDesc(orderbydesc);
        if (condition.getOrderbyDesc()!=null&&condition.getOrderbyDesc().size()!=0){
            wrapper= wrapper.orderByDesc(condition.getOrderbyDesc());
        }
        return  wrapper;
    }
}
