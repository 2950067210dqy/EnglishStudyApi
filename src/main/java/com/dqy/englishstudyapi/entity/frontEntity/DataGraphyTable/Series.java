package com.dqy.englishstudyapi.entity.frontEntity.DataGraphyTable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Repository
public class Series {
    String name;
    String textColor;
    ArrayList<Integer> data;
}
