package com.dqy.englishstudyapi.entity.adminEntity.BasicSetting.BasicScoreSetting;

import com.dqy.englishstudyapi.entity.frontEntity.DataGraphyTable.DataGraphyTable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.stereotype.Repository;

import java.util.List;

@Data
@NoArgsConstructor
@ToString
@AllArgsConstructor
@Repository
public class BasicScoreSetting {
    MaxScore max;
    Long todayMoney;
    Long allMoney;
    List<DataGraphyTable> data;
}
