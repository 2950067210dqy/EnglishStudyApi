package com.dqy.englishstudyapi.service.imp;

import com.dqy.englishstudyapi.tablebean.User;
import com.dqy.englishstudyapi.mapper.UserMapper;
import com.dqy.englishstudyapi.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 邓亲优
 * @since 2023-02-11
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

}
