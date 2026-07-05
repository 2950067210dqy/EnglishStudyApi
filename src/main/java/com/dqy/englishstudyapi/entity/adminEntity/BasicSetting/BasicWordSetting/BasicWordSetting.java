package com.dqy.englishstudyapi.entity.adminEntity.BasicSetting.BasicWordSetting;

import com.dqy.englishstudyapi.entity.frontEntity.DataGraphyTable.DataGraphyTable;
import com.dqy.englishstudyapi.entity.frontEntity.DataGraphyTable.Pie.PieGraphyTable;
import com.dqy.englishstudyapi.tablebean.User;
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
public class BasicWordSetting {
    Long allCount;

   DataGraphyTable data;
}
