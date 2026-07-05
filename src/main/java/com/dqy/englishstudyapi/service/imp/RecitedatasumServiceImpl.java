package com.dqy.englishstudyapi.service.imp;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.QueryChainWrapper;
import com.dqy.englishstudyapi.tablebean.Recitedatasum;
import com.dqy.englishstudyapi.mapper.RecitedatasumMapper;
import com.dqy.englishstudyapi.service.RecitedatasumService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dqy.englishstudyapi.util.TimeUtil;
import com.dqy.englishstudyapi.vo.SubReturnVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 邓亲优
 * @since 2023-03-02
 */
@Service
//开启事务回滚
@Transactional(rollbackFor = RuntimeException.class)
public class RecitedatasumServiceImpl extends ServiceImpl<RecitedatasumMapper, Recitedatasum> implements RecitedatasumService {
    @Autowired
    TimeUtil timeUtil;
    @Override
    public SubReturnVo setData(Recitedatasum recitedatasum) {
        SubReturnVo subReturnVo = new SubReturnVo();
        if (recitedatasum==null){
            subReturnVo.setResult(false);
            subReturnVo.setMessage("数据为空");
            subReturnVo.setCode(500);
            return  subReturnVo;
        }else{
            Recitedatasum storeRd = getOne(new QueryWrapper<Recitedatasum>().eq("uid",recitedatasum.getUid()) );
            if (storeRd==null){
                //insert
                recitedatasum.setCreatedate(timeUtil.getNowLocalDate());
                recitedatasum.setDeleted(0);
                recitedatasum.setCreatetime(timeUtil.getNowLocalDateTime());
                boolean result = save(recitedatasum);
                if (result){
                    subReturnVo.setResult(true);
                    subReturnVo.setMessage("存储总数成功");
                    subReturnVo.setCode(200);
                    return  subReturnVo;
                }else{
                    subReturnVo.setResult(false);
                    subReturnVo.setMessage("存储总数失败");
                    subReturnVo.setCode(500);
                    return  subReturnVo;
                }
            }else{
                //update
                storeRd.setCountnum(storeRd.getCountnum()+recitedatasum.getCountnum());
                storeRd.setNum(storeRd.getNum()+recitedatasum.getNum());
                storeRd.setTime(storeRd.getTime()+recitedatasum.getTime());
                storeRd.setNum2(storeRd.getNum2()+recitedatasum.getNum2());
                boolean result =updateById(storeRd);
                if (result){
                    subReturnVo.setResult(true);
                    subReturnVo.setMessage("更新存储总数成功");
                    subReturnVo.setCode(200);
                    return  subReturnVo;
                }else{
                    subReturnVo.setResult(false);
                    subReturnVo.setMessage("更新存储总数失败");
                    subReturnVo.setCode(500);
                    return  subReturnVo;
                }
            }
        }

    }
}
