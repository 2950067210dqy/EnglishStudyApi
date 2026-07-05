package com.dqy.englishstudyapi.service;

import com.dqy.englishstudyapi.tablebean.Score;
import com.baomidou.mybatisplus.extension.service.IService;
import com.dqy.englishstudyapi.tablebean.Zborder;
import com.dqy.englishstudyapi.vo.SubReturnVo;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 邓亲优
 * @since 2023-02-20
 */
public interface ScoreService extends IService<Score> {

    SubReturnVo setScore(Integer uid, Integer type,Long num);

    SubReturnVo reCharge(Zborder zborder);
}
