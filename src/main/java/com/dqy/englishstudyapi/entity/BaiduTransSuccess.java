package com.dqy.englishstudyapi.entity;

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
public class BaiduTransSuccess {
    String from;
    String to;
    ArrayList<BaiduTransSuccessItem> trans_result;
}
