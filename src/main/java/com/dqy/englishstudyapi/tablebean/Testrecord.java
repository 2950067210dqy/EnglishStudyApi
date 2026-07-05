package com.dqy.englishstudyapi.tablebean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
    import lombok.AllArgsConstructor;
    import lombok.Data;
    import lombok.NoArgsConstructor;
    import lombok.ToString;
    import org.springframework.stereotype.Repository;

/**
 * <p>
 * 
 * </p>
 *
 * @author 邓亲优
 * @since 2023-03-01
 */
@Data
@NoArgsConstructor
@ToString
@AllArgsConstructor
@Repository
@TableName("testrecord")
@ApiModel(value = "Testrecord对象", description = "")
public class Testrecord implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField("uid")
    private Integer uid;

    @TableField("testtype")
    private Integer testtype;



    @TableField("type")
    private Integer type;

    @ApiModelProperty("单位：分钟")
    @TableField("time")
    private Integer time;

    @TableField("correctnum")
    private Integer correctnum;

    @TableField("errornum")
    private Integer errornum;

    @ApiModelProperty("type 为1才有")
    @TableField("score")
    private Integer score;

    @TableField("deleted")
    @TableLogic
    private Integer deleted;
    @TableField("createdate")
    private LocalDate createdate;
    @TableField("createtime")
    private LocalDateTime createtime;

    @TableField("updatetime")
    private LocalDateTime updatetime;


}
