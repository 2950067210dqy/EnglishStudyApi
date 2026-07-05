package com.dqy.englishstudyapi.entity.frontEntity.ReadSimple;

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

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Repository
public class ReadSimple {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;


    @TableField("name")
    private String name;

    @TableField("author")
    private String author;

    @TableField("brief")
    private String brief;

    @TableField("image")
    private String image;

    @TableField("updatetime")
    private LocalDateTime updatetime;

}
