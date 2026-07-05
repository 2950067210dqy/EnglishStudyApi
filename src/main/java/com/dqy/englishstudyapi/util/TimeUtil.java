package com.dqy.englishstudyapi.util;



import com.dqy.englishstudyapi.tablebean.Testrecord;
import com.dqy.englishstudyapi.tablebean.Zborder;
import com.sun.org.apache.bcel.internal.generic.RET;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

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

    public ArrayList<String> getRangeDateByDayToStringNotYear(ArrayList<LocalDate> dates){
        ArrayList<String> datestr = new ArrayList<>();
        LocalDate nowDate =  getNowLocalDate();
        for (LocalDate d:dates
             ) {
            String str= String.valueOf(d.getMonthValue())+"-"+String.valueOf(d.getDayOfMonth());
            datestr.add(str);
        }

        return  datestr;
    }
    public ArrayList<String> getRangeDateByDayToStringNotDay(ArrayList<LocalDate> dates){
        ArrayList<String> datestr = new ArrayList<>();
        LocalDate nowDate =  getNowLocalDate();
        for (LocalDate d:dates
        ) {
            String str= String.valueOf(d.getYear())+"-"+String.valueOf(d.getMonthValue());
            datestr.add(str);
        }

        return  datestr;
    }
    public ArrayList<LocalDate> getRangeDateByDay(Integer length){
      ArrayList<LocalDate> dates = new ArrayList<>();
       LocalDate nowDate =  getNowLocalDate();

        for (int i = length-1; i >=0 ; i--) {
            dates.add( nowDate.minusDays(i) );
        }
        return  dates;
    }

    public ArrayList<LocalDate> getRangeDateByMonth(Integer length){
        ArrayList<LocalDate> dates = new ArrayList<>();
        LocalDate nowDate =  getNowLocalDate();
        for (int i = length-1; i >=0 ; i--) {
            dates.add(nowDate.minusMonths(i));
        }
        return  dates;
    }
    public LocalDate getNowLocalDate(){
        return  LocalDate.now();
    }
    public LocalDateTime getNowLocalDateTime(){
        return  LocalDateTime.now();
    }
    public long differDay(LocalDateTime startTime, LocalDateTime endTime){
      Duration dur= Duration.between(startTime, endTime );
      return dur.toDays();
    }
    public LocalDateTime differDay(LocalDateTime startTime,Long day){
        return startTime.minusDays(day);
    }
    public long differDay(LocalDate startTime, LocalDate endTime){
        Duration dur= Duration.between(startTime, endTime );
        return dur.toDays();
    }
    public LocalDate differDay(LocalDate startTime,Long day){
        return startTime.minusDays(day);
    }
    public long differHour(LocalDateTime startTime, LocalDateTime endTime){
        Duration dur= Duration.between(startTime, endTime );
        return dur.toHours();
    }

    public LocalDate differMonth(LocalDate nowDate, long month) {
        return nowDate.minusMonths(month);
    }
    public LocalDateTime differMonth(LocalDateTime nowDate, long month) {
        return nowDate.minusMonths(month);
    }

    /**
     * 计算最大连续打卡的日期天数
     * 例如: [1,2,3,5,7]  则判定为连续有3次打卡成功
     */
    public int continuousDay(List<LocalDate> dateList){
        if (dateList == null || dateList.size() ==0) {
            return 0;
        }
        dateList = dateList.stream().sorted(LocalDate::compareTo).collect(Collectors.toList());
        int maxContinuousDay = 1;
        int continuousDay = 1;
        for (int i = 0; i < dateList.size(); i++) {
            if (i==dateList.size()-1){
                break;
            }
            LocalDate date = dateList.get(i);
            LocalDate secondDate = dateList.get(i + 1);
            if (date.plusDays(1).equals(secondDate)){
                continuousDay ++;
            }else {
                if (continuousDay >= maxContinuousDay){
                    maxContinuousDay = continuousDay;
                }
                continuousDay = 1;
            }
        }

        return maxContinuousDay>continuousDay?maxContinuousDay:continuousDay;
    }

    public String getOnlyTimeNoDate(Testrecord tr) {
        String  time ="";
        Integer hour =tr.getUpdatetime().getHour();
        Integer min = tr.getUpdatetime().getMinute();
        Integer sec = tr.getUpdatetime().getSecond();
        time+= prefixZeroFill(hour);
        time+=":";
        time+= prefixZeroFill(min);
        time+=":";
        time+= prefixZeroFill(sec);
        return  time;
    }
    public ArrayList<String> getOnlyTimeNoDate(List<Testrecord> testrecordsType) {
        ArrayList<String> times = new ArrayList<>();
        for (Testrecord tr:testrecordsType
             ) {
            String  time ="";
            Integer hour =tr.getUpdatetime().getHour();
            Integer min = tr.getUpdatetime().getMinute();
            Integer sec = tr.getUpdatetime().getSecond();
            time+= prefixZeroFill(hour);
            time+=":";
            time+= prefixZeroFill(min);
            time+=":";
            time+= prefixZeroFill(sec);
           times.add(time);
        }
        return times;
    }
    public ArrayList<String> getOnlyTimeNoDateZborder(List<Zborder> zborders) {
        ArrayList<String> times = new ArrayList<>();
        for (Zborder  tr:zborders
        ) {
            String  time ="";
            Integer hour =tr.getUpdatetime().getHour();
            Integer min = tr.getUpdatetime().getMinute();
            Integer sec = tr.getUpdatetime().getSecond();
            time+= prefixZeroFill(hour);
            time+=":";
            time+= prefixZeroFill(min);
            time+=":";
            time+= prefixZeroFill(sec);
            times.add(time);
        }
        return times;
    }

    //一位数 前面补0
    public String prefixZeroFill(Integer num){
        String  time ="";
        if (num<10){
            time+=("0"+num.toString());
        }else {
            time+=(num.toString());
        }
        return time;
    }


}
