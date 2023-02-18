package com.dqy.englishstudyapi.controller;


import com.alibaba.fastjson.JSONObject;
import com.dqy.englishstudyapi.entity.frontEntity.CiKuWordEasy;
import com.dqy.englishstudyapi.entity.frontEntity.CikuWord;
import com.dqy.englishstudyapi.entity.frontEntity.ImportWordsEntity;
import com.dqy.englishstudyapi.entity.frontEntity.RegisterUser;
import com.dqy.englishstudyapi.service.*;
import com.dqy.englishstudyapi.tablebean.*;
import com.dqy.englishstudyapi.util.*;
import com.dqy.englishstudyapi.vo.ReturnVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.system.ApplicationHome;
import org.springframework.util.ClassUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

@RestController
@RequestMapping("file")
public class FileController {
    @Value("${dqy.address}")
    public String address;
    @Autowired
    JsonUtil jsonUtil;
    @Autowired
    FileUtil fileUtil;
    @Autowired
    CikutypeService cikutypeService;
    @Autowired
    CikuService cikuService;
    @Autowired
    WordService wordService;
    @Autowired
    CikuexampleService cikuexampleService;
    @Autowired
    UserService userService;
    @Autowired
    TimeUtil timeUtil;
    @Autowired
    ListUtil listUtil;
    @Autowired
    WordUtil wordUtil;
    @Autowired
    Base64Util base64Util;
    ReturnVO returnVO;

    @RequestMapping(value = "/uploadTxt", produces = "application/json; charset=UTF-8")
    public ReturnVO upload(@RequestParam(value = "file") MultipartFile file, @RequestParam(value = "cikuType") String cikuType, @RequestParam(value = "cikuTypeId") Integer cikuTypeId,
                           @RequestParam(value = "cikuName") String cikuName, @RequestParam(value = "cikuNameABB")  String cikuNameABB, @RequestParam(value = "user", required = false) String user

    ) {
        returnVO = new ReturnVO();
        ArrayList<String> words = new ArrayList<>();
        // 测试MultipartFile接口的各个方法
//        System.out.println("文件类型ContentType=" + file.getContentType());
//        System.out.println("文件组件名称Name=" + file.getName());
//        System.out.println("文件原名称OriginalFileName=" + file.getOriginalFilename());
//        System.out.println("文件大小Size=" + file.getSize()/1024 + "KB");
        try {
            if (file.isEmpty()) {
                returnVO.setCode(500);
                returnVO.setMessage("文件为空！");
                return returnVO;
            } else {
                // 获取文件名
//            String fileName = file.getOriginalFilename();
                String suffix = file.getOriginalFilename().substring(file.getOriginalFilename().indexOf(".") + 1);
                String fileName = cikuName + "." + "json";

                System.out.println("上传的文件名为：" + fileName);

                String os = System.getProperty("os.name");
                String path = "";
                if (os.toLowerCase().startsWith("win")) {
                    //windows系统
                    String filePath = ClassUtils.getDefaultClassLoader().getResource("").getPath();
//                String filePath= String.valueOf(this.getClass().getResource("/"));
                    System.out.println("path = " + filePath);
                    path = filePath + "static/Ciku/" + cikuType + "/" + fileName;
                    System.out.println("构造路径" + path);
                } else {
                    //linux系统

                    ApplicationHome home = new ApplicationHome(getClass());//获取jar包地址 /dqy
                    File jarFile = home.getSource();
                    path = jarFile.getParentFile().getPath() + "/static/Ciku/" + cikuType + "/" + fileName;
                    System.out.println("构造路径" + path);
                }
                File dest = new File(path);
                // 检测是否存在目录
                if (!dest.getParentFile().exists()) {
                    dest.getParentFile().mkdirs();// 新建文件夹
                }
//            file.transferTo(dest);// 文件写入

                CiKuWordEasy ciKuWordEasy = new CiKuWordEasy();
                if (suffix.equals("json")) {
                    String jsonStr = jsonUtil.readJsonFile(fileUtil.getFile(file));

//            System.out.println(jsonStr);
                    JSONObject jsonObject = JSONObject.parseObject(jsonStr);
                    CikuWord cikuWord = JSONObject.toJavaObject(jsonObject, CikuWord.class);
//            System.out.println(cikuWord);
                    words=cikuWord.getUnMasteredWords();
                    ciKuWordEasy.setWords(words);
                } else {
                    InputStream io = file.getInputStream();
                    BufferedReader reader;
                    reader = new BufferedReader( new InputStreamReader(io));
                    String line = reader.readLine();

                    while (line != null) {
                        String word =line.toString().trim().replace("\n","");
                        words.add(word);
                        // read next line
                        line = reader.readLine();
                    }
                    ciKuWordEasy.setWords(words);
                }
                String jsonStr2 = JSONObject.toJSONString(ciKuWordEasy);
                jsonUtil.storeJson(jsonStr2, dest);

//                String returnPath = fileName;
//                returnVO.setCode(200);
//                returnVO.setMessage("上传文件成功");
//                returnVO.setData(returnPath);
            }
        } catch (  IOException e) {
            e.printStackTrace();
            returnVO.setCode(500);
            returnVO.setMessage("文件失败，文件格式错误");
            return returnVO;
        }

        words=wordUtil.filter(words);

        if (words.size()!=0){
            if (cikuTypeId == -1) {
                Cikutype cikutypeObj = new Cikutype();
                cikutypeObj.setDsc(cikuType);
                cikutypeObj.setCreatetime(timeUtil.getCurrentTimeLocalDateTime());
                boolean result =cikutypeService.save(cikutypeObj);
                if (!result) {
                    returnVO.setCode(500);
                    returnVO.setMessage("词库类型插入失败");
                    return returnVO;
                }
                cikuTypeId = cikutypeObj.getId();
            }
            Ciku ciku = new Ciku();
            ciku.setDsc(cikuName);
            ciku.setDscabb(cikuNameABB);
            ciku.setCreatetime(timeUtil.getCurrentTimeLocalDateTime());
            ciku.setUid(1);
            boolean result = cikuService.save(cikuTypeId,ciku);
            if (!result) {
                returnVO.setCode(500);
                returnVO.setMessage("词库具体名称插入失败");
                return returnVO;
            }
            Integer cikuId = ciku.getId();
            ArrayList<Cikuexample> cikuexamples = new ArrayList<>();
            HashMap<Character,ArrayList<String>> wordsMap =listUtil.stringListClassifyByInitial(words);
            wordsMap.keySet().forEach(key ->{
                ArrayList<String> partWords = wordsMap.get(key);
                if (partWords.size()!=0){
                    //导入单词 进入单词service
                    ArrayList<Word> wordArrayList= wordService.selectByWordBatch(String.valueOf(key), partWords);
                    for (Word w:wordArrayList
                         ) {
                        Cikuexample cikuexample = new Cikuexample();
                        cikuexample.setCreatetime(timeUtil.getCurrentTimeLocalDateTime());
                        cikuexample.setInitial(String.valueOf(key));
                        cikuexample.setWid(w.getId());
                        cikuexamples.add(cikuexample);
                    }

                }
            });

//            for (String word:words
//            ) {
//                String initial =word.substring(0,1).toLowerCase().trim();
//                Word wordObj =  wordService.selectByWord(initial,word);
//                if (wordObj!=null){
//                    Cikuexample cikuexample = new Cikuexample();
//                    cikuexample.setCreatetime(timeUtil.getCurrentTimeLocalDateTime());
//                    cikuexample.setInitial(initial);
//                    cikuexample.setWid(wordObj.getId());
//                    cikuexamples.add(cikuexample);
//                }
//            }
            result = cikuexampleService.insertBatch(cikuTypeId,cikuId,cikuexamples);
            if (result){
                returnVO.setCode(200);
                returnVO.setMessage("导入词库成功");
                return returnVO;
            }else{
                returnVO.setCode(500);
                returnVO.setMessage("导入词库失败");
                return returnVO;
            }
        }else {
            returnVO.setCode(500);
            returnVO.setMessage("文件的单词为空");
            return returnVO;
        }
    }



