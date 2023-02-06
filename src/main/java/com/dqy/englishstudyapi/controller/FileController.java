package com.dqy.englishstudyapi.controller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.dqy.englishstudyapi.entity.frontEntity.CiKuWordEasy;
import com.dqy.englishstudyapi.entity.frontEntity.CikuWord;
import com.dqy.englishstudyapi.entity.frontEntity.ImportWordsEntity;
import com.dqy.englishstudyapi.service.CikuService;
import com.dqy.englishstudyapi.service.CikutypeService;
import com.dqy.englishstudyapi.tablebean.Cikutype;
import com.dqy.englishstudyapi.util.FileUtil;
import com.dqy.englishstudyapi.util.JsonUtil;
import com.dqy.englishstudyapi.util.TimeUtil;
import com.dqy.englishstudyapi.vo.ReturnVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.system.ApplicationHome;
import org.springframework.util.ClassUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

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
    TimeUtil timeUtil;
    ReturnVO returnVO;
    @RequestMapping(value = "/uploadTxt",produces="application/json; charset=UTF-8")
    public ReturnVO upload(@RequestParam(value = "file") MultipartFile file, @RequestParam(value = "cikuType") String cikuType, @RequestParam(value = "cikuTypeId") Integer cikuTypeId,
                         @RequestParam(value = "cikuName") String cikuName, @RequestParam(value = "user",required = false) String user

                         ){
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
                return  returnVO;
            }else{
                // 获取文件名
//            String fileName = file.getOriginalFilename();
                String suffix=file.getOriginalFilename().substring(file.getOriginalFilename().indexOf(".")+1);
                String fileName =cikuName+"."+suffix;
                System.out.println("上传的文件名为：" + fileName);

                String os = System.getProperty("os.name");
                String path="";
                if (os.toLowerCase().startsWith("win")) {
                    //windows系统
                    String filePath= ClassUtils.getDefaultClassLoader().getResource("").getPath();
//                String filePath= String.valueOf(this.getClass().getResource("/"));
                    System.out.println("path = " + filePath);
                    path = filePath +"static/Ciku/"+cikuType+"/"+ fileName;
                    System.out.println("构造路径"+path);
                }else{
                    //linux系统

                    ApplicationHome home = new ApplicationHome(getClass());//获取jar包地址 /dqy
                    File jarFile = home.getSource();
                    path = jarFile.getParentFile().getPath()+"/static/Ciku/"+cikuType+"/"+ fileName;
                    System.out.println("构造路径"+path);
                }
                File dest = new File(path);
                // 检测是否存在目录
                if (!dest.getParentFile().exists()) {
                    dest.getParentFile().mkdirs();// 新建文件夹
                }
//            file.transferTo(dest);// 文件写入

                CiKuWordEasy ciKuWordEasy = new CiKuWordEasy();
                if (suffix.equals("json")){
                    String jsonStr = jsonUtil.readJsonFile(fileUtil.getFile(file) );
                    String jsonStr3 =jsonStr;
                    if (jsonStr3.length()>1){
                       jsonStr3= jsonStr3.substring(1,jsonStr3.length()-1);
                       String[] strings=jsonStr3.split(",");
                        for (String s:strings
                             ) {
                            System.out.println(s);
                        }
                    }
//            System.out.println(jsonStr);
                    JSONObject jsonObject = JSONObject.parseObject(jsonStr);
                    CikuWord cikuWord =JSONObject.toJavaObject(jsonObject, CikuWord.class);
//            System.out.println(cikuWord);
                    ciKuWordEasy.setWords(cikuWord.getUnMasteredWords());
                    String jsonStr2 = JSONObject.toJSONString(ciKuWordEasy);
                    jsonUtil.storeJson(jsonStr2,dest);
                }else{

                }


                String returnPath=fileName;
                returnVO.setCode(200);
                returnVO.setMessage("上传文件成功");
                returnVO.setData(returnPath);

                if (cikuTypeId==-1){
                    Cikutype cikutypeObj = new Cikutype();
                    cikutypeObj.setDsc(cikuType);
                    cikutypeObj.setCreatetime(timeUtil.getCurrentTimeLocalDateTime());
                    cikutypeService.save(cikutypeObj);
                    cikuTypeId=cikutypeObj.getId();
                }
                ImportWordsEntity importWordsEntity = new ImportWordsEntity();
                importWordsEntity.setWords(ciKuWordEasy.getWords());
                importWordsEntity.setCikuName(cikuName);
                importWordsEntity.setCikuTypeId(cikuTypeId);

//                boolean result = cikuService.importWords( importWordsEntity);
//                if (result){
//                    returnVO.setCode(returnVO.OK);
//                    returnVO.setMessage(returnVO.OK_MESSAGE);
//                    System.out.println(returnVO.OK_MESSAGE);
//                }else{
//                    returnVO.setCode(returnVO.EXECUTE_ERROR);
//                    returnVO.setMessage(returnVO.EXECUTE_ERROR_MESSAGE);
//                }
                System.out.println(returnVO);
                return  returnVO;
            }
        }
        catch (IllegalStateException e) {
            e.printStackTrace();
            returnVO.setCode(500);
            returnVO.setMessage("上传失败");
            return  returnVO;
        }




    }
}

