package com.dqy.englishstudyapi.entity.adminEntity.administer;

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
public class FrontDatas {
    String dsc;
    Long num;
    String icon;
}
