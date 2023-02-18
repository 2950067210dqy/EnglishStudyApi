package com.dqy.englishstudyapi.controller;


import com.dqy.englishstudyapi.entity.frontEntity.FrontReview;
import com.dqy.englishstudyapi.entity.frontEntity.FrontReviewFull;
import com.dqy.englishstudyapi.service.ResiteService;
import com.dqy.englishstudyapi.tablebean.Resite;
import com.dqy.englishstudyapi.util.Base64Util;
import com.dqy.englishstudyapi.vo.ReturnVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 邓亲优
 * @since 2023-02-13
 */
@RestController
@RequestMapping("recite2")
public class ResiteController2 {
    @Autowired
    ResiteService resiteService;
    @Autowired
    Base64Util base64Util;
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
                            String review1 =resite.getReview1();
                            String newReview1s="";
                            if (!("".equals(review1))){
                                review1=base64Util.decodeToString(review1);
                                String[] review1s= review1.split(",");
                                for (int i = 0; i < review1s.length; i++) {

                                    if (Integer.valueOf(review1s[i])==frontReview.getId()){
                                        continue;
                                    }
                                    if (i==review1s.length-1){
                                        newReview1s=newReview1s+review1s[i];
                                    }else{
                                        newReview1s=newReview1s+review1s[i]+",";
                                    }
                                }
                            }else{
                                newReview1s=newReview1s+frontReview.getId();
                            }
                            resite.setReview1(base64Util.encodeToString(newReview1s));

