package com.dqy.englishstudyapi.controller.admin.administer;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dqy.englishstudyapi.entity.adminEntity.administer.FrontProducts;
import com.dqy.englishstudyapi.entity.adminEntity.condition.ProductCondition;
import com.dqy.englishstudyapi.entity.adminEntity.condition.StudyCircleCondition;
import com.dqy.englishstudyapi.entity.frontEntity.StudyCircleFull.StudyCircleComment;
import com.dqy.englishstudyapi.entity.frontEntity.StudyCircleFull.StudyCircleFull;
import com.dqy.englishstudyapi.entity.frontEntity.StudyCircleFull.StudyCircleSubComment;
import com.dqy.englishstudyapi.entity.page.MyPage;
import com.dqy.englishstudyapi.service.*;
import com.dqy.englishstudyapi.tablebean.*;
import com.dqy.englishstudyapi.util.*;
import com.dqy.englishstudyapi.vo.ReturnVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

@RestController
@RequestMapping("adminStudyCircle")
public class AdminStudyCircleController {
    @Autowired
    StudycircleService studycircleService;
    @Autowired
    TagsService tagsService;
    @Autowired
    UserService userService;
    @Autowired
    TimeUtil timeUtil;
    @Autowired
    Base64Util base64Util;
    @Autowired
    JsonUtil jsonUtil;
    @Autowired
    WordUtil wordUtil;
    @Autowired
    DynamicTableNameUtil dynamicTableNameUtil;

    ReturnVO returnVO;
    @PostMapping("/deleteStudyCircleSingle")
    public ReturnVO deleteStudyCircleSingle(@RequestParam("id")Integer id){
        returnVO = new ReturnVO();
        if (id==null){
            returnVO.setMessage("参数为空");
            returnVO.setCode(500);
            return  returnVO;
        }

        boolean result = studycircleService.removeById(id);
        if (result){
            returnVO.setMessage("删除成功");
            returnVO.setCode(200);
            return  returnVO;
        }else{
            returnVO.setMessage("删除失败");
            returnVO.setCode(500);
            return  returnVO;
        }

    }
    @PostMapping("/deleteStudyCircleBatch")
    public ReturnVO deleteStudyCircleBatch(@RequestParam("ids") List<Integer> ids){
        returnVO = new ReturnVO();
        if (ids==null||ids.size()==0){
            returnVO.setMessage("参数为空");
            returnVO.setCode(500);
            return  returnVO;
        }

        boolean result = studycircleService.removeBatchByIds(ids);
        if (result){
            returnVO.setMessage("删除成功");
            returnVO.setCode(200);
            return  returnVO;
        }else{
            returnVO.setMessage("删除失败");
            returnVO.setCode(500);
            return  returnVO;
        }
    }
    @PostMapping("/getTags")
    public  ReturnVO getTags(){
        returnVO = new ReturnVO();

        List<Tags> tags = tagsService.list();
        if (tags!=null&&tags.size()!=0){
            returnVO.setCode(200);
            returnVO.setMessage("获取标签成功");
            returnVO.setData(tags);
            return  returnVO;
        } else{
            returnVO.setCode(500);
            returnVO.setMessage("获取标签错误或空");
            return  returnVO;
        }

    }
    @PostMapping("/getStudyCircle")
    public  ReturnVO getStudyCircle(@RequestBody StudyCircleCondition condition){
        returnVO = new ReturnVO();
        if (condition==null){
            returnVO.setCode(500);
            returnVO.setMessage("数据为空");
            return returnVO;
        }

        Page<Studycircle> page = new Page<>();
        page.setCurrent(condition.getCurrent());
        page.setSize(condition.getSize());

        IPage<Studycircle> iPage = studycircleService.page(page,getConditionWrapper(new QueryWrapper<Studycircle>(),condition));
        ArrayList<StudyCircleFull> studyCircleFulls = null;
        if (iPage.getRecords()!=null){
            List<Studycircle> studycircles =iPage.getRecords().size()==0?new ArrayList<>(): (ArrayList<Studycircle>) iPage.getRecords();
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

                User user = userService.getById(s.getUid());
                studyCircleFull.setUser(user);

                studyCircleFull.setComments(null);
                studyCircleFull.setIslike(null);
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

    private QueryWrapper<Studycircle> getConditionWrapper(QueryWrapper<Studycircle> wrapper, StudyCircleCondition condition) {
        if (wrapper==null){
            wrapper = new QueryWrapper<>();
        }

        if (condition.getTitleSearch()!=null){
            wrapper = wrapper.like("title",condition.getTitleSearch());
        }

        if (condition.getUserSelect()!=null){
            Consumer<QueryWrapper<Studycircle>> consumer = new Consumer<QueryWrapper<Studycircle>>() {
                @Override
                public void accept(QueryWrapper<Studycircle> wrapper1) {
                    for (int i = 0; i < condition.getUserSelect().size(); i++) {
                        if (i!=condition.getUserSelect().size()-1){
                            wrapper1 = wrapper1.eq("uid",condition.getUserSelect().get(i)).or();
                        }else{
                            wrapper1 = wrapper1.eq("uid",condition.getUserSelect().get(i));
                        }
                    }
                }
            };
            wrapper.and(consumer);
        }
        if (condition.getTagsSelect()!=null){
            Consumer<QueryWrapper<Studycircle>> consumer = new Consumer<QueryWrapper<Studycircle>>() {
                @Override
                public void accept(QueryWrapper<Studycircle> wrapper1) {
                    for (int i = 0; i < condition.getTagsSelect().size(); i++) {
                        if (i!=condition.getTagsSelect().size()-1){
                            wrapper1 = wrapper1.likeRight("uid",condition.getTagsSelect().get(i)).or();
                        }else{
                            wrapper1 = wrapper1.likeRight("uid",condition.getTagsSelect().get(i));
                        }
                    }
                }
            };
            wrapper.and(consumer);
        }
        if (condition.getOrderbyAsc()!=null&&condition.getOrderbyAsc().size()!=0){
            wrapper= wrapper.orderByAsc(condition.getOrderbyAsc());
        }
        List<String> orderbydesc = condition.getOrderbyDesc();
        orderbydesc.add("updatetime");
        condition.setOrderbyDesc(orderbydesc);
        if (condition.getOrderbyDesc()!=null&&condition.getOrderbyDesc().size()!=0){
            wrapper= wrapper.orderByDesc(condition.getOrderbyDesc());
        }
        return  wrapper;
    }
}
