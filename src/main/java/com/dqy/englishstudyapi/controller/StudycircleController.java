package com.dqy.englishstudyapi.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dqy.englishstudyapi.entity.frontEntity.OrderFull.OrderFull;
import com.dqy.englishstudyapi.entity.frontEntity.StudyCircleFull.StudyCircleComment;
import com.dqy.englishstudyapi.entity.frontEntity.StudyCircleFull.StudyCircleFull;
import com.dqy.englishstudyapi.entity.frontEntity.StudyCircleFull.StudyCircleSubComment;
import com.dqy.englishstudyapi.entity.page.MyPage;
import com.dqy.englishstudyapi.service.*;
import com.dqy.englishstudyapi.tablebean.*;
import com.dqy.englishstudyapi.util.Base64Util;
import com.dqy.englishstudyapi.util.JsonUtil;
import com.dqy.englishstudyapi.util.TimeUtil;
import com.dqy.englishstudyapi.vo.ReturnVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
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
@RequestMapping("studycircle")
public class StudycircleController {
    @Autowired
    StudycircleService studycircleService;
    @Autowired
    ScoreService scoreService;
    @Autowired
    ScoresourceService scoresourceService;
    @Autowired
    ScoresourcetypeService scoresourcetypeService;
    @Autowired
    TagsService tagsService;
    @Autowired
    UserService userService;
    @Autowired
    StudycirclelikeService studycirclelikeService;
    @Autowired
    CommentService commentService;
    @Autowired
    JsonUtil jsonUtil;
    @Autowired
    Base64Util base64Util;
    @Autowired
    TimeUtil timeUtil;

    ReturnVO returnVO;


    @PostMapping("/delete")
    public ReturnVO delete(@RequestParam("id")Integer id){
        returnVO = new ReturnVO();
        if (id==null){
            returnVO.setCode(500);
            returnVO.setMessage("数据不能为空");
            return returnVO;
        }else{
            Studycircle studycircle = studycircleService.getById(id);
            if (studycircle!=null){
                List<Comment> comments = commentService.list(new QueryWrapper<Comment>().eq("sid",studycircle.getId()));
                ArrayList<Integer> cids =null;
                if (comments!=null){
                    cids= new ArrayList<>();
                    for (Comment c:comments
                         ) {
                        cids.add(c.getId());
                    }
                }
                if (cids!=null&&cids.size()!=0){
                    boolean result2= commentService.removeBatchByIds(cids);
                    if (!result2){
                        returnVO.setCode(500);
                        returnVO.setMessage("删除相关评论失败");
                        return returnVO;
                    }
                }
                boolean result =   studycircleService.removeById(id);
                if (result){
                    returnVO.setCode(200);
                    returnVO.setMessage("删除成功");
                    return returnVO;
                }else{
                    returnVO.setCode(500);
                    returnVO.setMessage("删除失败");
                    return returnVO;
                }
            }else{
                returnVO.setCode(500);
                returnVO.setMessage("不存在该学习圈");
                return returnVO;
            }

        }
    }

