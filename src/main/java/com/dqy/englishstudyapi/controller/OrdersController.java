package com.dqy.englishstudyapi.controller;


import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dqy.englishstudyapi.entity.frontEntity.OrderFull.Logistics;
import com.dqy.englishstudyapi.entity.frontEntity.OrderFull.OrderFull;
import com.dqy.englishstudyapi.entity.page.MyPage;
import com.dqy.englishstudyapi.service.*;
import com.dqy.englishstudyapi.tablebean.*;
import com.dqy.englishstudyapi.timetask.OrderTimeTask;
import com.dqy.englishstudyapi.timetask.OrderTimer;
import com.dqy.englishstudyapi.util.*;
import com.dqy.englishstudyapi.vo.ReturnVO;
import com.dqy.englishstudyapi.vo.SubReturnVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 邓亲优
 * @since 2023-02-22
 */
@RestController
@RequestMapping("order")
public class OrdersController {

    @Autowired
    OrdersService ordersService;
    @Autowired
    ScoreService scoreService;
    @Autowired
    ScoresourceService scoresourceService;
    @Autowired
    ProductsService productsService;
    @Autowired
    AddressService addressService;
    @Autowired
    OrderstatusService orderstatusService;
    @Autowired
    TimeUtil timeUtil;
    @Autowired
    RandomUtil randomUtil;
    @Autowired
    JsonUtil jsonUtil;
    @Autowired
    LogisticsUtil logisticsUtil;
    @Autowired
    OrderTimer orderTimer;
    OrderTimeTask orderTimeTask;
    ReturnVO returnVO;

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
    @PostMapping("/setFinish")
    public ReturnVO setFinish(@RequestParam("id") Integer id){
        returnVO = new ReturnVO();
        if (id!=null){
            Orders orders = ordersService.getById(id);
            if (orders !=null){
                orders.setOrderstatus(3);
                boolean result = ordersService.updateById(orders);
                if (result ){
                    returnVO.setCode(200);
                    returnVO.setMessage("更新订单成功");
                    return  returnVO;
                }else{
                    returnVO.setCode(500);
                    returnVO.setMessage("更新订单错误");
                    return  returnVO;
                }
            }else{
                returnVO.setCode(500);
                returnVO.setMessage("获取订单错误");
                return  returnVO;
            }
        }else{
            returnVO.setCode(500);
            returnVO.setMessage("数据为空");
            return  returnVO;
        }
    }


    @PostMapping("/setCancel")
    public ReturnVO setCancel(@RequestParam("id") Integer id){
        returnVO = new ReturnVO();
        if (id!=null){
            SubReturnVo subReturnVo = ordersService.cancel(id);
            if (subReturnVo.isResult()){
                returnVO.setCode(200);
                returnVO.setMessage("取消成功");
                return  returnVO;
            }else{
                returnVO.setCode(subReturnVo.getCode());
                returnVO.setMessage(subReturnVo.getMessage());
                return  returnVO;
            }
        }else{
            returnVO.setCode(500);
            returnVO.setMessage("数据为空");
            return  returnVO;
        }
    }

    @PostMapping("/setDaiShouHuo")
    public ReturnVO setDaiShouHuo(@RequestParam("id") Integer id){
        returnVO = new ReturnVO();
        if (id!=null){
            Orders orders = ordersService.getById(id);
            if (orders !=null){
                orders.setOrderstatus(2);
                boolean result = ordersService.updateById(orders);
                if (result ){
                    returnVO.setCode(200);
                    returnVO.setMessage("更新订单成功");
                    return  returnVO;
                }else{
                    returnVO.setCode(500);
                    returnVO.setMessage("更新订单错误");
                    return  returnVO;
                }
            }else{
                returnVO.setCode(500);
                returnVO.setMessage("获取订单错误");
                return  returnVO;
            }
        }else{
            returnVO.setCode(500);
            returnVO.setMessage("数据为空");
            return  returnVO;
        }
    }


