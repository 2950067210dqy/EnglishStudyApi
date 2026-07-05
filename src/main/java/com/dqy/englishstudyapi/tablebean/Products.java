package com.dqy.englishstudyapi.tablebean;

import com.baomidou.mybatisplus.annotation.*;

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
 * @since 2023-02-21
 */
@Data
@NoArgsConstructor
@ToString
@AllArgsConstructor
@Repository
@TableName("products")
@ApiModel(value = "Products对象", description = "")
public class Products implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("序号")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("商品名称")
    @TableField("title")
    private String title;

    @ApiModelProperty("商品价格")
    @TableField("price")
    private Float price;

    @TableField("merchant_id")
    private Integer merchantId;

    @ApiModelProperty("商品图片地址")
    @TableField("img_addre")
    private String imgAddre;

    @ApiModelProperty("商品详情超链接地址")
    @TableField("product_addre")
    private String productAddre;

    @ApiModelProperty("更新时间")
    @TableField("time")
    private LocalDateTime time;

    @TableField("ptypeid")
    private Integer ptypeid;

    @TableField("ptypesubid")
    private Integer ptypesubid;

    @TableField("num")
    private Integer num;

    @TableField("deleted")
    @TableLogic
    private Integer deleted;
}
