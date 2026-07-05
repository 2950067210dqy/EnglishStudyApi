package com.dqy.englishstudyapi.entity.frontEntity.ReadFront;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Repository
public class ReadFront {

    private Integer id;


    private List<String> sentences;


    private String name;


    private String author;


    private String brief;

    private String image;

    private Integer deleted;


    private LocalDateTime createtime;


    private LocalDateTime updatetime;
}
