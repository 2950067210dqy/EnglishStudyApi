package com.dqy.englishstudyapi.service;

import com.dqy.englishstudyapi.entity.frontEntity.ImportWordsEntity;
import com.dqy.englishstudyapi.tablebean.Ciku;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.ArrayList;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 邓亲优
 * @since 2023-02-03
 */
public interface CikuService extends IService<Ciku> {

    boolean importWords(ImportWordsEntity param);

}