    @PostMapping("/get")
    public ReturnVO setCancel(@RequestParam("uid") Integer uid,
                              @RequestParam(value = "orderstatus",required = false)Integer orderstatus,
                              @RequestParam(value = "current",defaultValue = "1",required = false)Integer current,@RequestParam(value = "size",defaultValue = "3",required = false)Integer size){
        returnVO = new ReturnVO();
        if (uid!=null){
            Page<Orders> page = new Page<>();
            page.setSize(size);
            page.setCurrent(current);
            List<String> orderBydesc = new ArrayList<>();

            orderBydesc.add("updatetime");
//            orderBydesc.add("orderstatus");
            QueryWrapper<Orders> queryWrapper = new QueryWrapper<>();
            queryWrapper = queryWrapper.eq("uid",uid);
            if (orderstatus!=null){
                queryWrapper = queryWrapper.eq("orderstatus",orderstatus);
            }
            queryWrapper =queryWrapper.orderByDesc(orderBydesc);
            IPage<Orders> iPage = ordersService.page(page,queryWrapper);
            ArrayList<Orders> orders=null;
            ArrayList<OrderFull> orderFulls = null;
            if (iPage!=null&&iPage.getRecords()!=null&&iPage.getRecords().size()!=0){
                 orders = (ArrayList<Orders>) iPage.getRecords();
                 orderFulls = new ArrayList<>();
                for (Orders o:orders
                ) {
                    OrderFull orderFull = new OrderFull();
                    ReturnVO subReuturn = logisticsUtil.get(o.getTrackid(),o.getTracktype());
                    if (subReuturn.getCode()==200){
                        orderFull.setLogistics((Logistics) subReuturn.getData());
                    }else{
                        orderFull.setLogistics(null);
                    }
                    Address address =  addressService.getById(o.getAid());
                    Products products =productsService.getById(o.getPid());
                    orderFull.setOrders(o);
                    orderFull.setAddress(address);
                    orderFull.setProducts(products);
                    orderFulls.add(orderFull);
                }
            }
            if (orderFulls!=null&&orderFulls.size()!=0){
                MyPage<String> myPage = new MyPage<>();
                myPage.setTotal(Math.toIntExact(iPage.getTotal()));
                myPage.setPageSize(Math.toIntExact(iPage.getSize()));
                myPage.setCurrent(Math.toIntExact(iPage.getCurrent()));
                myPage.setOne(jsonUtil.parseArrayListToJsonStrThenToBase64(orderFulls));
                returnVO.setCode(200);
                returnVO.setMessage("获取成功");
                returnVO.setData(myPage);
                return returnVO;
            }else{
                returnVO.setCode(500);
                returnVO.setMessage("获取订单错误");
                return  returnVO;
            }
        }else{
            returnVO.setCode(500);
            returnVO.setMessage("数据为空");
            return  returnVO;
        }
    }

