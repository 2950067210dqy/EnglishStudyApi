package com.dqy.englishstudyapi.service;

import com.dqy.englishstudyapi.entity.frontEntity.ImportWordsEntity;
import com.dqy.englishstudyapi.tablebean.Ciku;
import com.baomidou.mybatisplus.extension.service.IService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
    ArrayList<Ciku> list(Integer tableId);

    Ciku selectByDsc(Integer cikuTypeId, String dsc);

    boolean save(Integer cikuTypeId, Ciku ciku);

    Ciku selectById(Integer cikuTypeId, Integer id);

    List<Ciku> listByUid(Integer cikutypeId, Integer uid);

    boolean shouCang(Ciku shoucang);

    boolean shouCangCancel(Ciku shoucang);


    List<Ciku> select(Integer cikutypeId);

    List<Ciku> selectByToday(Integer cikutypeId, LocalDateTime startDateTime, LocalDateTime nowDateTime);

    Ciku selectOrderByDescAndLimit(Integer cikutypeId, String column, int start, int size);

    void createTable(Integer cikutypeId);
}
