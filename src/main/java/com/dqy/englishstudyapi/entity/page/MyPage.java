package com.dqy.englishstudyapi.entity.page;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
@Data
@NoArgsConstructor
@ToString
@AllArgsConstructor
@Repository
public class MyPage<T> {
    ArrayList<T> data;
    T one;
    Integer current;
    Integer pageSize;
    Integer total;
}
