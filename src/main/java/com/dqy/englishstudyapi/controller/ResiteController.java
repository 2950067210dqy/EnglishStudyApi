package com.dqy.englishstudyapi.controller;


import com.dqy.englishstudyapi.entity.endEntity.ReviewEnd;
import com.dqy.englishstudyapi.entity.endEntity.WordSimpleEnd;
import com.dqy.englishstudyapi.entity.frontEntity.FrontReview;
import com.dqy.englishstudyapi.entity.frontEntity.FrontReviewFull;
import com.dqy.englishstudyapi.service.FreshwordService;
import com.dqy.englishstudyapi.service.NowresiteService;
import com.dqy.englishstudyapi.service.ResiteService;
import com.dqy.englishstudyapi.tablebean.Freshword;
import com.dqy.englishstudyapi.tablebean.Nowresite;
import com.dqy.englishstudyapi.tablebean.Resite;
import com.dqy.englishstudyapi.util.Base64Util;
import com.dqy.englishstudyapi.util.JsonUtil;
import com.dqy.englishstudyapi.util.TimeUtil;
import com.dqy.englishstudyapi.vo.ReturnVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 邓亲优
 * @since 2023-02-13
 */
@RestController
@RequestMapping("recite")
public class ResiteController {
    @Autowired
    ResiteService resiteService;
    @Autowired
    FreshwordService freshwordService;
    @Autowired
    NowresiteService nowresiteService;
    @Autowired
    Base64Util base64Util;
    @Autowired
    JsonUtil jsonUtil;
    @Autowired
    TimeUtil timeUtil;
    ReturnVO returnVO;