    @PostMapping("/getByShouCang")
    public ReturnVO getByShouCang(@RequestParam("uid")Integer uid,
                        @RequestParam(value = "current",defaultValue = "1",required = false)Integer current,@RequestParam(value = "size",defaultValue = "3",required = false)Integer size,
                        @RequestParam(value = "tags",required = false) ArrayList<String> tag,@RequestParam(value = "allOrMe",required = false)Integer allOrMe
    ){
        returnVO= new ReturnVO();
        Page<Studycircle> page = new Page<>();
        page.setSize(size);
        page.setCurrent(current);
        List<Studycirclelike> studycirclelikes = studycirclelikeService.list(new QueryWrapper<Studycirclelike>().eq("uid",uid));
        if (studycirclelikes!=null&&studycirclelikes.size()!=0){
            List<Integer> sids = new ArrayList<>();
            for (Studycirclelike studycirclelike:studycirclelikes
                 ) {
                sids.add(studycirclelike.getSid());
            }

            QueryWrapper<Studycircle> condition = new QueryWrapper<>();
            condition.in("id",sids);
            if(tag!=null&&tag.size()>0){
                for (String t:tag
                ) {
                    condition.like("tag",t);
                }
            }
            if (allOrMe!=null&&allOrMe==1){
                condition.eq("uid",uid);
            }
            condition.orderByDesc("updatetime");
            IPage<Studycircle> iPage =studycircleService.page(page,condition);
            ArrayList<Studycircle> studycircles=null;
            ArrayList<StudyCircleFull> studyCircleFulls = null;
            if (iPage.getRecords()!=null&&iPage.getRecords().size()!=0){
                studycircles = (ArrayList<Studycircle>) iPage.getRecords();
                studyCircleFulls = new ArrayList<>();
                for (Studycircle s:studycircles
                ) {
                    StudyCircleFull studyCircleFull = new StudyCircleFull();
                    String[] tags = s.getTag().split(",");
                    ArrayList<Tags> tagsArrayList = null;
                    if (tags.length!=0){
                        if (tags.length==1){
                            tagsArrayList= (ArrayList<Tags>) tagsService.list(new QueryWrapper<Tags>().eq("id",tags[0]));
                        }else{
                            tagsArrayList= (ArrayList<Tags>) tagsService.list(new QueryWrapper<Tags>().eq("id",tags[0]).or().eq("id",tags[1]));
                        }
                    }
                    studyCircleFull.setTags(tagsArrayList);
                    studyCircleFull.setStudyCircle(s);
                    Map<String,Object> params = new HashMap<>();
                    params.put("sid",s.getId());
                    params.put("issub",0);
                    ArrayList<Comment> comments = (ArrayList<Comment>) commentService.list(new QueryWrapper<Comment>().allEq(params));
                    params.put("issub",1);
                    ArrayList<Comment> subComments = (ArrayList<Comment>) commentService.list(new QueryWrapper<Comment>().allEq(params));
                    ArrayList<StudyCircleComment> studyCircleComments = null;
                    if (comments!=null&&comments.size()!=0){
                        studyCircleComments= new ArrayList<>();
                        for (Comment c:comments
                        ) {
                            StudyCircleComment studyCircleComment = new StudyCircleComment();
                            User user0 = userService.getById(c.getUid());
                            studyCircleComment.setUser(user0);
                            studyCircleComment.setComment(c);
                            if (subComments!=null&&subComments.size()!=0){
                                ArrayList<StudyCircleSubComment> studyCircleSubComments = new ArrayList<>();
                                for (Comment subC:subComments
                                ) {
                                    if (subC.getForcid()==c.getId()){
                                        StudyCircleSubComment studyCircleSubComment = new StudyCircleSubComment();
                                        User user = userService.getById(subC.getForuser());
                                        User user2 = userService.getById(subC.getUid());
                                        studyCircleSubComment.setComment(subC);
                                        studyCircleSubComment.setUser(user2);
                                        studyCircleSubComment.setForUser(user);
                                        studyCircleSubComments.add(studyCircleSubComment);
                                    }

                                }
                                studyCircleComment.setSubComments(studyCircleSubComments);
                            }else{
                                studyCircleComment.setSubComments(null);
                            }
                            studyCircleComments.add(studyCircleComment);
                        }
                        studyCircleFull.setComments(studyCircleComments);
                    }else{
                        studyCircleFull.setComments(studyCircleComments);
                    }

                    Map<String,Object> params2 = new HashMap<>();
                    params2.put("sid",s.getId());
                    params2.put("uid",uid);
                    Studycirclelike studycirclelike = studycirclelikeService.getOne(new QueryWrapper<Studycirclelike>().allEq(params2));
                    if (studycirclelike==null){
                        studyCircleFull.setIslike(false);
                    }else{
                        studyCircleFull.setIslike(true);
                    }

                    User user = userService.getById(s.getUid());
                    studyCircleFull.setUser(user);
                    studyCircleFulls.add(studyCircleFull);
                }
            }
            if (studyCircleFulls!=null&&studyCircleFulls.size()!=0){
                MyPage<String> myPage = new MyPage<>();
                myPage.setTotal(Math.toIntExact(iPage.getTotal()));
                myPage.setPageSize(Math.toIntExact(iPage.getSize()));
                myPage.setCurrent(Math.toIntExact(iPage.getCurrent()));
                myPage.setOne(jsonUtil.parseArrayListToJsonStrThenToBase64(studyCircleFulls));
                returnVO.setCode(200);
                returnVO.setMessage("获取成功");
                returnVO.setData(myPage);
                return returnVO;
            }else{
                returnVO.setCode(500);
                returnVO.setMessage("获取学习圈错误2");
                return  returnVO;
            }
        }else{
            returnVO.setCode(500);
            returnVO.setMessage("获取学习圈错误1");
            return  returnVO;
        }

    }
    @PostMapping("/get")
    public ReturnVO get(@RequestParam("uid")Integer uid,
            @RequestParam(value = "current",defaultValue = "1",required = false)Integer current,@RequestParam(value = "size",defaultValue = "3",required = false)Integer size,
                        @RequestParam(value = "tags",required = false) ArrayList<String> tag,@RequestParam(value = "allOrMe",required = false)Integer allOrMe
                        ){
        returnVO= new ReturnVO();
        Page<Studycircle> page = new Page<>();
        page.setSize(size);
        page.setCurrent(current);
        QueryWrapper<Studycircle> condition = new QueryWrapper<>();
        if(tag!=null&&tag.size()>0){
            for (String t:tag
                 ) {
                condition.like("tag",t);
            }
        }
        if (allOrMe!=null&&allOrMe==1){
            condition.eq("uid",uid);
        }
        condition.orderByDesc("updatetime");
        IPage<Studycircle> iPage =studycircleService.page(page,condition);
        ArrayList<Studycircle> studycircles=null;
        ArrayList<StudyCircleFull> studyCircleFulls = null;
        if (iPage.getRecords()!=null&&iPage.getRecords().size()!=0){
            studycircles = (ArrayList<Studycircle>) iPage.getRecords();
            studyCircleFulls = new ArrayList<>();
            for (Studycircle s:studycircles
            ) {
                StudyCircleFull studyCircleFull = new StudyCircleFull();
                String[] tags = s.getTag().split(",");
                ArrayList<Tags> tagsArrayList = null;
                if (tags.length!=0){
                    if (tags.length==1){
                        tagsArrayList= (ArrayList<Tags>) tagsService.list(new QueryWrapper<Tags>().eq("id",tags[0]));
                    }else{
                        tagsArrayList= (ArrayList<Tags>) tagsService.list(new QueryWrapper<Tags>().eq("id",tags[0]).or().eq("id",tags[1]));
                    }
                }
                studyCircleFull.setTags(tagsArrayList);
                studyCircleFull.setStudyCircle(s);
                Map<String,Object> params = new HashMap<>();
                params.put("sid",s.getId());
                params.put("issub",0);
                ArrayList<Comment> comments = (ArrayList<Comment>) commentService.list(new QueryWrapper<Comment>().allEq(params));
                params.put("issub",1);
                ArrayList<Comment> subComments = (ArrayList<Comment>) commentService.list(new QueryWrapper<Comment>().allEq(params));
                ArrayList<StudyCircleComment> studyCircleComments = null;
                if (comments!=null&&comments.size()!=0){
                    studyCircleComments= new ArrayList<>();
                    for (Comment c:comments
                         ) {
                        StudyCircleComment studyCircleComment = new StudyCircleComment();
                        User user0 = userService.getById(c.getUid());
                        studyCircleComment.setUser(user0);
                        studyCircleComment.setComment(c);
                        if (subComments!=null&&subComments.size()!=0){
                            ArrayList<StudyCircleSubComment> studyCircleSubComments = new ArrayList<>();
                            for (Comment subC:subComments
                                 ) {
                                if (subC.getForcid()==c.getId()){
                                    StudyCircleSubComment studyCircleSubComment = new StudyCircleSubComment();
                                    User user = userService.getById(subC.getForuser());
                                    User user2 = userService.getById(subC.getUid());
                                    studyCircleSubComment.setComment(subC);
                                    studyCircleSubComment.setUser(user2);
                                    studyCircleSubComment.setForUser(user);
                                    studyCircleSubComments.add(studyCircleSubComment);
                                }

                            }
                            studyCircleComment.setSubComments(studyCircleSubComments);
                        }else{
                            studyCircleComment.setSubComments(null);
                        }
                        studyCircleComments.add(studyCircleComment);
                    }
                    studyCircleFull.setComments(studyCircleComments);
                }else{
                    studyCircleFull.setComments(studyCircleComments);
                }

                Map<String,Object> params2 = new HashMap<>();
                params2.put("sid",s.getId());
                params2.put("uid",uid);
                Studycirclelike studycirclelike = studycirclelikeService.getOne(new QueryWrapper<Studycirclelike>().allEq(params2));
                if (studycirclelike==null){
                    studyCircleFull.setIslike(false);
                }else{
                    studyCircleFull.setIslike(true);
                }

                User user = userService.getById(s.getUid());
                studyCircleFull.setUser(user);
                studyCircleFulls.add(studyCircleFull);
            }
        }
        if (studyCircleFulls!=null&&studyCircleFulls.size()!=0){
            MyPage<String> myPage = new MyPage<>();
            myPage.setTotal(Math.toIntExact(iPage.getTotal()));
            myPage.setPageSize(Math.toIntExact(iPage.getSize()));
            myPage.setCurrent(Math.toIntExact(iPage.getCurrent()));
            myPage.setOne(jsonUtil.parseArrayListToJsonStrThenToBase64(studyCircleFulls));
            returnVO.setCode(200);
            returnVO.setMessage("获取成功");
            returnVO.setData(myPage);
            return returnVO;
        }else{
            returnVO.setCode(500);
            returnVO.setMessage("获取学习圈错误");
            return  returnVO;
        }
    }
    @PostMapping("/issue")
    public ReturnVO issue(@RequestParam("data")String data){
        returnVO = new ReturnVO();
        if (data==null||data.equals("")){
            returnVO.setCode(500);
            returnVO.setMessage("数据不能为空");
            return returnVO;
        }else {
            Studycircle studycircle = (Studycircle) jsonUtil.parseBase64ToJsonStrThenToJavaObject(data,Studycircle.class);
            if (studycircle!=null){
                studycircle.setCreatetime(timeUtil.getCurrentTimeLocalDateTime());
                studycircle.setDeleted(0);
                boolean result = studycircleService.save(studycircle);
                if (result){
                    Score score = scoreService.getOne(new QueryWrapper<Score>().eq("uid",studycircle.getUid()));
                    if (score!=null){
                        Scoresourcetype scoresourcetype = scoresourcetypeService.getById(7);
                        if (scoresourcetype!=null){
                            score.setScore(score.getScore()+scoresourcetype.getDefaults());
                            boolean result2 =  scoreService.updateById(score);
                            if (result2){
                                Scoresource scoresource = new Scoresource();
                                scoresource.setSourceid(7);
                                scoresource.setNum(Long.valueOf(scoresourcetype.getDefaults()));
                                scoresource.setDeleted(0);
                                scoresource.setUid(studycircle.getUid());
                                scoresource.setCreatetime(timeUtil.getCurrentTimeLocalDateTime());
                                boolean result3= scoresourceService.save(scoresource);
                                if (result3){
                                    returnVO.setCode(200);
                                    returnVO.setMessage("发布成功");
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
                    returnVO.setMessage("存储发布失败");
                    return returnVO;
                }
            }else{
                returnVO.setCode(500);
                returnVO.setMessage("数据格式错误");
                return returnVO;
            }

        }
    }
}
