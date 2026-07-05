package com.dqy.englishstudyapi.service;

import com.dqy.englishstudyapi.tablebean.Liju;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 邓亲优
 * @since 2023-02-11
 */
public interface LijuService extends IService<Liju> {

    Liju getByWord(String initial, String word);

    void createTable(String initial);
}
