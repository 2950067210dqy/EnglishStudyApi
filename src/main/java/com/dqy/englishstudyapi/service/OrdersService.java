package com.dqy.englishstudyapi.service;

import com.dqy.englishstudyapi.tablebean.Orders;
import com.baomidou.mybatisplus.extension.service.IService;
import com.dqy.englishstudyapi.vo.SubReturnVo;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 邓亲优
 * @since 2023-02-22
 */
public interface OrdersService extends IService<Orders> {

    SubReturnVo cancel(Integer id);
}
