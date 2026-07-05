package com.dqy.englishstudyapi.service;

import com.dqy.englishstudyapi.tablebean.Signin;
import com.baomidou.mybatisplus.extension.service.IService;
import com.dqy.englishstudyapi.vo.SubReturnVo;

import java.time.LocalDate;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 邓亲优
 * @since 2023-02-20
 */
public interface SigninService extends IService<Signin> {

    SubReturnVo signIn(Integer uid, LocalDate date);

    SubReturnVo getSignIn(Integer uid);

    SubReturnVo getSeriesDayNum(Integer uid);

    SubReturnVo signInAfter(Integer uid, LocalDate date);
}
