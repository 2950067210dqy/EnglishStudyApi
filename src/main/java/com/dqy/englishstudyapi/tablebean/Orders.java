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
 * @since 2023-02-22
 */
@Data
@NoArgsConstructor
@ToString
@AllArgsConstructor
@Repository
@TableName("orders")
@ApiModel(value = "Order对象", description = "")
public class Orders implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField("uid")
    private Integer uid;

    @TableField("orders")
    private String orders;

    @ApiModelProperty("商品号")
    @TableField("pid")
    private Integer pid;

    @ApiModelProperty("收获地址号")
    @TableField("aid")
    private Integer aid;

    @ApiModelProperty("物流单号")
    @TableField("trackid")
    private String trackid;

    @ApiModelProperty("物流类型")
    @TableField("tracktype")
    private String tracktype;
    @TableField("orderstatus")
    private Integer orderstatus;


    @TableField("deleted")
    @TableLogic
    private Integer deleted;

    @TableField("createtime")
    private LocalDateTime createtime;

    @TableField("updatetime")
    private LocalDateTime updatetime;


}
