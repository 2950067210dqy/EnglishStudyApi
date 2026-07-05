package com.dqy.englishstudyapi.entity.frontEntity.DataGraphyTable.Pie;


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
public class PieGraphyTable {
    List<SeriesPie> series;
}