    @RequestMapping(value = "/uploadImageRegister" )
      public ReturnVO  registerByHeadImageUploadByVeriFy(@RequestParam(value = "file") MultipartFile file,@RequestParam("user") String user, HttpServletRequest request){
            returnVO = new ReturnVO();
            User registerUser = (User) jsonUtil.parseBase64ToJsonStrThenToJavaObject(user,User.class);

        returnVO = new ReturnVO();
        // 测试MultipartFile接口的各个方法
//        System.out.println("文件类型ContentType=" + file.getContentType());
//        System.out.println("文件组件名称Name=" + file.getName());
//        System.out.println("文件原名称OriginalFileName=" + file.getOriginalFilename());
//        System.out.println("文件大小Size=" + file.getSize()/1024 + "KB");
        try {
            if (file.isEmpty()) {
                returnVO.setCode(500);
                returnVO.setMessage("文件为空！");
                return returnVO;
            }
            // 获取文件名
            String fileName = file.getOriginalFilename();
            System.out.println("上传的文件名为：" + fileName);

            String os = System.getProperty("os.name");
            String path="";
            if (os.toLowerCase().startsWith("win")) {
                //windows系统
                String filePath= ClassUtils.getDefaultClassLoader().getResource("").getPath();;
                System.out.println("path = " + filePath);
                path = filePath +"static/image/headImage/"+ fileName;
                System.out.println("构造路径"+path);
            }else{
                //linux系统

                ApplicationHome home = new ApplicationHome(getClass());//获取jar包地址 /dqy
                File jarFile = home.getSource();
                path = jarFile.getParentFile().getPath()+"/static/image/headImage/"+fileName;
                System.out.println("构造路径"+path);
            }

            File dest = new File(path);
            // 检测是否存在目录
            if (!dest.getParentFile().exists()) {
                dest.getParentFile().mkdirs();// 新建文件夹
            }
            file.transferTo(dest);// 文件写入
            String returnPath=fileName;
            registerUser.setType(0);
            registerUser.setDeleted(0);
            registerUser.setCreatetime(timeUtil.getCurrentTimeLocalDateTime());
            registerUser.setHeadimage(returnPath);
            boolean result =userService.save(registerUser);
            if (result){
                returnVO.setCode(200);
                returnVO.setMessage("注册成功");
            }else{
                returnVO.setCode(500);
                returnVO.setMessage("注册成功，但上传头像成功");
            }

            return returnVO;
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        returnVO.setCode(500);
        returnVO.setMessage("上传头像失败");
        return returnVO;
        }

}


