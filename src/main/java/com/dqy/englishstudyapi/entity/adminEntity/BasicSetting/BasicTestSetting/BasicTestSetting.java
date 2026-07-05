package com.dqy.englishstudyapi.entity.adminEntity.BasicSetting.BasicTestSetting;

import com.dqy.englishstudyapi.entity.adminEntity.BasicSetting.BasicCikuSetting.BasicCikuSettingNode;
import com.dqy.englishstudyapi.entity.adminEntity.BasicSetting.BasicReadSetting.BasicReadSettingNode;
import com.dqy.englishstudyapi.tablebean.Testtype;
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
public class BasicTestSetting {
    Long allCount;
    Long todayCount;
    List<BasicTestSettingNode> nodes;
}
