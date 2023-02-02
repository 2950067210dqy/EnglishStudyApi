package com.dqy.englishstudyapi.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.stereotype.Repository;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Repository
public class ReturnVO {
    private int code;   // 返回码
    private String message; // 返回的message
    private Object data;  // 返回的数据
    private List<Object> datas;  // 返回的数据
}
