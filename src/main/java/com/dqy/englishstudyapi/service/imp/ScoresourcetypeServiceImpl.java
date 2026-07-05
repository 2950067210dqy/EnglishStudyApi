package com.dqy.englishstudyapi.service.imp;

import com.dqy.englishstudyapi.tablebean.Scoresourcetype;
import com.dqy.englishstudyapi.mapper.ScoresourcetypeMapper;
import com.dqy.englishstudyapi.service.ScoresourcetypeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 邓亲优
 * @since 2023-02-20
 */
@Service
//开启事务回滚
@Transactional(rollbackFor = RuntimeException.class)
public class ScoresourcetypeServiceImpl extends ServiceImpl<ScoresourcetypeMapper, Scoresourcetype> implements ScoresourcetypeService {

}
