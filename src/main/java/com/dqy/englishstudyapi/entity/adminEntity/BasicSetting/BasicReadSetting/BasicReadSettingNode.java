package com.dqy.englishstudyapi.entity.adminEntity.BasicSetting.BasicReadSetting;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@ToString
@AllArgsConstructor
@Repository
public class BasicReadSettingNode {
    String parentDsc;
    String dsc;
    LocalDateTime createtime;
    Integer count;
}
