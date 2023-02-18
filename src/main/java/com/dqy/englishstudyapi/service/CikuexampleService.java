package com.dqy.englishstudyapi.service;

import com.dqy.englishstudyapi.tablebean.Cikuexample;
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
public interface CikuexampleService extends IService<Cikuexample> {

    Integer count(Integer cikutypeId, Integer cikuId);

    boolean insertBatch(Integer cikuTypeId, Integer cikuId, ArrayList<Cikuexample> cikuexamples);

    ArrayList<Cikuexample> list(Integer cikuTypeId, Integer cikuId);

    ArrayList<Cikuexample> listByIds(Integer cikutypeid, Integer cikuid, Integer[] cikuexampleids);
}