                            String review2 =resite.getReview2();
                            String newReview2s="";
                            if (!("".equals(review2))){
                                review2=base64Util.decodeToString(review2);
                                newReview2s=review2+","+frontReview.getId();
                            }else{
                                newReview2s=newReview2s+frontReview.getId();
                            }
                            resite.setReview2(base64Util.encodeToString(newReview2s));
                            break;
                        }
                        case "2":{
                            String review2 =resite.getReview1();
                            String newReview2s="";
                            if (!("".equals(review2))){
                                review2=base64Util.decodeToString(review2);
                                String[] review2s= review2.split(",");
                                for (int i = 0; i < review2s.length; i++) {

                                    if (Integer.valueOf(review2s[i])==frontReview.getId()){
                                        continue;
                                    }
                                    if (i==review2s.length-1){
                                        newReview2s=newReview2s+review2s[i];
                                    }else{
                                        newReview2s=newReview2s+review2s[i]+",";
                                    }
                                }
                            }else{
                                newReview2s=newReview2s+frontReview.getId();
                            }
                            resite.setReview2(base64Util.encodeToString(newReview2s));

                            String review4 =resite.getReview4();
                            String newReview4s="";
                            if (!("".equals(review4))){
                                review4=base64Util.decodeToString(review4);
                                newReview4s=review4+","+frontReview.getId();
                            }else{
                                newReview4s=newReview4s+frontReview.getId();
                            }
                            resite.setReview4(base64Util.encodeToString(newReview4s));
                            break;
                        }
                        case "4":{
                            String review4 =resite.getReview4();
                            String newReview4s="";
                            if (!("".equals(review4))){
                                review4=base64Util.decodeToString(review4);
                                String[] review4s= review4.split(",");
                                for (int i = 0; i < review4s.length; i++) {

                                    if (Integer.valueOf(review4s[i])==frontReview.getId()){
                                        continue;
                                    }
                                    if (i==review4s.length-1){
                                        newReview4s=newReview4s+review4s[i];
                                    }else{
                                        newReview4s=newReview4s+review4s[i]+",";
                                    }
                                }
                            }else{
                                newReview4s=newReview4s+frontReview.getId();
                            }
                            resite.setReview4(base64Util.encodeToString(newReview4s));

                            String review7 =resite.getReview7();
                            String newReview7s="";
                            if (!("".equals(review7))){
                                review7=base64Util.decodeToString(review7);
                                newReview7s=review7+","+frontReview.getId();
                            }else{
                                newReview7s=newReview7s+frontReview.getId();
                            }
                            resite.setReview7(base64Util.encodeToString(newReview7s));
                            break;
                        }
                        case "7":{
                            String review7 =resite.getReview7();
                            String newReview7s="";
                            if (!("".equals(review7))){
                                review7=base64Util.decodeToString(review7);
                                String[] review7s= review7.split(",");
                                for (int i = 0; i < review7s.length; i++) {

                                    if (Integer.valueOf(review7s[i])==frontReview.getId()){
                                        continue;
                                    }
                                    if (i==review7s.length-1){
                                        newReview7s=newReview7s+review7s[i];
                                    }else{
                                        newReview7s=newReview7s+review7s[i]+",";
                                    }
                                }
                            }else{
                                newReview7s=newReview7s+frontReview.getId();
                            }
                            resite.setReview7(base64Util.encodeToString(newReview7s));

                            String review15 =resite.getReview15();
                            String newReview15s="";
                            if (!("".equals(review15))){
                                review15=base64Util.decodeToString(review15);
                                newReview15s=review15+","+frontReview.getId();
                            }else{
                                newReview15s=newReview15s+frontReview.getId();
                            }
                            resite.setReview15(base64Util.encodeToString(newReview15s));
                            break;
                        }
                        case "15":{
                            String review15 =resite.getReview15();
                            String newReview15s="";
                            if (!("".equals(review15))){
                                review15=base64Util.decodeToString(review15);
                                String[] review15s= review15.split(",");
                                for (int i = 0; i < review15s.length; i++) {

                                    if (Integer.valueOf(review15s[i])==frontReview.getId()){
                                        continue;
                                    }
                                    if (i==review15s.length-1){
                                        newReview15s=newReview15s+review15s[i];
                                    }else{
                                        newReview15s=newReview15s+review15s[i]+",";
                                    }
                                }
                            }else{
                                newReview15s=newReview15s+frontReview.getId();
                            }
                            resite.setReview15(base64Util.encodeToString(newReview15s));

                            String reviewover =resite.getOver();
                            String newReviewovers="";
                            if (!("".equals(reviewover))){
                                reviewover=base64Util.decodeToString(reviewover);
                                newReviewovers=reviewover+","+frontReview.getId();
                            }else{
                                newReviewovers=newReviewovers+frontReview.getId();
                            }
                            resite.setOver(base64Util.encodeToString(newReviewovers));
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
                            String review1 =resite.getReview1();
                            String newReview1s="";
                            if (!("".equals(review1))){
                                review1=base64Util.decodeToString(review1);
                                String[] review1s= review1.split(",");
                                for (int i = 0; i < review1s.length; i++) {

                                    if (Integer.valueOf(review1s[i])==frontReview.getId()){
                                        continue;
                                    }
                                    if (i==review1s.length-1){
                                        newReview1s=newReview1s+review1s[i];
                                    }else{
                                        newReview1s=newReview1s+review1s[i]+",";
                                    }
                                }
                            }else{
                                newReview1s=newReview1s+frontReview.getId();
                            }
                            resite.setReview1(base64Util.encodeToString(newReview1s));

                            review1 =resite.getReview1();
                            newReview1s="";
                            if (!("".equals(review1))){
                                review1=base64Util.decodeToString(review1);
                                newReview1s=review1+","+frontReview.getId();
                            }else{
                                newReview1s=newReview1s+frontReview.getId();
                            }
                            resite.setReview1(base64Util.encodeToString(newReview1s));
                            break;
                        }

                        case "2":
                        {
                            String review2 =resite.getReview2();
                            String newReview2s="";
                            if (!("".equals(review2))){
                                review2=base64Util.decodeToString(review2);
                                String[] review2s= review2.split(",");
                                for (int i = 0; i < review2s.length; i++) {

                                    if (Integer.valueOf(review2s[i])==frontReview.getId()){
                                        continue;
                                    }
                                    if (i==review2s.length-1){
                                        newReview2s=newReview2s+review2s[i];
                                    }else{
                                        newReview2s=newReview2s+review2s[i]+",";
                                    }
                                }
                            }else{
                                newReview2s=newReview2s+frontReview.getId();
                            }
                            resite.setReview2(base64Util.encodeToString(newReview2s));

                            review2 =resite.getReview2();
                            newReview2s="";
                            if (!("".equals(review2))){
                                review2=base64Util.decodeToString(review2);
                                newReview2s=review2+","+frontReview.getId();
                            }else{
                                newReview2s=newReview2s+frontReview.getId();
                            }
                            resite.setReview2(base64Util.encodeToString(newReview2s));
                            break;
                        }
                        case "4":
                        {
                            String review4 =resite.getReview4();
                            String newReview4s="";
                            if (!("".equals(review4))){
                                review4=base64Util.decodeToString(review4);
                                String[] review4s= review4.split(",");
                                for (int i = 0; i < review4s.length; i++) {

                                    if (Integer.valueOf(review4s[i])==frontReview.getId()){
                                        continue;
                                    }
                                    if (i==review4s.length-1){
                                        newReview4s=newReview4s+review4s[i];
                                    }else{
                                        newReview4s=newReview4s+review4s[i]+",";
                                    }
                                }
                            }else{
                                newReview4s=newReview4s+frontReview.getId();
                            }
                            resite.setReview4(base64Util.encodeToString(newReview4s));

                            review4 =resite.getReview4();
                            newReview4s="";
                            if (!("".equals(review4))){
                                review4=base64Util.decodeToString(review4);
                                newReview4s=review4+","+frontReview.getId();
                            }else{
                                newReview4s=newReview4s+frontReview.getId();
                            }
                            resite.setReview4(base64Util.encodeToString(newReview4s));
                            break;
                        }
                        case "7":
                        {
                            String review7 =resite.getReview7();
                            String newReview7s="";
                            if (!("".equals(review7))){
                                review7=base64Util.decodeToString(review7);
                                String[] review7s= review7.split(",");
                                for (int i = 0; i < review7s.length; i++) {

                                    if (Integer.valueOf(review7s[i])==frontReview.getId()){
                                        continue;
                                    }
                                    if (i==review7s.length-1){
                                        newReview7s=newReview7s+review7s[i];
                                    }else{
                                        newReview7s=newReview7s+review7s[i]+",";
                                    }
                                }
                            }else{
                                newReview7s=newReview7s+frontReview.getId();
                            }
                            resite.setReview7(base64Util.encodeToString(newReview7s));

                            review7 =resite.getReview7();
                            newReview7s="";
                            if (!("".equals(review7))){
                                review7=base64Util.decodeToString(review7);
                                newReview7s=review7+","+frontReview.getId();
                            }else{
                                newReview7s=newReview7s+frontReview.getId();
                            }
                            resite.setReview7(base64Util.encodeToString(newReview7s));
                            break;
                        }
                        case "15":
                        {
                            String review15 =resite.getReview15();
                            String newReview15s="";
                            if (!("".equals(review15))){
                                review15=base64Util.decodeToString(review15);
                                String[] review15s= review15.split(",");
                                for (int i = 0; i < review15s.length; i++) {

                                    if (Integer.valueOf(review15s[i])==frontReview.getId()){
                                        continue;
                                    }
                                    if (i==review15s.length-1){
                                        newReview15s=newReview15s+review15s[i];
                                    }else{
                                        newReview15s=newReview15s+review15s[i]+",";
                                    }
                                }
                            }else{
                                newReview15s=newReview15s+frontReview.getId();
                            }
                            resite.setReview15(base64Util.encodeToString(newReview15s));

                            review15 =resite.getReview15();
                            newReview15s="";
                            if (!("".equals(review15))){
                                review15=base64Util.decodeToString(review15);
                                newReview15s=review15+","+frontReview.getId();
                            }else{
                                newReview15s=newReview15s+frontReview.getId();
                            }
                            resite.setReview15(base64Util.encodeToString(newReview15s));
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
                            String review1 =resite.getReview1();
                            String newReview1s="";
                            if (!("".equals(review1))){
                                review1=base64Util.decodeToString(review1);
                                String[] review1s= review1.split(",");
                                for (int i = 0; i < review1s.length; i++) {

                                    if (Integer.valueOf(review1s[i])==frontReview.getId()){
                                        continue;
                                    }
                                    if (i==review1s.length-1){
                                        newReview1s=newReview1s+review1s[i];
                                    }else{
                                        newReview1s=newReview1s+review1s[i]+",";
                                    }
                                }
                            }else{
                                newReview1s=newReview1s+frontReview.getId();
                            }
                            resite.setReview1(base64Util.encodeToString(newReview1s));

                            review1 =resite.getReview1();
                            newReview1s="";
                            if (!("".equals(review1))){
                                review1=base64Util.decodeToString(review1);
                                newReview1s=review1+","+frontReview.getId();
                            }else{
                                newReview1s=newReview1s+frontReview.getId();
                            }
                            resite.setReview1(base64Util.encodeToString(newReview1s));
                            break;
                        }
                        case "2":
                        {
                            String review2 =resite.getReview2();
                            String newReview2s="";
                            if (!("".equals(review2))){
                                review2=base64Util.decodeToString(review2);
                                String[] review2s= review2.split(",");
                                for (int i = 0; i < review2s.length; i++) {

                                    if (Integer.valueOf(review2s[i])==frontReview.getId()){
                                        continue;
                                    }
                                    if (i==review2s.length-1){
                                        newReview2s=newReview2s+review2s[i];
                                    }else{
                                        newReview2s=newReview2s+review2s[i]+",";
                                    }
                                }
                            }else{
                                newReview2s=newReview2s+frontReview.getId();
                            }
                            resite.setReview2(base64Util.encodeToString(newReview2s));

                            String review1 =resite.getReview1();
                            String newReview1s="";
                            if (!("".equals(review1))){
                                review1=base64Util.decodeToString(review1);
                                newReview1s=review1+","+frontReview.getId();
                            }else{
                                newReview1s=newReview1s+frontReview.getId();
                            }
                            resite.setReview1(base64Util.encodeToString(newReview1s));
                            break;
                        }
                        case "4":
                        {
                            String review4 =resite.getReview4();
                            String newReview4s="";
                            if (!("".equals(review4))){
                                review4=base64Util.decodeToString(review4);
                                String[] review2s= review4.split(",");
                                for (int i = 0; i < review2s.length; i++) {

                                    if (Integer.valueOf(review2s[i])==frontReview.getId()){
                                        continue;
                                    }
                                    if (i==review2s.length-1){
                                        newReview4s=newReview4s+review2s[i];
                                    }else{
                                        newReview4s=newReview4s+review2s[i]+",";
                                    }
                                }
                            }else{
                                newReview4s=newReview4s+frontReview.getId();
                            }
                            resite.setReview4(base64Util.encodeToString(newReview4s));

                            String review1 =resite.getReview1();
                            String newReview1s="";
                            if (!("".equals(review1))){
                                review1=base64Util.decodeToString(review1);
                                newReview1s=review1+","+frontReview.getId();
                            }else{
                                newReview1s=newReview1s+frontReview.getId();
                            }
                            resite.setReview1(base64Util.encodeToString(newReview1s));
                            break;
                        }
                        case "7":
                        {
                            String review7 =resite.getReview7();
                            String newReview7s="";
                            if (!("".equals(review7))){
                                review7=base64Util.decodeToString(review7);
                                String[] review7s= review7.split(",");
                                for (int i = 0; i < review7s.length; i++) {

                                    if (Integer.valueOf(review7s[i])==frontReview.getId()){
                                        continue;
                                    }
                                    if (i==review7s.length-1){
                                        newReview7s=newReview7s+review7s[i];
                                    }else{
                                        newReview7s=newReview7s+review7s[i]+",";
                                    }
                                }
                            }else{
                                newReview7s=newReview7s+frontReview.getId();
                            }
                            resite.setReview7(base64Util.encodeToString(newReview7s));

                            String review1 =resite.getReview1();
                            String newReview1s="";
                            if (!("".equals(review1))){
                                review1=base64Util.decodeToString(review1);
                                newReview1s=review1+","+frontReview.getId();
                            }else{
                                newReview1s=newReview1s+frontReview.getId();
                            }
                            resite.setReview1(base64Util.encodeToString(newReview1s));
                            break;
                        }
                        case "15":
                        {
                            String review15 =resite.getReview15();
                            String newReview15s="";
                            if (!("".equals(review15))){
                                review15=base64Util.decodeToString(review15);
                                String[] review15s= review15.split(",");
                                for (int i = 0; i < review15s.length; i++) {

                                    if (Integer.valueOf(review15s[i])==frontReview.getId()){
                                        continue;
                                    }
                                    if (i==review15s.length-1){
                                        newReview15s=newReview15s+review15s[i];
                                    }else{
                                        newReview15s=newReview15s+review15s[i]+",";
                                    }
                                }
                            }else{
                                newReview15s=newReview15s+frontReview.getId();
                            }
                            resite.setReview15(base64Util.encodeToString(newReview15s));

                            String review1 =resite.getReview1();
                            String newReview1s="";
                            if (!("".equals(review1))){
                                review1=base64Util.decodeToString(review1);
                                newReview1s=review1+","+frontReview.getId();
                            }else{
                                newReview1s=newReview1s+frontReview.getId();
                            }
                            resite.setReview1(base64Util.encodeToString(newReview1s));
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
                returnVO.setCode(500);
                returnVO.setMessage("recite为空");
            }
        }
        return returnVO;
    }

    @RequestMapping(value = "/reciteOne",method = RequestMethod.POST)
    public ReturnVO reciteOne(@RequestParam(value = "cikuexampleid")Integer cikuexampleid, @RequestParam(value = "reciteid")Integer reciteid)
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
                if (!("".equals(learn))){
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
                    String review1 = resite.getReview1();
                    String  nowReview1Base64="";
                    if (!("".equals(review1))){
                        String[] cikuexampleidsReview = base64Util.decodeToString(review1).split(",");
                        String nowReview1="";
                        for (int i = 0; i < cikuexampleidsReview.length; i++) {
                            nowReview1+=(cikuexampleidsReview[i]+",");
                        }
                        nowReview1+=String.valueOf(cikuexampleid);
                        nowReview1Base64 =base64Util.encodeToString( nowReview1);
                    }else{
                        nowReview1Base64+=base64Util.encodeToString(String.valueOf(cikuexampleid));
                    }
                    resite.setReview1(nowReview1Base64);

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
            }else{
                returnVO.setCode(500);
                returnVO.setMessage("recite为空");
            }
        }
        return returnVO;
    }
}
