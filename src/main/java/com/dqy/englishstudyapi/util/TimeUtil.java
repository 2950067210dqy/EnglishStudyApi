package com.dqy.englishstudyapi.util;



import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.util.Date;

@Component
public class TimeUtil {

  public Date getCurrentTimeDate(){
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
    public Timestamp getCurrentTimeTimeStamp(){
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        String Time=df.format(new Date());// new Date()为获取当前系统时间
        Date date=null;
        try {
            date = df.parse(Time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new Timestamp(date.getTime());
    }
    public LocalDate getCurrentTimeLocalDate(){
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        String Time=df.format(new Date());// new Date()为获取当前系统时间
        Date date=null;
        LocalDate localDate=null;
        try {
            date = df.parse(Time);
            Instant instant = date.toInstant();
            ZoneId zoneId = ZoneId.systemDefault();
            localDate= instant.atZone(zoneId).toLocalDate();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return localDate;
    }
    public LocalDateTime getCurrentTimeLocalDateTime(){
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        String Time=df.format(new Date());// new Date()为获取当前系统时间
        Date date=null;
        LocalDateTime localDateTime=null;
        try {
            date = df.parse(Time);
            Instant instant = date.toInstant();
            ZoneId zoneId = ZoneId.systemDefault();

            localDateTime = instant.atZone(zoneId).toLocalDateTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return localDateTime;
    }
    public LocalDateTime getNowLocalDateTime(){
        return  LocalDateTime.now();
    }
    public long differDay(LocalDateTime startTime, LocalDateTime endTime){
      Duration dur= Duration.between(startTime, endTime );
      return dur.toDays();
    }
    public long differHour(LocalDateTime startTime, LocalDateTime endTime){
        Duration dur= Duration.between(startTime, endTime );
        return dur.toHours();
    }
}
