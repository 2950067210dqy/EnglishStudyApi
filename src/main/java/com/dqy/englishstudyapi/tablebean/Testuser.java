package com.dqy.englishstudyapi.tablebean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
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
@TableName("testuser")
@ApiModel(value = "Testuser对象", description = "")
public class Testuser implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField("username")
    private String username;

    @TableField("name")
    private String name;

    @TableField("phone")
    private String phone;

    @TableField("sex")
    private String sex;

    @TableField("headimg")
    private String headimg;

    @TableField("password")
    private String password;

    @TableField("role")
    private Integer role;

    @TableField("registertime")
    private LocalDateTime registertime;


}
