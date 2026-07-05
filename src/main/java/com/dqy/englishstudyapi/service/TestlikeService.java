package com.dqy.englishstudyapi.service;

import com.dqy.englishstudyapi.entity.frontEntity.TestFront;
import com.dqy.englishstudyapi.tablebean.Test;
import com.dqy.englishstudyapi.tablebean.Testlike;
import com.baomidou.mybatisplus.extension.service.IService;
import com.dqy.englishstudyapi.vo.SubReturnVo;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 邓亲优
 * @since 2023-02-28
 */
public interface TestlikeService extends IService<Testlike> {

    SubReturnVo getFull(Integer uid);

    List<TestFront> setLike(List<Test> testList, Integer uid, Integer  testtype);

    TestFront setLike(Test test, Integer uid, Integer id);
}
