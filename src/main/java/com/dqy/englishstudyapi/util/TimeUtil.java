package com.dqy.englishstudyapi.util;



import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class TimeUtil {

  public Date getCurrentTime(){
       SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
       String Time=df.format(new Date());// new Date()为获取当前系统时间
       Date date=null;
       try {
           date = df.parse(Time);
       } catch (ParseException e) {
           e.printStackTrace();
       }
       return  date;
   }
}
