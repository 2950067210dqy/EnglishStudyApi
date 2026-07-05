package com.dqy.englishstudyapi.service.imp;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dqy.englishstudyapi.service.TestService;
import com.dqy.englishstudyapi.tablebean.Test;
import com.dqy.englishstudyapi.tablebean.Testerror;
import com.dqy.englishstudyapi.mapper.TesterrorMapper;
import com.dqy.englishstudyapi.service.TesterrorService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dqy.englishstudyapi.tablebean.Testlike;
import com.dqy.englishstudyapi.util.TimeUtil;
import com.dqy.englishstudyapi.vo.SubReturnVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

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
public class TesterrorServiceImpl extends ServiceImpl<TesterrorMapper, Testerror> implements TesterrorService {

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
        List<Testerror> testerrors = list(new QueryWrapper<Testerror>().eq("uid",uid));
        if (testerrors!=null&&testerrors.size()!=0){
            ArrayList<Test> errors = new ArrayList<>();
            for (Testerror testerror:testerrors
            ) {
                Test test = testService.getById(testerror.getTesttype(),testerror.getTestid());
                if (test!=null){
                    errors.add(test);
                }
            }
            if (errors.size()!=0){
                subReturnVo.setResult(true);
                subReturnVo.setCode(200);
                subReturnVo.setMessage("获取错题本成功");
                subReturnVo.setData(errors);
                return subReturnVo;
            }else{
                subReturnVo.setResult(false);
                subReturnVo.setCode(500);
                subReturnVo.setMessage("未有错题");
                return subReturnVo;
            }
        }else{
            subReturnVo.setResult(false);
            subReturnVo.setCode(500);
            subReturnVo.setMessage("testerror数据不存在或获取错误");
            return subReturnVo;
        }

    }
}
