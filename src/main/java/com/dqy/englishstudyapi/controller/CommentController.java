package com.dqy.englishstudyapi.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dqy.englishstudyapi.entity.frontEntity.StudyCircleFull.StudyCircleComment;
import com.dqy.englishstudyapi.entity.frontEntity.StudyCircleFull.StudyCircleSubComment;
import com.dqy.englishstudyapi.service.*;
import com.dqy.englishstudyapi.tablebean.*;
import com.dqy.englishstudyapi.util.Base64Util;
import com.dqy.englishstudyapi.util.JsonUtil;
import com.dqy.englishstudyapi.util.TimeUtil;
import com.dqy.englishstudyapi.vo.ReturnVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 邓亲优
 * @since 2023-02-24
 */
@RestController
@RequestMapping("comment")
public class CommentController {
    @Autowired
    CommentService commentService;
    @Autowired
    StudycircleService studycircleService;
    @Autowired
    ScoreService scoreService;
    @Autowired
    ScoresourceService scoresourceService;
    @Autowired
    ScoresourcetypeService scoresourcetypeService;
    @Autowired
    UserService userService;
    @Autowired
    TimeUtil timeUtil;
    @Autowired
    JsonUtil jsonUtil;
    @Autowired
    Base64Util base64Util;
    ReturnVO returnVO;


    @PostMapping("/setComment")
    public  ReturnVO setComment(@RequestParam("data")String data){
        returnVO=new ReturnVO();
        if (data!=null&&!data.equals("")){
            Comment comment = (Comment) jsonUtil.parseBase64ToJsonStrThenToJavaObject(data,Comment.class);
            comment.setDeleted(0);
            comment.setCreatetime(timeUtil.getCurrentTimeLocalDateTime());
            boolean result = commentService.save(comment);
            if (result){
                Score score = scoreService.getOne(new QueryWrapper<Score>().eq("uid",comment.getUid()));
                if (score!=null){
                    Scoresourcetype scoresourcetype = scoresourcetypeService.getById(7);
                    if (scoresourcetype!=null){
                        score.setScore(score.getScore()+scoresourcetype.getDefaults());
                        boolean result2 =  scoreService.updateById(score);
                        if (result2){
                            Scoresource scoresource = new Scoresource();
                            scoresource.setSourceid(8);
                            scoresource.setNum(Long.valueOf(scoresourcetype.getDefaults()));
                            scoresource.setDeleted(0);
                            scoresource.setUid(comment.getUid());
                            scoresource.setCreatetime(timeUtil.getCurrentTimeLocalDateTime());
                            boolean result3= scoresourceService.save(scoresource);
                            if (result3){
                                returnVO.setCode(200);
                                returnVO.setMessage("评论成功");
                                if (comment.getIssub()==0){
                                    //一级评论
                                    StudyCircleComment studyCircleComment = new StudyCircleComment();
                                    User user0 = userService.getById(comment.getUid());
                                    studyCircleComment.setUser(user0);
                                    studyCircleComment.setComment(comment);
                                    returnVO.setData(studyCircleComment);
                                }else {
                                    StudyCircleSubComment studyCircleSubComment = new StudyCircleSubComment();
                                    User user = userService.getById(comment.getForuser());
                                    User user2 = userService.getById(comment.getUid());
                                    studyCircleSubComment.setComment(comment);
                                    studyCircleSubComment.setUser(user2);
                                    studyCircleSubComment.setForUser(user);
                                    returnVO.setData(studyCircleSubComment);
                                    //二级评论
                                }
                                return returnVO;
                            }else{
                                returnVO.setCode(500);
                                returnVO.setMessage("添加scoresourse失败");
                                return returnVO;
                            }
                        }else{
                            returnVO.setCode(500);
                            returnVO.setMessage("更新score失败");
                            return returnVO;
                        }
                    }else{
                        returnVO.setCode(500);
                        returnVO.setMessage("获取scoresourcetype失败");
                        return returnVO;
                    }
                }else{
                    returnVO.setCode(500);
                    returnVO.setMessage("获取积分失败");
                    return returnVO;
                }

            }else{
                returnVO.setCode(500);
                returnVO.setMessage("评论为空");
                return returnVO;
            }
        }else{
            returnVO.setCode(500);
            returnVO.setMessage("数据不能为空");
            return returnVO;
        }
    }

    @PostMapping("/deleteComment")
    public  ReturnVO deleteComment(@RequestParam("id")Integer id){
        returnVO=new ReturnVO();
        if (id!=null){
                boolean result = commentService.removeById(id);
                if (result){
                    returnVO.setCode(200);
                    returnVO.setMessage("删除评论成功");
                    return returnVO;
                }else{
                    returnVO.setCode(500);
                    returnVO.setMessage("删除评论失败");
                    return returnVO;
                }
        }else{
            returnVO.setCode(500);
            returnVO.setMessage("数据不能为空");
            return returnVO;
        }
    }
}
