package com.dqy.englishstudyapi.service.imp;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.dqy.englishstudyapi.service.*;
import com.dqy.englishstudyapi.tablebean.*;
import com.dqy.englishstudyapi.mapper.ScoreMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dqy.englishstudyapi.util.JsonUtil;
import com.dqy.englishstudyapi.util.TimeUtil;
import com.dqy.englishstudyapi.vo.SubReturnVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 邓亲优
 * @since 2023-02-20
 */
@Service
//开启事务回滚
@Transactional(rollbackFor = RuntimeException.class)
public class ScoreServiceImpl extends ServiceImpl<ScoreMapper, Score> implements ScoreService {
    @Autowired
    ScoresourceService scoresourceService;
    @Autowired
    ScoresourcetypeService scoresourcetypeService;
    @Autowired
    UserService userService;
    @Autowired
    ZborderService zborderService;
    @Autowired
    JsonUtil jsonUtil;
    @Autowired
    TimeUtil timeUtil;


    public SubReturnVo reCharge(Zborder zborder){
        SubReturnVo subReturnVo = new SubReturnVo();
        Map<String,Object> paramss= new HashMap<>();
        paramss.put("uid", Integer.valueOf(zborder.getUid()));
        paramss.put("zborderid",zborder.getZborderid());
        boolean result =  zborderService.saveOrUpdate(zborder,new UpdateWrapper<Zborder>().allEq(paramss));
        if (result){
            Scoresource scoresource = new Scoresource();
            scoresource.setCreatetime(timeUtil.getCurrentTimeLocalDateTime());
            scoresource.setDeleted(0);
            scoresource.setUid(zborder.getUid());
            scoresource.setSourceid(1);
            scoresource.setNum(Long.valueOf(zborder.getDsc()));
            boolean result2  = scoresourceService.save(scoresource);
            if (result2){
               Score score =   getOne(new QueryWrapper<Score>().eq("uid",zborder.getUid()));
               if (score!=null){
                   score.setScore(score.getScore()+Long.valueOf(zborder.getDsc()));
                   boolean result3 = updateById(score);
                   if (result3){
                       subReturnVo.setCode(200);
                       subReturnVo.setResult(true);
                       subReturnVo.setMessage("更新score成功");
                       return  subReturnVo;
                   }else{
                       subReturnVo.setCode(500);
                       subReturnVo.setResult(false);
                       subReturnVo.setMessage("更新score表错误");
                       return  subReturnVo;
                   }
               }else{
                   score = new Score();
                   score.setScore(Long.valueOf(zborder.getDsc()));
                   score.setDeleted(0);
                   score.setUid(zborder.getUid());
                   score.setCreatetime(timeUtil.getCurrentTimeLocalDateTime());
                   boolean result3 = save(score);
                   if (result3){
                       subReturnVo.setCode(200);
                       subReturnVo.setResult(true);
                       subReturnVo.setMessage("保存score成功");
                       return  subReturnVo;
                   }else{
                       subReturnVo.setCode(500);
                       subReturnVo.setResult(false);
                       subReturnVo.setMessage("保存score表错误");
                       return  subReturnVo;
                   }
               }
            }else{
                subReturnVo.setCode(500);
                subReturnVo.setResult(false);
                subReturnVo.setMessage("插入scoresource表错误");
                return  subReturnVo;
            }
        }else{
            subReturnVo.setCode(500);
            subReturnVo.setResult(false);
            subReturnVo.setMessage("更新订单错误");
            return  subReturnVo;
        }

    }

    @Override
    public SubReturnVo setScore(Integer uid, Integer type,Long num) {
        SubReturnVo subReturnVo = new SubReturnVo();
        subReturnVo.setResult(false);
        User user = userService.getById(uid);
        if (user!=null){
            Scoresourcetype scoresourcetype = scoresourcetypeService.getById(type);
            if (scoresourcetype!=null){
                Scoresource scoresource = new Scoresource();
                scoresource.setUid(user.getId());
                scoresource.setDeleted(0);
                if (num==null){
                    scoresource.setNum(Long.valueOf(scoresourcetype.getDefaults()));
                }else{
                    scoresource.setNum(num);
                }
               scoresource.setSourceid(scoresourcetype.getId());
                scoresource.setCreatetime(timeUtil.getCurrentTimeLocalDateTime());
                boolean result = scoresourceService .save(scoresource);
                if (result){
                    Score score = getOne(new QueryWrapper<Score>().eq("uid",user.getId()));
                    if (score!=null){
                        //更新
                        if (num==null){
                            score.setScore(score.getScore()+ Long.valueOf(scoresourcetype.getDefaults()));
                        }else{
                            score.setScore(score.getScore()+num);
                        }
                        boolean result2= updateById(score);
                        if (result2){
                            subReturnVo.setResult(true);
                            subReturnVo.setMessage("score保存更新成功");
                            return  subReturnVo;
                        }else{
                            subReturnVo.setMessage("score保存更新错误");
                            return  subReturnVo;
                        }
                    }else{
                        //插入
                        score = new Score();
                        if (num==null){
                            score.setScore(Long.valueOf(scoresourcetype.getDefaults()));
                        }else{
                            score.setScore(num);
                        }
                        score.setCreatetime(timeUtil.getCurrentTimeLocalDateTime());
                        score.setDeleted(0);
                        score.setUid(user.getId());
                        boolean result2= save(score);
                        if (result2){
                            subReturnVo.setResult(true);
                            subReturnVo.setMessage("score保存成功");
                            return  subReturnVo;
                        }else{
                            subReturnVo.setMessage("score保存错误");
                            return  subReturnVo;
                        }
                    }
                }else{
                    subReturnVo.setMessage("scoresource保存错误");
                    return  subReturnVo;
                }
            }else{
                subReturnVo.setMessage("type不存在");
                return  subReturnVo;
            }
        }else{
            subReturnVo.setMessage("用户不存在");
            return  subReturnVo;
        }
    }
}