    @PostMapping("/getOrderNumsByUid")
    public  ReturnVO getOrderNumsByUid(@RequestParam("uid")Integer uid){
        returnVO = new ReturnVO();
        if (uid ==null){
            returnVO.setCode(500);
            returnVO.setMessage("参数为空");
            return returnVO;
        }
        List<Orderstatus> orderstatuses =orderstatusService.list();
        if (orderstatuses!=null&&orderstatuses.size()!=0){
            List<Integer> nums = new ArrayList<>();
            for (Orderstatus os :orderstatuses
            ) {
                Long count = ordersService.count(new QueryWrapper<Orders>().eq("orderstatus",os.getId()).eq("uid",uid));
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

    @PostMapping("/pay")
    public ReturnVO pay(@RequestParam("id") Integer id){
        returnVO = new ReturnVO();
        if (id!=null){
            Orders orders = ordersService.getById(id);
            if (orders !=null){
                Score score = scoreService.getOne(new QueryWrapper<Score>().eq("uid", orders.getUid()));
                if (score!=null){
                    Products products=  productsService.getById(orders.getPid());
                    if (products!=null){
                        if (score.getScore()<products.getPrice()){
                            returnVO.setCode(550);
                            returnVO.setMessage("积分不足");
                            return returnVO;
                        }else{
                            score.setScore((long) (score.getScore()-products.getPrice()));
                            boolean result2=  scoreService.updateById(score);
                            if (result2){
                                Scoresource scoresource = new Scoresource();
                                scoresource.setSourceid(11);
                                scoresource.setUid(orders.getUid());
                                scoresource.setCreatetime(timeUtil.getCurrentTimeLocalDateTime());
                                scoresource.setNum(Long.valueOf(Math.round(products.getPrice())));
                                boolean result2_5=  scoresourceService.save(scoresource);
                                if (result2_5){
                                    orders.setOrderstatus(1);
                                    boolean result3 = ordersService.updateById(orders);
                                    if (result3){
                                        returnVO.setCode(200);
                                        returnVO.setMessage("兑换成功");
                                        return returnVO;
                                    }else{
                                        returnVO.setCode(500);
                                        returnVO.setMessage("更新订单失败");
                                        return returnVO;
                                    }
                                }else{
                                    returnVO.setCode(500);
                                    returnVO.setMessage("存储积分来源失败");
                                    return returnVO;
                                }

                            }else{
                                returnVO.setCode(500);
                                returnVO.setMessage("更新积分失败");
                                return returnVO;
                            }

                        }
                    }else{
                        returnVO.setCode(500);
                        returnVO.setMessage("获取商品失败");
                        return returnVO;
                    }
                }else{
                    returnVO.setCode(500);
                    returnVO.setMessage("获取用户积分失败");
                    return returnVO;
                }
            }else{
                returnVO.setCode(500);
                returnVO.setMessage("获取订单错误");
                return  returnVO;
            }
        }else{
            returnVO.setCode(500);
            returnVO.setMessage("数据为空");
            return  returnVO;
        }
    }


    @PostMapping("/setOrder")
    public ReturnVO setOrder(@RequestBody Orders orders){
        returnVO = new ReturnVO();
        if (orders ==null){
            returnVO.setCode(500);
            returnVO.setMessage("数据不为空");
            return returnVO;
        }else{
            Products products=  productsService.getById(orders.getPid());
            if (products!=null){
                products.setNum(products.getNum()-1);
                boolean result0 = productsService.updateById(products);
                if (result0){
                    orders.setCreatetime(timeUtil.getCurrentTimeLocalDateTime());
                    orders.setDeleted(0);
                    orders.setOrderstatus(5);
                    orders.setOrders(randomUtil.getOrderRandom(orders.getUid()));
                    orders.setId(null);
                    orders.setUpdatetime(timeUtil.getCurrentTimeLocalDateTime());
                    boolean result = ordersService.save(orders);
                    if (result){
                        // 初始化时间轮
                        // 注册此定时任务（延迟时间为5秒，也就是说5秒后订单过期
                        orderTimeTask = new OrderTimeTask(orders.getId());
                        orderTimer.newTimeout(orderTimeTask,1, TimeUnit.DAYS);
                        returnVO.setCode(200);
                        returnVO.setMessage("订单创建成功");
                        returnVO.setData(orders);
                        return returnVO;
                    }else{
                        returnVO.setCode(500);
                        returnVO.setMessage("订单创建失败");
                        return returnVO;
                    }
                }else{
                    returnVO.setCode(500);
                    returnVO.setMessage("锁定商品失败");
                    return returnVO;
                }

            }else{
                returnVO.setCode(500);
                returnVO.setMessage("获取商品失败");
                return returnVO;
            }

        }
    }

    @PostMapping("/order")
    public ReturnVO order(@RequestBody Orders orders){
        returnVO = new ReturnVO();
        if (orders ==null){
            returnVO.setCode(500);
            returnVO.setMessage("数据不为空");
            return returnVO;
        }else{
            Products products=  productsService.getById(orders.getPid());
            if (products!=null){
                Score score = scoreService.getOne(new QueryWrapper<Score>().eq("uid", orders.getUid()));
                if (score!=null){
                    if (score.getScore()<products.getPrice()){
                        returnVO.setCode(550);
                        returnVO.setMessage("积分不足");
                        return returnVO;
                    }else{
                        score.setScore((long) (score.getScore()-products.getPrice()));
                        boolean result2=  scoreService.updateById(score);
                        if (result2){

                            Scoresource scoresource = new Scoresource();
                            scoresource.setSourceid(11);
                            scoresource.setUid(orders.getUid());
                            scoresource.setCreatetime(timeUtil.getCurrentTimeLocalDateTime());
                            scoresource.setNum(Long.valueOf(Math.round(products.getPrice())));
                            boolean result2_5=  scoresourceService.save(scoresource);
                            if (result2_5){
                                orders.setOrderstatus(1);
                                orders.setUpdatetime(timeUtil.getNowLocalDateTime());
                                boolean result3 = ordersService.updateById(orders);
                                if (result3){
                                    returnVO.setCode(200);
                                    returnVO.setMessage("兑换成功");
                                    return returnVO;
                                }else{
                                    returnVO.setCode(500);
                                    returnVO.setMessage("更新订单失败");
                                    return returnVO;
                                }
                            }else{
                                returnVO.setCode(500);
                                returnVO.setMessage("存储积分来源失败");
                                return returnVO;
                            }

                        }else{
                            returnVO.setCode(500);
                            returnVO.setMessage("更新积分失败");
                            return returnVO;
                        }

                    }
                }else{
                    returnVO.setCode(500);
                    returnVO.setMessage("获取用户积分失败");
                    return returnVO;
                }
            }else{
                returnVO.setCode(500);
                returnVO.setMessage("获取商品失败");
                return returnVO;
            }

        }
    }

//    @PostMapping("/order")
//    public ReturnVO order(@RequestBody Orders orders){
//        returnVO = new ReturnVO();
//        if (orders ==null){
//            returnVO.setCode(500);
//            returnVO.setMessage("数据不为空");
//            return returnVO;
//        }else{
//            Products products=  productsService.getById(orders.getPid());
//            if (products!=null){
//                products.setNum(products.getNum()-1);
//                boolean result0 = productsService.updateById(products);
//                if (result0){
//                    orders.setCreatetime(timeUtil.getCurrentTimeLocalDateTime());
//                    orders.setDeleted(0);
//                    orders.setOrderstatus(5);
//                    orders.setOrders(randomUtil.getOrderRandom(orders.getUid()));
//                    orders.setId(null);
//                    orders.setUpdatetime(null);
//                    boolean result = ordersService.save(orders);
//                    if (result){
//                        // 初始化时间轮
//                        // 注册此定时任务（延迟时间为5秒，也就是说5秒后订单过期
//                        orderTimeTask = new OrderTimeTask(orders.getId());
//                        orderTimer.newTimeout(orderTimeTask,1, TimeUnit.DAYS);
//
//
//                        Score score = scoreService.getOne(new QueryWrapper<Score>().eq("uid", orders.getUid()));
//                        if (score!=null){
//                            if (score.getScore()<products.getPrice()){
//                                returnVO.setCode(550);
//                                returnVO.setMessage("积分不足");
//                                return returnVO;
//                            }else{
//                                score.setScore((long) (score.getScore()-products.getPrice()));
//                                boolean result2=  scoreService.updateById(score);
//                                if (result2){
//
//                                    Scoresource scoresource = new Scoresource();
//                                    scoresource.setSourceid(11);
//                                    scoresource.setUid(orders.getUid());
//                                    scoresource.setCreatetime(timeUtil.getCurrentTimeLocalDateTime());
//                                    scoresource.setNum(Long.valueOf(Math.round(products.getPrice())));
//                                    boolean result2_5=  scoresourceService.save(scoresource);
//                                    if (result2_5){
//                                        orders.setOrderstatus(1);
//                                        boolean result3 = ordersService.updateById(orders);
//                                        if (result3){
//                                            returnVO.setCode(200);
//                                            returnVO.setMessage("兑换成功");
//                                            return returnVO;
//                                        }else{
//                                            returnVO.setCode(500);
//                                            returnVO.setMessage("更新订单失败");
//                                            return returnVO;
//                                        }
//                                    }else{
//                                        returnVO.setCode(500);
//                                        returnVO.setMessage("存储积分来源失败");
//                                        return returnVO;
//                                    }
//
//                                }else{
//                                    returnVO.setCode(500);
//                                    returnVO.setMessage("更新积分失败");
//                                    return returnVO;
//                                }
//
//                            }
//                        }else{
//                            returnVO.setCode(500);
//                            returnVO.setMessage("获取用户积分失败");
//                            return returnVO;
//                        }
//                    }else{
//                        returnVO.setCode(500);
//                        returnVO.setMessage("订单创建失败");
//                        return returnVO;
//                    }
//                }else{
//                    returnVO.setCode(500);
//                    returnVO.setMessage("锁定商品失败");
//                    return returnVO;
//                }
//
//            }else{
//                returnVO.setCode(500);
//                returnVO.setMessage("获取商品失败");
//                return returnVO;
//            }
//
//        }
//    }



}
