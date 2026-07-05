package com.dqy.englishstudyapi.service.imp;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dqy.englishstudyapi.service.ScoreService;
import com.dqy.englishstudyapi.service.ScoresourceService;
import com.dqy.englishstudyapi.service.ScoresourcetypeService;
import com.dqy.englishstudyapi.tablebean.Score;
import com.dqy.englishstudyapi.tablebean.Scoresource;
import com.dqy.englishstudyapi.tablebean.Scoresourcetype;
import com.dqy.englishstudyapi.tablebean.Signin;
import com.dqy.englishstudyapi.mapper.SigninMapper;
import com.dqy.englishstudyapi.service.SigninService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dqy.englishstudyapi.util.TimeUtil;
import com.dqy.englishstudyapi.vo.SubReturnVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 邓亲优
 * @since 2023-02-20
 */
@Service
public class SigninServiceImpl extends ServiceImpl<SigninMapper, Signin> implements SigninService {
    @Autowired
    TimeUtil timeUtil;
    @Autowired
    ScoreService scoreService;
    @Autowired
    ScoresourcetypeService scoresourcetypeService;
    @Autowired
    ScoresourceService scoresourceService;
    @Override
    public SubReturnVo getSeriesDayNum(Integer uid) {
        SubReturnVo subReturnVo = new SubReturnVo();
         subReturnVo =getSignIn(uid);
        if (subReturnVo.isResult()) {
            ArrayList<Signin> signins = (ArrayList<Signin>) subReturnVo.getData();
            List<LocalDate> localDates = new ArrayList<>();
            for (Signin s : signins
            ) {
                localDates.add(s.getCreatedate());
            }
            int contiuesday = timeUtil.continuousDay(localDates);
            subReturnVo.setResult(true);
            subReturnVo.setData(contiuesday);
            return  subReturnVo;
        }else{
            subReturnVo.setResult(false);
            subReturnVo.setMessage(subReturnVo.getMessage());
            return  subReturnVo;
        }
    }

    @Override
    public SubReturnVo signInAfter(Integer uid, LocalDate date) {
        SubReturnVo subReturnVo = new SubReturnVo();
        ArrayList<Scoresourcetype> scoresourcetypes = (ArrayList<Scoresourcetype>) scoresourcetypeService.list();
        Integer defaults = 0;
        if (scoresourcetypes!=null&&scoresourcetypes.size()!=0){
            for (Scoresourcetype s:scoresourcetypes
            ) {
                if (s.getId()==12){
                    defaults=s.getDefaults();
                    break;
                }
            }
        }else{
            subReturnVo.setCode(500);
            subReturnVo.setResult(false);
            subReturnVo.setMessage("获取Scoresourcetype错误");
            return subReturnVo;
        }
        Score score = scoreService.getOne(new QueryWrapper<Score>().eq("uid",uid));
        if (score!=null){
            if (score.getScore()>=defaults){
                score.setScore(score.getScore()-defaults);
                boolean result = scoreService.updateById(score);
                if (result){
                    Scoresource scoresource = new Scoresource();
                    scoresource.setNum(Long.valueOf(defaults));
                    scoresource.setSourceid(12);
                    scoresource.setDeleted(0);
                    scoresource.setUid(uid);
                    scoresource.setCreatetime(timeUtil.getCurrentTimeLocalDateTime());
                    boolean result2 = scoresourceService.save(scoresource);
                    if (result2){
                        subReturnVo =  signIn(uid,date);
                        subReturnVo.setCode(200);
                        return subReturnVo;
                    }else{
                        subReturnVo.setResult(false);
                        subReturnVo.setMessage("scoresource添加失败");
                        subReturnVo.setCode(500);
                        return subReturnVo;
                    }

                }else{
                    subReturnVo.setResult(false);
                    subReturnVo.setMessage("积分扣除失败");
                    subReturnVo.setCode(500);
                    return subReturnVo;
                }
            }else{
                subReturnVo.setResult(false);
                subReturnVo.setMessage("积分不足");
                subReturnVo.setCode(550);
                return subReturnVo;
            }
        }else{
            subReturnVo.setResult(false);
            subReturnVo.setMessage("尚未有积分");
            subReturnVo.setCode(550);
            return subReturnVo;
        }

    }

    @Override
    public SubReturnVo signIn(Integer uid, LocalDate date) {
        SubReturnVo subReturnVo = new SubReturnVo();
        Signin signin = new Signin();
        signin.setDeleted(0);
        signin.setCreatetime(timeUtil.getCurrentTimeLocalDateTime());
        signin.setUid(uid);
        signin.setCreatedate(date);
        boolean result = save(signin);
        if (result){
            if (date.isEqual(timeUtil.getNowLocalDate())){
                subReturnVo =getSeriesDayNum(uid);
                if (subReturnVo.isResult()){

                    int contiuesday = (int) subReturnVo.getData();
                    if (contiuesday%30==0){
                        contiuesday=1;
                    }else {
                        contiuesday=contiuesday%30;
                    }
                    subReturnVo= scoreService.setScore(uid,10, Long.valueOf(contiuesday));
                    return  subReturnVo;
                }else{
                    subReturnVo.setResult(false);
                    subReturnVo.setMessage(subReturnVo.getMessage());
                    return subReturnVo;
                }
            }else{
                //补签
                subReturnVo= scoreService.setScore(uid,10,null);
                return  subReturnVo;
            }


        }else{
            subReturnVo.setResult(false);
            subReturnVo.setMessage("保存signin表失败");
            return subReturnVo;
        }

    }

    @Override
    public SubReturnVo getSignIn(Integer uid) {
        SubReturnVo subReturnVo = new SubReturnVo();
        ArrayList<Signin> signins = (ArrayList<Signin>) list(new QueryWrapper<Signin>().eq("uid",uid));
        if (signins!=null&&signins.size()!=0){
            subReturnVo.setData(signins);
            subReturnVo.setResult(true);
            return subReturnVo;
        }else {
            subReturnVo.setMessage("获取签到数据错误");
            subReturnVo.setResult(false);
            return subReturnVo;
        }
    }


}
