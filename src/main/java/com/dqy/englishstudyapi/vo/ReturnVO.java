package com.dqy.englishstudyapi.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Repository
@ApiModel(value = "接口返回实体类",description = "所有接口返回都是此实体类")
public class ReturnVO {
    public static final String OK_MESSAGE = "返回成功";
    public static final String PARAM_ERROR_MESSAGE = "参数有问题";
    public static final String EXECUTE_ERROR_MESSAGE = "运行有问题";
    @ApiModelProperty(required = false,value = "200表示返回成功")
    public static final int OK=200;
    @ApiModelProperty(required = false,value = "400表示参数有问题")
    public  static  final int PARAM_ERROR=400;
    @ApiModelProperty(required = false,value = "500表示运行有问题")
    public  static final int EXECUTE_ERROR=500;
    @ApiModelProperty(required = true,value = "返回码，200表示返回成功，400表示参数有问题，500表示运行有问题")
    private int code;   // 返回码
    @ApiModelProperty(required = true,value = "返回信息")
    private String message; // 返回的message
    @ApiModelProperty(required = false,value = "返回数据")
    private Object data;  // 返回的数据
    @ApiModelProperty(required = false,value = "返回多个数据")
    private List<Object> datas;  // 返回的数据
}
