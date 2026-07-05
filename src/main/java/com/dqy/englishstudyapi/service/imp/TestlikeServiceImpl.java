package com.dqy.englishstudyapi.service.imp;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dqy.englishstudyapi.entity.frontEntity.TestFront;
import com.dqy.englishstudyapi.service.TestService;
import com.dqy.englishstudyapi.tablebean.Test;
import com.dqy.englishstudyapi.tablebean.Testlike;
import com.dqy.englishstudyapi.mapper.TestlikeMapper;
import com.dqy.englishstudyapi.service.TestlikeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dqy.englishstudyapi.util.TimeUtil;
import com.dqy.englishstudyapi.vo.SubReturnVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.management.openmbean.TabularType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 邓亲优
 * @since 2023-02-28
 */
@Service
//开启事务回滚
@Transactional(rollbackFor = RuntimeException.class)
public class TestlikeServiceImpl extends ServiceImpl<TestlikeMapper, Testlike> implements TestlikeService {
    @Autowired
    TestService testService;
    @Autowired
    TimeUtil timeUtil;
    @Override
    public SubReturnVo getFull(Integer uid) {
        SubReturnVo subReturnVo = new SubReturnVo();
        if (uid==null){
            subReturnVo.setResult(false);
            subReturnVo.setCode(500);
            subReturnVo.setMessage("uid数据不存在");
            return subReturnVo;
        }
        List<Testlike> testlikes = list(new QueryWrapper<Testlike>().eq("uid",uid));
        if (testlikes!=null&&testlikes.size()!=0){
            ArrayList<Test> shoucangs = new ArrayList<>();
            for (Testlike testlike:testlikes
                 ) {
                Test test = testService.getById(testlike.getTesttype(),testlike.getTestid());
                if (test!=null){
                    shoucangs.add(test);
                }
            }
            if (shoucangs.size()!=0){
                subReturnVo.setResult(true);
                subReturnVo.setCode(200);
                subReturnVo.setMessage("获取收藏成功");
                subReturnVo.setData(shoucangs);
                return subReturnVo;
            }else{
                subReturnVo.setResult(false);
                subReturnVo.setCode(500);
                subReturnVo.setMessage("未有收藏");
                return subReturnVo;
            }
        }else{
            subReturnVo.setResult(false);
            subReturnVo.setCode(500);
            subReturnVo.setMessage("testlikes数据不存在或获取错误");
            return subReturnVo;
        }

    }

    public TestFront setLike(Test test, Integer uid, Integer  testtype) {

        if (test==null){
            return null;
        }else{
            TestFront testFront = new TestFront();
            Map<String,Object> params = new HashMap<>();
            params.put("uid",uid);
            params.put("testtype",testtype);
            testFront.setTest(test);
            params.put("testid", test.getId());
            Testlike testlike = getOne(new QueryWrapper<Testlike>().allEq(params));
            if (testlike!=null){
                testFront.setIsLike(true);
            }else{
                testFront.setIsLike(false);
            }



            return  testFront;
        }


    }
    @Override
    public List<TestFront> setLike(List<Test> testList, Integer uid, Integer  testtype) {

        if (testList==null||testList.size()==0){
            return null;
        }else{
            ArrayList<TestFront> testFronts = new ArrayList<>();
            Map<String,Object> params = new HashMap<>();
            params.put("uid",uid);
            params.put("testtype",testtype);
            for (Test t:testList
                 ) {
                TestFront testFront = new TestFront();
                testFront.setTest(t);
                params.put("testid", t.getId());
                Testlike testlike = getOne(new QueryWrapper<Testlike>().allEq(params));
                if (testlike!=null){
                    testFront.setIsLike(true);
                }else{
                    testFront.setIsLike(false);
                }
                testFronts.add(testFront);

            }
            return  testFronts;
        }


    }
}
