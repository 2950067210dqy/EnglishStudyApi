package com.dqy.englishstudyapi.entity.adminEntity.BasicSetting.BasicOrderSetting;

import com.dqy.englishstudyapi.entity.frontEntity.DataGraphyTable.Pie.PieGraphyTable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.stereotype.Repository;

@Data
@NoArgsConstructor
@ToString
@AllArgsConstructor
@Repository
public class BasicOrderSetting {
    Long allCount;
    Long todayCount;

    PieGraphyTable data;
}
