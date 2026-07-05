package com.dqy.englishstudyapi.entity.frontEntity.OrderFull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.stereotype.Repository;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Repository
public class LogisticsResult {
    String number; //快递单号
    String type;    //快递类型 中通 圆通等
    List<LogisticsResultMessage> list;
    String deliverystatus; /* 0：快递收件(揽件)1.在途中 2.正在派件 3.已签收 4.派送失败 5.疑难件 6.退件签收  */
    String  issign;                    /*  1.是否签收                  */
    String  expName ;             /*  快递公司名称                */
    String   expSite ;         /*  快递公司官网                */
    String   expPhone  ;             /*  快递公司电话                */
    String    courier   ;          /*  快递员 或 快递站(没有则为空)*/
    String    courierPhone;      /*  快递员电话 (没有则为空)     */
    String    updateTime; /*  快递轨迹信息最新时间        */
    String     takeTime ;        /*  发货到收货消耗时长 (截止最新轨迹)  */
    String     logo;/* 快递公司LOGO */
}
