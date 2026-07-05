package com.dqy.englishstudyapi.entity.frontEntity.DataGraphyTable.Pie;

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
public class SeriesItem {
    String name;
    Integer value;
}