    @RequestMapping(value = "/reviewOne",method = RequestMethod.POST)
    public ReturnVO reviewOne(@RequestBody(required = false) FrontReviewFull datas)
    {
        ArrayList<FrontReview> vagues= (ArrayList<FrontReview>) datas.getVagues();
        ArrayList<FrontReview> knows= (ArrayList<FrontReview>)datas.getKnows();
        ArrayList<FrontReview> forgets =(ArrayList<FrontReview>) datas.getForgets();
        Integer reciteid =datas.getReciteid();
        returnVO = new ReturnVO();
        if (reciteid==null||(vagues==null&&forgets==null&&knows==null)){
            returnVO.setCode(500);
            returnVO.setMessage("数据不为空");
            return  returnVO ;
        }else{
            Resite resite =  resiteService.getById(reciteid);
            if (resite!=null){
                if (datas.getType()==0){
                    //词库
                    ArrayList<Integer> vaguesindexs = new ArrayList<>();
                    ArrayList<Integer> knowsindexs = new ArrayList<>();
                    for (int i = 0; i <forgets.size() ; i++) {

                        for (int j = 0; j < vagues.size(); j++) {
                            if (forgets.get(i).getId()==vagues.get(j).getId()){
//                            vaguesindexs.add(j);
                                vagues.remove(vagues.get(j));
                            }

                        }
                        for (int j = 0; j < knows.size() ; j++) {
                            if (forgets.get(i).getId()==knows.get(j).getId()){
//                            knowsindexs.add(j);
                                knows.remove(knows.get(j));
                            }
                        }
                    }
//                for (Integer i:vaguesindexs
//                     ) {
//                    vagues.remove(vagues.get(i));
//                }
//                for (Integer i:knowsindexs){
//                    knows.remove(knows.get(i));
//                }
                    knowsindexs = new ArrayList<>();
                    for (int i = 0; i <vagues.size() ; i++) {

                        for (int j = 0; j < knows.size() ; j++) {
                            if (vagues.get(i).getId()==knows.get(j).getId()){
//                            knowsindexs.add(j);
                                knows.remove(knows.get(j));
                            }

                        }
                    }
//                for (Integer i:knowsindexs){
//                    knows.remove(knows.get(i));
//                }
                    for (FrontReview frontReview:knows
                    ) {
                        switch (frontReview.getType()){
                            case "1": {
                                resite =deleteReviewById(resite,"1",frontReview.getId());
                                resite =insertReviewById(resite,"2",frontReview.getId());
                                break;
                            }
                            case "2":{
                                resite =deleteReviewById(resite,"2",frontReview.getId());
                                resite =insertReviewById(resite,"4",frontReview.getId());
                                break;
                            }
                            case "4":{
                                resite = deleteReviewById(resite,"4",frontReview.getId());
                                resite =insertReviewById(resite,"7",frontReview.getId());
                                break;
                            }
                            case "7":{
                                resite =deleteReviewById(resite,"7",frontReview.getId());
                                resite =insertReviewById(resite,"15",frontReview.getId());
                                break;
                            }
                            case "15":{
                                resite =deleteReviewById(resite,"15",frontReview.getId());
                                resite =insertReviewById(resite,"over",frontReview.getId());
                                break;
                            }
                            default:break;
                        }
                    }
                    for (FrontReview frontReview:vagues
                    ) {
                        switch (frontReview.getType()){
                            case "1":
                            {
                                resite =deleteReviewById(resite,"1",frontReview.getId());
                                resite =insertReviewById(resite,"1",frontReview.getId());
                                break;
                            }

                            case "2":
                            {
                                resite =deleteReviewById(resite,"2",frontReview.getId());
                                resite = insertReviewById(resite,"2",frontReview.getId());
                                break;
                            }
                            case "4":
                            {
                                resite =deleteReviewById(resite,"4",frontReview.getId());
                                resite =insertReviewById(resite,"4",frontReview.getId());
                                break;
                            }
                            case "7":
                            {
                                resite =deleteReviewById(resite,"7",frontReview.getId());
                                resite =insertReviewById(resite,"7",frontReview.getId());
                                break;
                            }
                            case "15":
                            {
                                resite =deleteReviewById(resite,"15",frontReview.getId());
                                resite =insertReviewById(resite,"15",frontReview.getId());
                                break;
                            }
                            default:break;
                        }
                    }

                    for (FrontReview frontReview:forgets
                    ) {
                        switch (frontReview.getType()){
                            case "1":
                            {
                                resite =deleteReviewById(resite,"1",frontReview.getId());
                                resite =insertReviewById(resite,"1",frontReview.getId());
                                break;
                            }
                            case "2":
                            {
                                resite =deleteReviewById(resite,"2",frontReview.getId());
                                resite =insertReviewById(resite,"1",frontReview.getId());
                                break;
                            }
                            case "4":
                            {
                                resite =deleteReviewById(resite,"4",frontReview.getId());
                                resite = insertReviewById(resite,"1",frontReview.getId());
                                break;
                            }
                            case "7":
                            {
                                resite =deleteReviewById(resite,"7",frontReview.getId());
                                resite =insertReviewById(resite,"1",frontReview.getId());
                                break;
                            }
                            case "15":
                            {
                                resite =deleteReviewById(resite,"15",frontReview.getId());
                                resite =insertReviewById(resite,"1",frontReview.getId());
                                break;
                            }
                            default:break;
                        }
                    }

                    resite.setUpdatetime(null);
                    boolean result = resiteService.updateById(resite);
                    if (result){
                        returnVO.setCode(200);
                        returnVO.setMessage("成功");
                        return returnVO;
                    }else{
                        returnVO.setCode(500);
                        returnVO.setMessage("更新失败");
                        return returnVO;
                    }
                }else{
                    //生词本
                    for (int i = 0; i <forgets.size() ; i++) {

                        for (int j = 0; j < vagues.size(); j++) {
                            if (forgets.get(i).getWid()==vagues.get(j).getWid()&&forgets.get(i).getInitial().equals(vagues.get(j).getInitial())){
                                vagues.remove(vagues.get(j));
                            }

                        }
                        for (int j = 0; j < knows.size() ; j++) {
                            if (forgets.get(i).getWid()==knows.get(j).getWid()&&forgets.get(i).getInitial().equals(knows.get(j).getInitial())){
                                knows.remove(knows.get(j));
                            }
                        }
                    }


                    for (int i = 0; i <vagues.size() ; i++) {

                        for (int j = 0; j < knows.size() ; j++) {
                            if (vagues.get(i).getWid()==knows.get(j).getWid()&&vagues.get(i).getInitial().equals(knows.get(j).getInitial())){
                                knows.remove(knows.get(j));
                            }

                        }
                    }

                    for (FrontReview frontReview:knows
                    ) {
                        switch (frontReview.getType()){
                            case "1": {
                                resite =deleteReviewById(resite,"1",frontReview.getWid(),frontReview.getInitial());
                                resite =insertReviewById(resite,"2",frontReview.getWid(),frontReview.getInitial());
                                break;
                            }
                            case "2":{
                                resite =deleteReviewById(resite,"2",frontReview.getWid(),frontReview.getInitial());
                                resite =insertReviewById(resite,"4",frontReview.getWid(),frontReview.getInitial());
                                break;
                            }
                            case "4":{
                                resite = deleteReviewById(resite,"4",frontReview.getWid(),frontReview.getInitial());
                                resite =insertReviewById(resite,"7",frontReview.getWid(),frontReview.getInitial());
                                break;
                            }
                            case "7":{
                                resite =deleteReviewById(resite,"7",frontReview.getWid(),frontReview.getInitial());
                                resite =insertReviewById(resite,"15",frontReview.getWid(),frontReview.getInitial());
                                break;
                            }
                            case "15":{
                                resite =deleteReviewById(resite,"15",frontReview.getWid(),frontReview.getInitial());
                                resite =insertReviewById(resite,"over",frontReview.getWid(),frontReview.getInitial());
                                Nowresite nowresite = nowresiteService.getById(resite.getNowresiteid());
                                Freshword freshword = freshwordService.getById(nowresite.getCikutypeid()) ;
                                ArrayList<WordSimpleEnd> wordSimpleEnds = jsonUtil.parseBase64ToJsonStrThenToJavaArrayList(freshword.getWords(),WordSimpleEnd.class);
                               Iterator<WordSimpleEnd> wordSimpleEndIterator =wordSimpleEnds.iterator();
                               while (wordSimpleEndIterator.hasNext()){
                                   WordSimpleEnd wordSimpleEnd =wordSimpleEndIterator.next();
                                   if (Objects.equals(frontReview.getWid(),wordSimpleEnd.getId())&&frontReview.getInitial().equals(wordSimpleEnd.getInitial())){
                                       wordSimpleEndIterator.remove();
                                   }
                               }
                               freshword.setWords(jsonUtil.parseArrayListToJsonStrThenToBase64(wordSimpleEnds));
                                Boolean result = freshwordService.updateById(freshword);
                                break;
                            }
                            default:break;
                        }
                    }
                    for (FrontReview frontReview:vagues
                    ) {
                        switch (frontReview.getType()){
                            case "1":
                            {
                                resite =deleteReviewById(resite,"1",frontReview.getWid(),frontReview.getInitial());
                                resite =insertReviewById(resite,"1",frontReview.getWid(),frontReview.getInitial());
                                break;
                            }

                            case "2":
                            {
                                resite =deleteReviewById(resite,"2",frontReview.getWid(),frontReview.getInitial());
                                resite = insertReviewById(resite,"2",frontReview.getWid(),frontReview.getInitial());
                                break;
                            }
                            case "4":
                            {
                                resite =deleteReviewById(resite,"4",frontReview.getWid(),frontReview.getInitial());
                                resite =insertReviewById(resite,"4",frontReview.getWid(),frontReview.getInitial());
                                break;
                            }
                            case "7":
                            {
                                resite =deleteReviewById(resite,"7",frontReview.getWid(),frontReview.getInitial());
                                resite =insertReviewById(resite,"7",frontReview.getWid(),frontReview.getInitial());
                                break;
                            }
                            case "15":
                            {
                                resite =deleteReviewById(resite,"15",frontReview.getWid(),frontReview.getInitial());
                                resite =insertReviewById(resite,"15",frontReview.getWid(),frontReview.getInitial());
                                break;
                            }
                            default:break;
                        }
                    }

                    for (FrontReview frontReview:forgets
                    ) {
                        switch (frontReview.getType()){
                            case "1":
                            {
                                resite =deleteReviewById(resite,"1",frontReview.getWid(),frontReview.getInitial());
                                resite =insertReviewById(resite,"1",frontReview.getWid(),frontReview.getInitial());
                                break;
                            }
                            case "2":
                            {
                                resite =deleteReviewById(resite,"2",frontReview.getWid(),frontReview.getInitial());
                                resite =insertReviewById(resite,"1",frontReview.getWid(),frontReview.getInitial());
                                break;
                            }
                            case "4":
                            {
                                resite =deleteReviewById(resite,"4",frontReview.getWid(),frontReview.getInitial());
                                resite = insertReviewById(resite,"1",frontReview.getWid(),frontReview.getInitial());
                                break;
                            }
                            case "7":
                            {
                                resite =deleteReviewById(resite,"7",frontReview.getWid(),frontReview.getInitial());
                                resite =insertReviewById(resite,"1",frontReview.getWid(),frontReview.getInitial());
                                break;
                            }
                            case "15":
                            {
                                resite =deleteReviewById(resite,"15",frontReview.getWid(),frontReview.getInitial());
                                resite =insertReviewById(resite,"1",frontReview.getWid(),frontReview.getInitial());
                                break;
                            }
                            default:break;
                        }
                    }

                    resite.setUpdatetime(null);
                    boolean result = resiteService.updateById(resite);
                    if (result){
                        returnVO.setCode(200);
                        returnVO.setMessage("成功");
                        return returnVO;
                    }else{
                        returnVO.setCode(500);
                        returnVO.setMessage("更新失败");
                        return returnVO;
                    }
                }

            }else{
                returnVO.setCode(500);
                returnVO.setMessage("recite为空");
            }
        }
        return returnVO;
    }

