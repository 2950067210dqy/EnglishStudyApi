package com.dqy.englishstudyapi.util;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

@Component
public class FileUtil {

    public File getFile(MultipartFile multipartFile){
        // 获取文件名
        String fileName =multipartFile.getOriginalFilename();
        // 获取文件后缀
        String prefix = fileName.substring(fileName.lastIndexOf("."));
        // 若须要防止生成的临时文件重复,能够在文件名后添加随机码

        try {
            File file = File.createTempFile(fileName, prefix);
            multipartFile.transferTo(file);
            return file;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


     /** ；

     2      * TODO 下载文件到本地 ；

     3      * @author nadim  ；
     4      * @date Sep 11, 2015 11:45:31 AM ；

     5      * @param fileUrl 远程地址 ；

     6      * @param fileLocal 本地路径 ；

     7      * @throws Exception ；
     8      */
     public void downloadFile(String fileUrl,String fileLocal) throws Exception {

                 URL url = new URL(fileUrl);

                HttpURLConnection urlCon = (HttpURLConnection) url.openConnection();

                urlCon.setConnectTimeout(20000);

                 urlCon.setReadTimeout(200000);
                 int code = urlCon.getResponseCode();

               if (code != HttpURLConnection.HTTP_OK) {
                   System.out.println(code);
                   System.out.println(fileUrl);
                         throw new Exception("网络地址文件读取失败,返回网络码"+code);

                     }

                 //读文件流；
                 File dest = new File(fileLocal);
                 // 检测是否存在目录
                 if (!dest.getParentFile().exists()) {
                     dest.getParentFile().mkdirs();// 新建文件夹
                 }
                DataInputStream in = new DataInputStream(urlCon.getInputStream());

                DataOutputStream out = new DataOutputStream(new FileOutputStream(fileLocal));

                 byte[] buffer = new byte[2048];

                 int count = 0;

                while ((count = in.read(buffer)) > 0) {

                        out.write(buffer, 0, count);

                   }

                out.close();

               in.close();

             }

}
