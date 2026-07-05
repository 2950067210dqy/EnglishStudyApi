package com.dqy.englishstudyapi.service.imp;

import com.dqy.englishstudyapi.tablebean.Recitedata;
import com.dqy.englishstudyapi.mapper.RecitedataMapper;
import com.dqy.englishstudyapi.service.RecitedataService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 邓亲优
 * @since 2023-02-19
 */
@Service
//开启事务回滚
@Transactional(rollbackFor = RuntimeException.class)
public class RecitedataServiceImpl extends ServiceImpl<RecitedataMapper, Recitedata> implements RecitedataService {

}