    @RequestMapping(value = "/reciteOne",method = RequestMethod.POST)
    public ReturnVO reciteOne(
            @RequestParam(value = "cikuexampleid")Integer cikuexampleid, @RequestParam(value = "reciteid")Integer reciteid,@RequestParam(value = "type",defaultValue = "0")Integer type
            ,@RequestParam(value="wid") Integer wid,@RequestParam("initial")String initial
    )
    {
        returnVO = new ReturnVO();
        if (cikuexampleid==null||reciteid==null){
            returnVO.setCode(500);
            returnVO.setMessage("数据不为空");
            return  returnVO ;
        }else{
            Resite resite =  resiteService.getById(reciteid);
            if (resite!=null){
                String learn = resite.getLearn();
                if (!(learn.equals(""))){
                    if (type==0){
                        //词库
                        String[] cikuexampleids = base64Util.decodeToString(learn).split(",");
                        String nowLearn="";
                        for (int i = 0; i < cikuexampleids.length; i++) {
                            if (Integer.valueOf(cikuexampleids[i])==cikuexampleid){
                                continue;
                            }
                            if (i==cikuexampleids.length-1){
                                nowLearn+=(cikuexampleids[i]);
                            }else{
                                nowLearn+=(cikuexampleids[i]+",");
                            }
                        }
                        String nowLearnBase64 =base64Util.encodeToString(nowLearn);
                        resite.setLearn(nowLearnBase64);
                        resite = insertReviewById(resite,"1",cikuexampleid);

                        resite.setUpdatetime(null);
                        boolean result = resiteService.updateById(resite);
                        if (result){
                            returnVO.setCode(200);
                            returnVO.setMessage("背诵成功");
                        }else{
                            returnVO.setCode(500);
                            returnVO.setMessage("更新失败");
                        }
                    }else{
                        //生词本
                        ArrayList<WordSimpleEnd> wordSimpleEnds = jsonUtil.parseBase64ToJsonStrThenToJavaArrayList(learn,WordSimpleEnd.class);
                        Iterator<WordSimpleEnd> wordSimpleEndsIt = wordSimpleEnds.iterator();
                        while (wordSimpleEndsIt.hasNext()){
                            WordSimpleEnd wordSimpleEnd =wordSimpleEndsIt.next();
                            if (wordSimpleEnd.getInitial().equals(initial)&& Objects.equals(wordSimpleEnd.getId(),wid)){
                                wordSimpleEndsIt.remove();
                            }
                        }
                        String nowLearnBase64 =jsonUtil.parseArrayListToJsonStrThenToBase64(wordSimpleEnds);
                        resite.setLearn(nowLearnBase64);
                        resite = insertReviewById(resite,"1",wid,initial);

                        resite.setUpdatetime(null);
                        boolean result = resiteService.updateById(resite);
                        if (result){
                            returnVO.setCode(200);
                            returnVO.setMessage("背诵成功");
                        }else{
                            returnVO.setCode(500);
                            returnVO.setMessage("更新失败");
                        }
                    }

                }
            }else{
                returnVO.setCode(500);
                returnVO.setMessage("recite为空");
            }
        }
        return returnVO;
    }


