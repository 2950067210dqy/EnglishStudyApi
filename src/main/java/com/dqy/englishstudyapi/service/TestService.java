package com.dqy.englishstudyapi.service;

import com.dqy.englishstudyapi.tablebean.Test;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 邓亲优
 * @since 2023-02-28
 */
public interface TestService extends IService<Test> {

    List<Test> list(Integer testtype, Integer current, Integer size);

    Integer count(Integer testtype);

    Test getById(Integer testtype, Integer testid);

    List<Test> listRandom(Integer testtype, Integer random, Integer size);

    boolean createTableIfNotExist(Integer id);
}
