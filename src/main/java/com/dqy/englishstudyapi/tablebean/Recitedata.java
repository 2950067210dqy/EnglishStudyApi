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
 * @since 2023-02-19
 */
@Data
@NoArgsConstructor
@ToString
@AllArgsConstructor
@Repository
@TableName("recitedata")
@ApiModel(value = "Recitedata对象", description = "")
public class Recitedata implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField("uid")
    private Integer uid;
    @ApiModelProperty("学习单词个数")
    @TableField("num")
    private Integer num;
    @ApiModelProperty("复习单词个数")
    @TableField("num2")
    private Integer num2;

    @ApiModelProperty("单位：分钟")
    @TableField("time")
    private Integer time;

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
