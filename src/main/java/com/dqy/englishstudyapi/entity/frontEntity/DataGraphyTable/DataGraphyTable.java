package com.dqy.englishstudyapi.entity.frontEntity.DataGraphyTable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.ArrayList;
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Repository
public class DataGraphyTable {
    ArrayList<String> categories;
    ArrayList<Series> series;
}
