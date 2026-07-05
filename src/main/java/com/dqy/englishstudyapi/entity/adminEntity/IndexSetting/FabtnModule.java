package com.dqy.englishstudyapi.entity.adminEntity.IndexSetting;

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
public class FabtnModule {
    FabBtn1Text fabBtn1Text;
    List<FabBtn2Text> fabBtn2Text;
}
