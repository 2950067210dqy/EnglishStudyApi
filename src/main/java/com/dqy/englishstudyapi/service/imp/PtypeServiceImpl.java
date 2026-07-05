package com.dqy.englishstudyapi.service.imp;

import com.dqy.englishstudyapi.tablebean.Ptype;
import com.dqy.englishstudyapi.mapper.PtypeMapper;
import com.dqy.englishstudyapi.service.PtypeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 邓亲优
 * @since 2023-02-21
 */
//开启事务回滚
@Transactional(rollbackFor = RuntimeException.class)
@Service
public class PtypeServiceImpl extends ServiceImpl<PtypeMapper, Ptype> implements PtypeService {

}
