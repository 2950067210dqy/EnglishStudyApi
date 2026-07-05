package com.dqy.englishstudyapi.timetask;

import com.dqy.englishstudyapi.service.ZborderService;
import com.dqy.englishstudyapi.service.imp.ZborderServiceImpl;
import com.dqy.englishstudyapi.tablebean.Zborder;
import com.dqy.englishstudyapi.util.SpringUtils;
import io.netty.util.Timeout;
import io.netty.util.TimerTask;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class ZbOrderTimeTask implements TimerTask {
    ApplicationContext applicationContext = SpringUtils.getApplicationContext();
    Integer id;
    public ZbOrderTimeTask(Integer id){
        this.id = id;
    }
    public ZbOrderTimeTask(){

    }
    public void run(Timeout timeout) throws Exception {
        // 取消订单业务逻辑
        System.out.println("取消========================【");
        ZborderService zborderService = applicationContext.getBean(ZborderServiceImpl.class);
        Zborder zborder =zborderService.getById(id);
        if (zborder!=null&&zborder.getStatus()==0){
            zborderService.cancel(id);
        }

    }
}
