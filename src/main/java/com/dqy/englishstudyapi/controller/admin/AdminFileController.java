package com.dqy.englishstudyapi.controller.admin;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dqy.englishstudyapi.entity.adminEntity.IndexSetting.WordSetting;
import com.dqy.englishstudyapi.entity.adminEntity.administer.FrontWords;
import com.dqy.englishstudyapi.tablebean.Liju;
import com.dqy.englishstudyapi.tablebean.Word;
import com.dqy.englishstudyapi.util.FileUtil;
import com.dqy.englishstudyapi.util.JsonUtil;
import com.dqy.englishstudyapi.util.Md5Util;
import com.dqy.englishstudyapi.vo.ReturnVO;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.system.ApplicationHome;
import org.springframework.util.ClassUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

@RestController
@RequestMapping("adminFile")
public class AdminFileController {
    String wordSettingUrl = "static/setting/wordsetting.json";
    String wordSettingOriginUrl = "static/setting/wordsettingBackUp.json";
    String wordAudioFileUrl = "static/audio/";
    String bg1Url = "static/image/background/bg1.jpg";
    String bg1BackUpUrl = "static/image/background/bg1BackUp.jpg";
    @Autowired
    FileUtil fileUtil;
    @Autowired
    JsonUtil jsonUtil;
    @Autowired
    Md5Util md5Util;
    ReturnVO returnVO;
    @RequestMapping(value = "/uploadProductImage" )
    public ReturnVO  uploadProductImage(@RequestParam(value = "file") MultipartFile file){
        returnVO = new ReturnVO();

        // 测试MultipartFile接口的各个方法
        System.out.println("uploadImage文件类型ContentType=" + file.getContentType());
        System.out.println("文件组件名称Name=" + file.getName());
        System.out.println("文件原名称OriginalFileName=" + file.getOriginalFilename());
        System.out.println("文件大小Size=" + file.getSize()/1024 + "KB");
        try {
            if (file.isEmpty()) {
                returnVO.setCode(500);
                returnVO.setMessage("文件为空！");
                return returnVO;
            }
            // 获取文件名
            String suffix = file.getOriginalFilename().substring(file.getOriginalFilename().indexOf(".") + 1);
            String fileName = file.getOriginalFilename();
            String fileNameMd5 = md5Util.getMD5(file.getInputStream());
            fileNameMd5+="."+suffix;
            System.out.println("上传的文件名为：" + fileName);
            System.out.println("上传的文件名加密md5为：" + fileNameMd5);
            String os = System.getProperty("os.name");
            String path="";

            if (os.toLowerCase().startsWith("win")) {
                //windows系统
                String filePath= ClassUtils.getDefaultClassLoader().getResource("").getPath();;
                System.out.println("path = " + filePath);
                path = filePath +"static/image/productImage/"+ fileNameMd5;
                System.out.println("构造路径"+path);
            }else{
                //linux系统

                ApplicationHome home = new ApplicationHome(getClass());//获取jar包地址 /dqy
                File jarFile = home.getSource();
                path = jarFile.getParentFile().getPath()+"/static/image/productImage/"+fileNameMd5;
                System.out.println("构造路径"+path);
            }

            File dest = new File(path);
            // 检测是否存在目录
            if (!dest.getParentFile().exists()) {
                dest.getParentFile().mkdirs();// 新建文件夹
            }
            file.transferTo(dest);// 文件写入
            String returnPath=fileNameMd5;
            returnVO.setCode(200);
            returnVO.setMessage("上传成功");
            returnVO.setData(returnPath);
            return returnVO;
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        returnVO.setCode(500);
        returnVO.setMessage("上传图片失败");
        return returnVO;
    }
    @RequestMapping(value = "/uploadReadImage" )
    public ReturnVO  uploadReadImage(@RequestParam(value = "file") MultipartFile file){
        returnVO = new ReturnVO();

        // 测试MultipartFile接口的各个方法
        System.out.println("uploadImage文件类型ContentType=" + file.getContentType());
        System.out.println("文件组件名称Name=" + file.getName());
        System.out.println("文件原名称OriginalFileName=" + file.getOriginalFilename());
        System.out.println("文件大小Size=" + file.getSize()/1024 + "KB");
        try {
            if (file.isEmpty()) {
                returnVO.setCode(500);
                returnVO.setMessage("文件为空！");
                return returnVO;
            }
            // 获取文件名
            String suffix = file.getOriginalFilename().substring(file.getOriginalFilename().indexOf(".") + 1);
            String fileName = file.getOriginalFilename();
            String fileNameMd5 = md5Util.getMD5(file.getInputStream());
            fileNameMd5+="."+suffix;
            System.out.println("上传的文件名为：" + fileName);
            System.out.println("上传的文件名加密md5为：" + fileNameMd5);
            String os = System.getProperty("os.name");
            String path="";

            if (os.toLowerCase().startsWith("win")) {
                //windows系统
                String filePath= ClassUtils.getDefaultClassLoader().getResource("").getPath();;
                System.out.println("path = " + filePath);
                path = filePath +"static/image/readImage/"+ fileNameMd5;
                System.out.println("构造路径"+path);
            }else{
                //linux系统

                ApplicationHome home = new ApplicationHome(getClass());//获取jar包地址 /dqy
                File jarFile = home.getSource();
                path = jarFile.getParentFile().getPath()+"/static/image/readImage/"+fileNameMd5;
                System.out.println("构造路径"+path);
            }

            File dest = new File(path);
            // 检测是否存在目录
            if (!dest.getParentFile().exists()) {
                dest.getParentFile().mkdirs();// 新建文件夹
            }
            file.transferTo(dest);// 文件写入
            String returnPath=fileNameMd5;
            returnVO.setCode(200);
            returnVO.setMessage("上传成功");
            returnVO.setData(returnPath);
            return returnVO;
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        returnVO.setCode(500);
        returnVO.setMessage("上传图片失败");
        return returnVO;
    }
    @RequestMapping(value = "/deleteAudio" )
    public ReturnVO  deleteAudio(@RequestParam(value = "type",defaultValue = "1",required = false)Integer type,@RequestParam(value = "word")String word){
        returnVO = new ReturnVO();


        try {

            // 获取文件名
            String fileName = word+".mp3";
            String initial = word.substring(0,1).toLowerCase();


            System.out.println("上传的文件名为：" + fileName);

            String os = System.getProperty("os.name");
            String path="";
            if (os.toLowerCase().startsWith("win")) {
                //windows系统
                String filePath= ClassUtils.getDefaultClassLoader().getResource("").getPath();;
                System.out.println("path = " + filePath);
                path = filePath +"static/audio/"+type+"/"+initial+"/"+fileName;
                System.out.println("构造路径"+path);
            }else{
                //linux系统

                ApplicationHome home = new ApplicationHome(getClass());//获取jar包地址 /dqy
                File jarFile = home.getSource();
                path = jarFile.getParentFile().getPath()+"/static/audio/"+type+"/"+initial+"/"+fileName;
                System.out.println("构造路径"+path);
            }

            File dest = new File(path);
            // 检测是否存在目录
            if (!dest.getParentFile().exists()) {
                dest.getParentFile().mkdirs();// 新建文件夹
            }
            if(dest.delete()){
                returnVO.setCode(200);
                returnVO.setMessage("删除文件成功");

                return returnVO;
            }else{
                returnVO.setCode(500);
                returnVO.setMessage("删除文件失败");

                return returnVO;
            }


        } catch (IllegalStateException e) {
            e.printStackTrace();
        }

        returnVO.setCode(500);
        returnVO.setMessage("删除音频失败");
        return returnVO;
    }

    @RequestMapping(value = "/uploadAudio" )
    public ReturnVO  uploadAudio(@RequestParam(value = "file") MultipartFile file,@RequestParam(value = "type",defaultValue = "1",required = false)Integer type,@RequestParam(value = "word")String word){
        returnVO = new ReturnVO();

        // 测试MultipartFile接口的各个方法
        System.out.println("uploadAudio文件类型ContentType=" + file.getContentType());
        System.out.println("文件组件名称Name=" + file.getName());
        System.out.println("文件原名称OriginalFileName=" + file.getOriginalFilename());
        System.out.println("文件大小Size=" + file.getSize()/1024 + "KB");
        try {
            if (file.isEmpty()) {
                returnVO.setCode(500);
                returnVO.setMessage("文件为空！");
                return returnVO;
            }
            // 获取文件名
            String fileName = word+".mp3";
            String initial = word.substring(0,1).toLowerCase();


            System.out.println("上传的文件名为：" + fileName);

            String os = System.getProperty("os.name");
            String path="";
            if (os.toLowerCase().startsWith("win")) {
                //windows系统
                String filePath= ClassUtils.getDefaultClassLoader().getResource("").getPath();;
                System.out.println("path = " + filePath);
                path = filePath +"static/audio/"+type+"/"+initial+"/"+fileName;
                System.out.println("构造路径"+path);
            }else{
                //linux系统

                ApplicationHome home = new ApplicationHome(getClass());//获取jar包地址 /dqy
                File jarFile = home.getSource();
                path = jarFile.getParentFile().getPath()+"/static/audio/"+type+"/"+initial+"/"+fileName;
                System.out.println("构造路径"+path);
            }

            File dest = new File(path);
            // 检测是否存在目录
            if (!dest.getParentFile().exists()) {
                dest.getParentFile().mkdirs();// 新建文件夹
            }
            file.transferTo(dest);// 文件写入
            String returnPath=fileName;
            returnVO.setCode(200);
            returnVO.setMessage("上传成功");
            returnVO.setData(returnPath);
            return returnVO;
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        returnVO.setCode(500);
        returnVO.setMessage("上传音频失败");
        return returnVO;
    }

    @PostMapping("/getWordAudioFileExist")
    public ReturnVO getWordAudioFileExist(@RequestParam("word")String word, @RequestParam("type")Integer type){
        returnVO = new ReturnVO();
        if (word==null||word.equals("")||type==null){
            returnVO.setCode(500);
            returnVO.setMessage("数据为空");
            return returnVO;
        }
        String os = System.getProperty("os.name");
        String path = "";
        String filePath="";
        if (os.toLowerCase().startsWith("win")) {
            //windows系统
            filePath = ClassUtils.getDefaultClassLoader().getResource("").getPath();
//                String filePath= String.valueOf(this.getClass().getResource("/"));
            System.out.println("path = " + filePath);
            path = filePath + wordAudioFileUrl+type+"/"+word.substring(0,1).toLowerCase()+"/"+word+".mp3" ;
            System.out.println("构造路径" + path);
        } else {
            //linux系统

            ApplicationHome home = new ApplicationHome(getClass());//获取jar包地址 /dqy
            File jarFile = home.getSource();
            filePath = jarFile.getParentFile().getPath()+"/";
            path =filePath + wordAudioFileUrl+type+"/"+word.substring(0,1).toLowerCase()+"/"+word+".mp3" ;
            System.out.println("构造路径" + path);
        }
        File dest = new File(path);
        if (dest.exists()){
            returnVO.setCode(200);
            returnVO.setMessage("文件存在");

        }else{
            returnVO.setCode(500);
            returnVO.setMessage("文件不存在");

        }
        return returnVO;

    }
    @PostMapping("/updateWordSetting")
    public ReturnVO updateWordSetting(@RequestBody WordSetting wordSetting){
        returnVO = new ReturnVO();
        if (wordSetting==null){
            returnVO.setCode(500);
            returnVO.setMessage("数据为空");
            return returnVO;
        }
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
        jsonUtil.storeJson(jsonUtil.parseObjectToJsonString(wordSetting),dest);

        //换背景图片
        fileUtil.copyFile(filePath+"static/image/background/upload/"+wordSetting.getBgUrl(),filePath+bg1Url);

        returnVO.setCode(200);
        returnVO.setMessage("修改成功");
        return returnVO;
    }
    @PostMapping("/updateWordSettingToOrigin")
    public ReturnVO updateWordSettingToOrigin(){
        returnVO = new ReturnVO();

        String os = System.getProperty("os.name");
        String path = "";
        String pathOrigin = "";
        String filePath="";
        if (os.toLowerCase().startsWith("win")) {
            //windows系统
            filePath = ClassUtils.getDefaultClassLoader().getResource("").getPath();
//                String filePath= String.valueOf(this.getClass().getResource("/"));
            System.out.println("path = " + filePath);
            path = filePath + wordSettingUrl;
            pathOrigin=filePath+ wordSettingOriginUrl;
            System.out.println("构造路径" + path);
            System.out.println("构造路径2" + pathOrigin);
        } else {
            //linux系统

            ApplicationHome home = new ApplicationHome(getClass());//获取jar包地址 /dqy
            File jarFile = home.getSource();
            filePath=jarFile.getParentFile().getPath() + "/";
            path =filePath +wordSettingUrl;
            pathOrigin=filePath+wordSettingOriginUrl;
            System.out.println("构造路径" + path);
            System.out.println("构造路径" + pathOrigin);
        }
        File dest = new File(path);
        File destOrigin = new File(pathOrigin);
        // 检测是否存在目录
        if (!dest.getParentFile().exists()) {
            dest.getParentFile().mkdirs();// 新建文件夹
        }
        if (!destOrigin.getParentFile().exists()) {
            destOrigin.getParentFile().mkdirs();// 新建文件夹
        }


        String orginJson = jsonUtil.readJsonFile(destOrigin);

        jsonUtil.storeJson(orginJson,dest);
        //换背景图片
        fileUtil.copyFile(filePath+bg1BackUpUrl,filePath+bg1Url);

        returnVO.setCode(200);
        returnVO.setMessage("修改成功");
        return returnVO;
    }


    @RequestMapping(value = "/uploadImageBackGround" )
    public ReturnVO  uploadImageBackGround(@RequestParam(value = "file") MultipartFile file){
        returnVO = new ReturnVO();

        // 测试MultipartFile接口的各个方法
        System.out.println("uploadImage文件类型ContentType=" + file.getContentType());
        System.out.println("文件组件名称Name=" + file.getName());
        System.out.println("文件原名称OriginalFileName=" + file.getOriginalFilename());
        System.out.println("文件大小Size=" + file.getSize()/1024 + "KB");
        try {
            if (file.isEmpty()) {
                returnVO.setCode(500);
                returnVO.setMessage("文件为空！");
                return returnVO;
            }
            // 获取文件名
            String suffix = file.getOriginalFilename().substring(file.getOriginalFilename().indexOf(".") + 1);
            String fileName = file.getOriginalFilename();
            String fileNameMd5 = md5Util.getMD5(file.getInputStream());
            fileNameMd5+="."+suffix;
            System.out.println("上传的文件名为：" + fileName);
            System.out.println("上传的文件名加密md5为：" + fileNameMd5);
            String os = System.getProperty("os.name");
            String path="";

            if (os.toLowerCase().startsWith("win")) {
                //windows系统
                String filePath= ClassUtils.getDefaultClassLoader().getResource("").getPath();;
                System.out.println("path = " + filePath);
                path = filePath +"static/image/background/upload/"+ fileNameMd5;
                System.out.println("构造路径"+path);
            }else{
                //linux系统

                ApplicationHome home = new ApplicationHome(getClass());//获取jar包地址 /dqy
                File jarFile = home.getSource();
                path = jarFile.getParentFile().getPath()+"/static/image/background/upload/"+fileNameMd5;
                System.out.println("构造路径"+path);
            }

            File dest = new File(path);
            // 检测是否存在目录
            if (!dest.getParentFile().exists()) {
                dest.getParentFile().mkdirs();// 新建文件夹
            }
            file.transferTo(dest);// 文件写入
            String returnPath=fileNameMd5;
            returnVO.setCode(200);
            returnVO.setMessage("上传成功");
            returnVO.setData(returnPath);
            return returnVO;
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        returnVO.setCode(500);
        returnVO.setMessage("上传图片失败");
        return returnVO;
    }

}
