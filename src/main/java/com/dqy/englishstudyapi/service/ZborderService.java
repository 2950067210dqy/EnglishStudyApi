package com.dqy.englishstudyapi.service;

import com.dqy.englishstudyapi.tablebean.Zborder;
import com.baomidou.mybatisplus.extension.service.IService;
import com.dqy.englishstudyapi.vo.SubReturnVo;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 邓亲优
 * @since 2023-02-20
 */
public interface ZborderService extends IService<Zborder> {

    SubReturnVo cancel(Integer id);
    SubReturnVo getDataByUidAndDay();
    SubReturnVo getDataByUidAndWeek();
    SubReturnVo getDataByUidAndMonth();
}
