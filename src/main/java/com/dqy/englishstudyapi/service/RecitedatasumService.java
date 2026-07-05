package com.dqy.englishstudyapi.service;

import com.dqy.englishstudyapi.tablebean.Recitedatasum;
import com.baomidou.mybatisplus.extension.service.IService;
import com.dqy.englishstudyapi.vo.SubReturnVo;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 邓亲优
 * @since 2023-03-02
 */
public interface RecitedatasumService extends IService<Recitedatasum> {

    SubReturnVo setData(Recitedatasum recitedatasum);
}