    public Resite insertReviewById(Resite resite,String type,Integer id){
        String review = "";
        switch (type){
            case "1":
                review = resite.getReview1();
                break;
            case "2":
                review = resite.getReview2();
                break;
            case "4":
                review = resite.getReview4();
                break;
            case "7":
                review = resite.getReview7();
                break;
            case "15":
                review = resite.getReview15();
                break;
            case "over":
                review = resite.getOver();
                break;
            default:
                review = resite.getReview1();
                break;
        }
        ReviewEnd reviewEnd = new ReviewEnd();
        reviewEnd.setUpdatetime(timeUtil.getCurrentTimeLocalDateTime());
        reviewEnd.setId(id);
        ArrayList<ReviewEnd> reviewEnds=null;
        if (!("".equals(review))){
            reviewEnds = jsonUtil.parseBase64ToJsonStrThenToJavaArrayList(review,ReviewEnd.class);

        }else{
            reviewEnds=new ArrayList<>();
        }
        reviewEnds.add(reviewEnd);
        switch (type){
            case "1":
                resite.setReview1(jsonUtil.parseArrayListToJsonStrThenToBase64(reviewEnds));
                break;
            case "2":
                resite.setReview2(jsonUtil.parseArrayListToJsonStrThenToBase64(reviewEnds));
                break;
            case "4":
                resite.setReview4(jsonUtil.parseArrayListToJsonStrThenToBase64(reviewEnds));
                break;
            case "7":
                resite.setReview7(jsonUtil.parseArrayListToJsonStrThenToBase64(reviewEnds));
                break;
            case "15":
                resite.setReview15(jsonUtil.parseArrayListToJsonStrThenToBase64(reviewEnds));
                break;
            case "over":
                resite.setOver(jsonUtil.parseArrayListToJsonStrThenToBase64(reviewEnds));
                break;
            default:
                resite.setReview1(jsonUtil.parseArrayListToJsonStrThenToBase64(reviewEnds));
                break;
        }
       return  resite;
    }
    public Resite insertReviewById(Resite resite,String type,Integer id,String initial){
        String review = "";
        switch (type){
            case "1":
                review = resite.getReview1();
                break;
            case "2":
                review = resite.getReview2();
                break;
            case "4":
                review = resite.getReview4();
                break;
            case "7":
                review = resite.getReview7();
                break;
            case "15":
                review = resite.getReview15();
                break;
            case "over":
                review = resite.getOver();
                break;
            default:
                review = resite.getReview1();
                break;
        }
        ReviewEnd reviewEnd = new ReviewEnd();
        reviewEnd.setUpdatetime(timeUtil.getCurrentTimeLocalDateTime());
        reviewEnd.setId(id);
        reviewEnd.setInitial(initial);
        ArrayList<ReviewEnd> reviewEnds=null;
        if (!("".equals(review))){
            reviewEnds = jsonUtil.parseBase64ToJsonStrThenToJavaArrayList(review,ReviewEnd.class);

        }else{
            reviewEnds=new ArrayList<>();
        }
        reviewEnds.add(reviewEnd);
        switch (type){
            case "1":
                resite.setReview1(jsonUtil.parseArrayListToJsonStrThenToBase64(reviewEnds));
                break;
            case "2":
                resite.setReview2(jsonUtil.parseArrayListToJsonStrThenToBase64(reviewEnds));
                break;
            case "4":
                resite.setReview4(jsonUtil.parseArrayListToJsonStrThenToBase64(reviewEnds));
                break;
            case "7":
                resite.setReview7(jsonUtil.parseArrayListToJsonStrThenToBase64(reviewEnds));
                break;
            case "15":
                resite.setReview15(jsonUtil.parseArrayListToJsonStrThenToBase64(reviewEnds));
                break;
            case "over":
                resite.setOver(jsonUtil.parseArrayListToJsonStrThenToBase64(reviewEnds));
                break;
            default:
                resite.setReview1(jsonUtil.parseArrayListToJsonStrThenToBase64(reviewEnds));
                break;
        }
        return  resite;
    }

