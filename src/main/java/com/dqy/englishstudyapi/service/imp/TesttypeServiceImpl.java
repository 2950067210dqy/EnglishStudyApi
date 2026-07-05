package com.dqy.englishstudyapi.service.imp;

import com.dqy.englishstudyapi.tablebean.Testtype;
import com.dqy.englishstudyapi.mapper.TesttypeMapper;
import com.dqy.englishstudyapi.service.TesttypeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 邓亲优
 * @since 2023-02-28
 */

@Service
//开启事务回滚
@Transactional(rollbackFor = RuntimeException.class)
public class TesttypeServiceImpl extends ServiceImpl<TesttypeMapper, Testtype> implements TesttypeService {

}
