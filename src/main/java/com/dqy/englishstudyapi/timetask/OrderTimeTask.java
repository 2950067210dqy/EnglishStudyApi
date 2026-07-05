package com.dqy.englishstudyapi.timetask;

import com.dqy.englishstudyapi.service.OrdersService;
import com.dqy.englishstudyapi.service.imp.OrdersServiceImpl;
import com.dqy.englishstudyapi.tablebean.Orders;
import com.dqy.englishstudyapi.util.SpringUtils;
import io.netty.util.Timeout;
import io.netty.util.TimerTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class OrderTimeTask implements TimerTask {
    ApplicationContext applicationContext = SpringUtils.getApplicationContext();
    @Autowired
    OrdersService ordersService;
    Integer id;
    public OrderTimeTask(Integer id){
        this.id = id;
    }
    public OrderTimeTask(){

    }
    public void run(Timeout timeout) throws Exception {
        // 取消订单业务逻辑
        System.out.println("取消========================【");
        OrdersService ordersService = applicationContext.getBean(OrdersServiceImpl.class);
        Orders orders = ordersService.getById(id);
        if (orders !=null&& orders.getOrderstatus()==5){
            ordersService.cancel(id);
        }

    }
}
