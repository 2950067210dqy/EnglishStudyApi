package com.dqy.englishstudyapi.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.FileNameMap;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

@Component
public class FileUtil<T> {
    @Autowired
    JsonUtil jsonUtil;
    public void copyFile(String oldPath, String newPath) {
        try {
            int bytesum = 0;
            int byteread = 0;
            File oldfile = new File(oldPath);
            if (oldfile.exists()) { //文件存在时
                InputStream inStream = new FileInputStream(oldPath); //读入原文件
                FileOutputStream fs = new FileOutputStream(newPath);
                byte[] buffer = new byte[1444];
                int length;
                while ( (byteread = inStream.read(buffer)) != -1) {
                    bytesum += byteread; //字节数 文件大小
                    System.out.println(bytesum);
                    fs.write(buffer, 0, byteread);
                }
                inStream.close();
            }
        }
        catch (Exception e) {
            System.out.println("复制单个文件操作出错");
            e.printStackTrace();
        }
    }
    public byte[] readDataToBytes(InputStream fileInputStream){
        byte[] bdata=null;
        try {
            bdata = FileCopyUtils.copyToByteArray(fileInputStream );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return bdata;
    }
    public byte[] readDataToBytes(File file){
        byte[] bdata=null;
        try {
             bdata = FileCopyUtils.copyToByteArray(new FileInputStream(file));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return bdata;
    }

    public String readDataToString(File file){
        String data =null;
        byte[] bdata=readDataToBytes(file);
        if (bdata==null){
            return  data;
        }
        data = new String(bdata, StandardCharsets.UTF_8);
        return  data;
    }

    public T readDataToJavaObject(File file){
        T t = null;
        String data =null;
        byte[] bdata=readDataToBytes(file);
        if (bdata==null){
            return  t;
        }
        data = new String(bdata, StandardCharsets.UTF_8);
        t =(T)jsonUtil.parseJsonStrToJavaObject(data,t.getClass());
        return  t;
    }

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
