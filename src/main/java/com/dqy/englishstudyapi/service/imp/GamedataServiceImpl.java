package com.dqy.englishstudyapi.service.imp;

import com.dqy.englishstudyapi.tablebean.Gamedata;
import com.dqy.englishstudyapi.mapper.GamedataMapper;
import com.dqy.englishstudyapi.service.GamedataService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 邓亲优
 * @since 2023-04-02
 */
@Service
@Transactional(rollbackFor = RuntimeException.class)
public class GamedataServiceImpl extends ServiceImpl<GamedataMapper, Gamedata> implements GamedataService {

}
