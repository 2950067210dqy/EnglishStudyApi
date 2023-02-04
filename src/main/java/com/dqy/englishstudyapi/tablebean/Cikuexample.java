package com.dqy.englishstudyapi.tablebean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
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
 * @since 2023-02-03
 */
@Data
@NoArgsConstructor
@ToString
@AllArgsConstructor
@Repository
@TableName("cikuexample")
@ApiModel(value = "Cikuexample对象", description = "")
public class Cikuexample implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField("initial")
    private String initial;

    @TableField("wid")
    private Integer wid;

    @TableField("deleted")
    @TableLogic
    private Integer deleted;

    @TableField("createtime")
    private LocalDateTime createtime;

    @TableField("updatetime")
    private LocalDateTime updatetime;


}