    public Resite deleteReviewById(Resite resite,String type,Integer id){
        String review = "";
        switch (type){
            case "1":
                review = resite.getReview1();
                break;
            case "2":
                review = resite.getReview2();
                break;
            case "4":
                review = resite.getReview4();
                break;
            case "7":
                review = resite.getReview7();
                break;
            case "15":
                review = resite.getReview15();
                break;
            case "over":
                review = resite.getOver();
                break;
            default:
                review = resite.getReview1();
                break;
        }
        ArrayList<ReviewEnd> reviewEnds=null;
        if (!("".equals(review))){
            reviewEnds = jsonUtil.parseBase64ToJsonStrThenToJavaArrayList(review,ReviewEnd.class);
            Iterator<ReviewEnd> iterator =reviewEnds.iterator();
            while (iterator.hasNext()){
                if (iterator.next().getId()==id){
                    iterator.remove();
                }
            }
        }
        if (reviewEnds!=null){
            switch (type){
                case "1":
                    resite.setReview1(jsonUtil.parseArrayListToJsonStrThenToBase64(reviewEnds));
                    break;
                case "2":
                    resite.setReview2(jsonUtil.parseArrayListToJsonStrThenToBase64(reviewEnds));
                    break;
                case "4":
                    resite.setReview4(jsonUtil.parseArrayListToJsonStrThenToBase64(reviewEnds));
                    break;
                case "7":
                    resite.setReview7(jsonUtil.parseArrayListToJsonStrThenToBase64(reviewEnds));
                    break;
                case "15":
                    resite.setReview15(jsonUtil.parseArrayListToJsonStrThenToBase64(reviewEnds));
                    break;
                case "over":
                    resite.setOver(jsonUtil.parseArrayListToJsonStrThenToBase64(reviewEnds));
                    break;
                default:
                    resite.setReview1(jsonUtil.parseArrayListToJsonStrThenToBase64(reviewEnds));
                    break;
            }
        }

    return resite;


    }

