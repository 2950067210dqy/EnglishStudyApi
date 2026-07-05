package com.dqy.englishstudyapi.timetask;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dqy.englishstudyapi.entity.adminEntity.IndexSetting.EveryDayWord;
import com.dqy.englishstudyapi.entity.adminEntity.IndexSetting.EveryDayWordModule;
import com.dqy.englishstudyapi.entity.adminEntity.IndexSetting.WordSetting;
import com.dqy.englishstudyapi.entity.adminEntity.IndexSetting.WordSettingData;
import com.dqy.englishstudyapi.entity.frontEntity.CikuWord;
import com.dqy.englishstudyapi.service.WordService;
import com.dqy.englishstudyapi.tablebean.Word;
import com.dqy.englishstudyapi.util.*;
import com.dqy.englishstudyapi.vo.ReturnVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.system.ApplicationHome;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;
import springfox.documentation.spring.web.json.Json;

import java.io.File;

@Component

@EnableScheduling
public class EverDayWordTask {
    String wordSettingUrl = "static/setting/wordsetting.json";
    @Autowired
    WordService wordService;
    @Autowired
    ListUtil listUtil;
    @Autowired
    DynamicTableNameUtil dynamicTableNameUtil;
    @Autowired
    RandomUtil randomUtil;
    @Autowired
    JsonUtil jsonUtil;
    @Autowired
    FileUtil fileUtil;
    //5秒钟换一次
//    @Scheduled(fixedDelay = 5000)
    //每天0点换词
    @Scheduled(cron = "0 0 0 * * ?")
    public void changeEveryDayWord(){

        Integer initialRandom =  randomUtil.getRandomRange(0,listUtil.abs.length-1);
        if (initialRandom<0||initialRandom>listUtil.abs.length-1){

            System.out.println("随机数initialRandom错误");
            return ;
        }
        Character initial = listUtil.abs[initialRandom];
        dynamicTableNameUtil.SetTableName("word","_"+initial.toString());

        Long count = wordService.count();
        if (count!=null&&count!=0L){
            Integer random =  randomUtil.getRandomRange(0, Math.toIntExact(count)-1);
            if (random<0||random>count-1L){
                System.out.println("随机数random错误");
                return ;
            }
            Word word = wordService.getOne(new QueryWrapper<Word>().last(" limit "+random+",1 "));
            if (word!=null){

                System.out.println("获取成功");
                String os = System.getProperty("os.name");
                String path = "";
                String filePath="";
                if (os.toLowerCase().startsWith("win")) {
                    //windows系统
                    filePath = ClassUtils.getDefaultClassLoader().getResource("").getPath();
//                String filePath= String.valueOf(this.getClass().getResource("/"));
                    System.out.println("path = " + filePath);
                    path = filePath + wordSettingUrl;
                    System.out.println("构造路径" + path);
                } else {
                    //linux系统

                    ApplicationHome home = new ApplicationHome(getClass());//获取jar包地址 /dqy
                    File jarFile = home.getSource();
                    filePath = jarFile.getParentFile().getPath()+"/";
                    path =filePath  + wordSettingUrl;
                    System.out.println("构造路径" + path);
                }
                File dest = new File(path);
                // 检测是否存在目录
                if (!dest.getParentFile().exists()) {
                    dest.getParentFile().mkdirs();// 新建文件夹
                }

                String jsonStr = jsonUtil.readJsonFile(dest);
                if (jsonStr.equals("{}")){
                    System.out.println("每日单词设置json为空");
                    return;
                }
                WordSetting wordSetting = (WordSetting) jsonUtil.parseJsonStrToJavaObject(jsonStr, WordSetting.class);
                WordSettingData wordSettingData = wordSetting.getData();
                EveryDayWordModule everyDayWordModule =wordSettingData.getEveryDayWordModule();
                EveryDayWord everyDayWord = everyDayWordModule.getEveryDayWord();
                everyDayWord.setTrans(word.getTrans());
                everyDayWord.setSoundmark1(word.getSoundmark1());
                everyDayWord.setSoundmark2(word.getSoundmark2());
                everyDayWord.setSoundUrl1("audio/1/"+word.getWord().substring(0, 1).toLowerCase()+"/"+word.getWord()+".mp3");
                everyDayWord.setSoundUrl2("audio/2/"+word.getWord().substring(0, 1).toLowerCase()+"/"+word.getWord()+".mp3");
                everyDayWord.setValue(word.getWord());
                everyDayWordModule.setEveryDayWord(everyDayWord);
                wordSettingData.setEveryDayWordModule(everyDayWordModule);
                wordSetting.setData(wordSettingData);
                jsonUtil.storeJson(jsonUtil.parseObjectToJsonString(wordSetting),dest);
                System.out.println("每日单词设置成功");
            }else{
                System.out.println("获取单词错误");
                return ;
            }

        }else{
            System.out.println("获取单词库错误或为空");
            return ;
        }

    }
}
