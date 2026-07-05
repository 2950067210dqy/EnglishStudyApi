package com.dqy.englishstudyapi.service;

import com.dqy.englishstudyapi.tablebean.Testerror;
import com.baomidou.mybatisplus.extension.service.IService;
import com.dqy.englishstudyapi.vo.SubReturnVo;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 邓亲优
 * @since 2023-02-28
 */
public interface TesterrorService extends IService<Testerror> {

    SubReturnVo getFull(Integer uid);
}
