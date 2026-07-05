package com.dqy.englishstudyapi.entity.frontEntity.OrderFull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.stereotype.Repository;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Repository
public class LogisticsResultMessage {
//    "time": "2018-03-09 11:59:26",
//            "status": "【石家庄市】快件已在【长安三部】 签收,签收人: 本人,感谢使用中通快递,期待再次为您服务!"

    String time;
    String status;
}
