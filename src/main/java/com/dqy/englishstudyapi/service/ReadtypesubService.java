package com.dqy.englishstudyapi.service;

import com.dqy.englishstudyapi.tablebean.Readtypesub;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 邓亲优
 * @since 2023-03-02
 */
public interface ReadtypesubService extends IService<Readtypesub> {
     void createTable(Integer suffix);


}
