package com.dqy.englishstudyapi.tablebean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.sql.Timestamp;

@Data
@NoArgsConstructor
@ToString
@AllArgsConstructor
@Repository
@TableName("ciku")
public class Ciku {

            @TableId(value = "id",type = IdType.AUTO)
            Integer id;
            @TableField(value = "cikutableid")
            String cikutableid;
            @TableField(value = "desc")
            String desc;
            @TableField(value = "createtime")
            Timestamp createtime;
            @TableField(value = "updatetime")
            Timestamp updatetime;

}