    public Resite deleteReviewById(Resite resite,String type,Integer id,String initial){
        String review = "";
        switch (type){
            case "1":
                review = resite.getReview1();
                break;
            case "2":
                review = resite.getReview2();
                break;
            case "4":
                review = resite.getReview4();
                break;
            case "7":
                review = resite.getReview7();
                break;
            case "15":
                review = resite.getReview15();
                break;
            case "over":
                review = resite.getOver();
                break;
            default:
                review = resite.getReview1();
                break;
        }
        ArrayList<ReviewEnd> reviewEnds=null;
        if (!("".equals(review))){
            reviewEnds = jsonUtil.parseBase64ToJsonStrThenToJavaArrayList(review,ReviewEnd.class);
            Iterator<ReviewEnd> iterator =reviewEnds.iterator();
            while (iterator.hasNext()){
                ReviewEnd reviewEnd = iterator.next();
                if (Objects.equals(reviewEnd.getId(),id)&&reviewEnd.getInitial().equals(initial)){
                    iterator.remove();
                }
            }
        }
        if (reviewEnds!=null){
            switch (type){
                case "1":
                    resite.setReview1(jsonUtil.parseArrayListToJsonStrThenToBase64(reviewEnds));
                    break;
                case "2":
                    resite.setReview2(jsonUtil.parseArrayListToJsonStrThenToBase64(reviewEnds));
                    break;
                case "4":
                    resite.setReview4(jsonUtil.parseArrayListToJsonStrThenToBase64(reviewEnds));
                    break;
                case "7":
                    resite.setReview7(jsonUtil.parseArrayListToJsonStrThenToBase64(reviewEnds));
                    break;
                case "15":
                    resite.setReview15(jsonUtil.parseArrayListToJsonStrThenToBase64(reviewEnds));
                    break;
                case "over":
                    resite.setOver(jsonUtil.parseArrayListToJsonStrThenToBase64(reviewEnds));
                    break;
                default:
                    resite.setReview1(jsonUtil.parseArrayListToJsonStrThenToBase64(reviewEnds));
                    break;
            }
        }

        return resite;


    }
}
