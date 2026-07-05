package com.dqy.englishstudyapi.entity.adminEntity.BasicSetting.BasicProductSetting;

import com.dqy.englishstudyapi.entity.adminEntity.BasicSetting.BasicCikuSetting.BasicCikuSettingNode;
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
public class BasicProductSetting {
    Long allCount;
    Long todayCount;
    List<BasicProductSettingNode> nodes;
}
